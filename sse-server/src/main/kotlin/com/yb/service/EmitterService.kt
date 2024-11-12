package com.yb.service

import brave.Tracer
import com.yb.dto.MessageDto
import com.yb.repository.EmitterRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.IOException


@Service
class EmitterService(
    @Autowired private val emitterRepository: EmitterRepository,
    @Autowired private val tracer: Tracer,
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun subscribe(companyId: String): Flux<Any> {

        val sink = Sinks.many().multicast().onBackpressureBuffer<Any>()

        // 저장소에 Sink를 저장합니다.
        emitterRepository.save(companyId, sink)

        // 첫 구독 시 이벤트 발생
//        sink.tryEmitNext("subscribe event, companyId : $companyId")

        val flux = sink.asFlux()
            .doOnCancel {
                log.info("server sent event complete (cancelled): id={}", companyId)
                emitterRepository.deleteById(companyId)
            }
            .doOnError { error ->
                log.info("server sent event error: id={}, error={}", companyId, error.message)
                emitterRepository.deleteById(companyId)
            }

        emitterRepository.save(companyId, sink)
        return flux
    }

    fun broadcast(id: String, dto: MessageDto) {
        val sink: Sinks.Many<Any> = emitterRepository.findById(id)
        sendToClient(sink, id, dto)
    }

    fun findEmitterByCompanyId(id: String): Sinks.Many<Any> {
        return emitterRepository.findById(id)
    }

    fun sendToClient(sinks: Sinks.Many<Any>?, companyId: String, data: MessageDto) {
        try {
            if (sinks != null) {
                val childSpan = tracer.nextSpan().name("sse-event-${data.id}")
                try {
                    tracer.withSpanInScope(childSpan.start()).use { ws ->
                        // 트레이스 ID를 이벤트 데이터에 포함
                        val traceId: String = childSpan.context().traceIdString()
                        val spanId: String = childSpan.context().spanIdString()
                        val dataObj = ServerSentEvent.builder(
                            "Event 123, TraceId: $traceId, SpanId: $spanId"
                        )
                            .event(traceId)
                            .id("sse-server")
                            .build()

                        sinks.tryEmitNext(dataObj)
                        sinks.tryEmitComplete()
                        log.info("SEND TO MESSAGE")
                    }
                } finally {
                    childSpan.finish()
                }


            }
        } catch (ex: IOException) {
//            sinks.
//            sinks!!.complete()
            emitterRepository.deleteById(companyId)
            //      emitterRepository.deleteAllCacheByCompanyId(companyId);
            log.error("연결 오류 발생 : {}", ex.message)
            //      throw new RuntimeException("연결 오류 발생");
        }
    }

}