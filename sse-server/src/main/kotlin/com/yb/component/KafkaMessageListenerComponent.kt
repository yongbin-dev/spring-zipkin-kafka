package com.yb.component

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.dto.MessageDto
import com.yb.service.EmitterService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class KafkaMessageListenerComponent(
    private val emitterService: EmitterService,
    private val objectMapper: ObjectMapper,
) {

    private val log = KotlinLogging.logger {}

    //    @Traced
    @KafkaListener(topics = ["zipkin-test"], groupId = "zipkin-group")
    fun listener(@Payload payload: String) {
        log.info { "receive payload : $payload " }

        try {
            val payloadMessageDto = objectMapper.readValue(payload, MessageDto::class.java)
            emitterService.broadcast(payloadMessageDto.id, payloadMessageDto)
        } catch (e: JsonProcessingException) {
            log.error { "JsonProcessingException : ${e.message}" }
        } catch (e: Exception) {
            log.error { "Exception : ${e.message}" }
        }
    }
}