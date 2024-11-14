package com.yb.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.config.AwsSnsConfig
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Tracer
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sns.SnsClient
import software.amazon.awssdk.services.sns.model.PublishRequest
import software.amazon.awssdk.services.sns.model.PublishResponse

@Service
class NotificationService(
    private val awsConfig: AwsSnsConfig,
    private val tracer: Tracer,
    private val objectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger {}

    fun sendMessage() {
        val messageData =
            mutableMapOf("test1" to "test1Value", "test2" to "test2Value", "test3" to "test3Value")

        messageData["traceId"] = tracer.currentSpan()!!.context().traceId();

        log.info { publishToSns(messageData) }
    }

    private fun publishToSns(messageData: Map<String, Any>): PublishResponse {
        val messageJson = objectMapper.writeValueAsString(messageData)


        val publishRequest: PublishRequest = PublishRequest.builder()
            .topicArn(awsConfig.getTopicArc())
            .subject("sns send message")
            .message(messageJson)
            .build()

        val snsClient: SnsClient = awsConfig.getSnsClient();
        return snsClient.publish(publishRequest)
    }
}