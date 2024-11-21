package com.yb.component

import brave.Tracer
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.exception.NotFoundEmitterException
import com.yb.service.EmitterService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class KafkaMessageListenerComponent(
    private val emitterService: EmitterService,
    private val objectMapper: ObjectMapper,
    private val tracer: Tracer,

//    @Value("\${kafka.topic}")
//    val  topicName: String
) {

    private val log = LoggerFactory.getLogger(this.javaClass);

    @KafkaListener(topics = ["sugang-apply"], groupId = "sugang-group")
    fun listener(@Payload payload: String) {
        log.info("receive payload : $payload ")

        try {
//            val payloadMessageDto = objectMapper.readValue(payload, MessageDto::class.java)
//            val childSpan = tracer.nextSpan()
//                .name("[SSE] EMITTER BROADCAST")
//                .start()
//
//            try {
//                tracer.withSpanInScope(childSpan).use {
//                    emitterService.broadcast(payloadMessageDto.id, payloadMessageDto)
//                }
//            } finally {
//                childSpan.finish()
//            }

        } catch (e: JsonProcessingException) {
            log.error("JsonProcessingException : ${e.message}")
        } catch (e: NotFoundEmitterException) {
            log.info("NotFoundEmitterException")
        }
    }
}