package com.yb.controller

import brave.Tracer
import com.yb.service.EmitterService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux


@RestController
class EmitterController(
    private val emitterService: EmitterService,
    private val tracer: Tracer
) {

    private val log = LoggerFactory.getLogger(this.javaClass)


    @GetMapping(value = ["/sse/{id}"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun subscribe(@PathVariable id: String): Flux<Any> {
        val flux: Flux<Any> = emitterService.subscribe(id) // 데이터
        return flux;

    }


}