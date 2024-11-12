package com.yb.controller

import brave.Tracing
import com.yb.service.EmitterService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import reactor.core.publisher.Flux
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@RestController
class EmitterController(
    private val emitterService: EmitterService,
    @Autowired private val tracing: Tracing
) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    @GetMapping(value = ["/sse/{id}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribe(@PathVariable id: String): Flux<Any> {
        // TracingExecutorService로 ExecutorService 감싸기
//        emit("data: Current time is ")
        val flux: Flux<Any> = emitterService.subscribe(id) // 데이터
        return flux;
//        emit(flux)
//        return flux
    }

    @GetMapping(value = ["/stream-sse"])
    fun test(): SseEmitter {

        val emitter = SseEmitter()
        val executor: ExecutorService =
            tracing.currentTraceContext().executorService(Executors.newSingleThreadExecutor())

        executor.execute {
            try {
                for (i in 0..9) {
                    emitter.send("SSE event - $i")
                    Thread.sleep(1000) // 1초마다 이벤트 전송
                }
                emitter.complete()
            } catch (e: IOException) {
                emitter.completeWithError(e)
            } catch (e: InterruptedException) {
                emitter.completeWithError(e)
            }
        }

        return emitter
    }

}