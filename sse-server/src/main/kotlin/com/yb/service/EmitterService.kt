package com.yb.service

import brave.Tracer
import com.yb.dto.MessageDto
import com.yb.exception.NotFoundEmitterException
import com.yb.repository.EmitterRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import java.io.IOException


@Service
class EmitterService(
    private val emitterRepository: EmitterRepository,
    private val tracer: Tracer,
) {

    private val log = KotlinLogging.logger { }

    fun subscribe(companyId: String): Flux<Any> {

        val sink = Sinks.many().multicast().onBackpressureBuffer<Any>()

        // 저장소에 Sink를 저장합니다.
        emitterRepository.save(companyId, sink)

        // 첫 구독 시 이벤트 발생
//        sink.tryEmitNext("subscribe event, companyId : $companyId")

        val flux = sink.asFlux()
            .doOnCancel {
                log.info { "server sent event complete (cancelled): id=$companyId" }
                emitterRepository.deleteById(companyId)
            }
            .doOnError { error ->
                log.info { "server sent event error: id=$companyId, error=${error.message}" }
                emitterRepository.deleteById(companyId)
            }
            .doOnNext {
                log.info { "server sent event complete" }
            }

        emitterRepository.save(companyId, sink)

        return flux
    }

    fun broadcast(id: String, dto: MessageDto) {
        val sinks: Sinks.Many<Any> = emitterRepository.findById(id)
            ?: throw NotFoundEmitterException()

        sendToClient(sinks, id, dto)
    }

    fun findEmitterByCompanyId(id: String): Sinks.Many<Any> {
        return emitterRepository.findById(id) ?: throw NotFoundEmitterException()
    }

    fun sendToClient(sinks: Sinks.Many<Any>, companyId: String, data: MessageDto) {
        try {
            val dataObj = ServerSentEvent
                .builder(data)
                .id("[SSE] SEND MESSAGE")
                .build()

            sinks.tryEmitNext(dataObj)
            sinks.tryEmitComplete()

            emitterRepository.deleteById(companyId)
        } catch (ex: IOException) {
            emitterRepository.deleteById(companyId)
            log.error { "연결 오류 발생 : ${ex.message} " }
            throw NotFoundEmitterException()
        }
    }

}