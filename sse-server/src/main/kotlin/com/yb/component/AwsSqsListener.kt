package com.yb.component

import brave.Tracer
import brave.propagation.TraceContextOrSamplingFlags
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.client.StudentClient
import com.yb.dto.TraceDto
import com.yb.dto.response.StudentResponseDto
import com.yb.util.TraceUtil
import io.awspring.cloud.sqs.annotation.SqsListener
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class AwsSqsListener(
    private val studentClient: StudentClient,
    private val tracer: Tracer,
    private val objectMapper: ObjectMapper
) {

    private val log = KotlinLogging.logger {}

    @SqsListener(
        value = ["MyQueue-2"],
        factory = "customAwsSqsFactory"
    )
    fun receiveMessage(message: String) {
        val messageNode: JsonNode = objectMapper.readTree(message)
        val messageContent = messageNode.get("Message").asText()
        val messageContentNode = objectMapper.readTree(messageContent);

        val traceId = extractedTraceId(messageContentNode)
        val traceDto = TraceDto(traceId, "");
        val traceContext = TraceUtil(tracer).createTraceContext(traceDto)

        val span =
            tracer.nextSpan(TraceContextOrSamplingFlags.create(traceContext))
                .name("[SSE] AWS-SQS")
                .start()

        try {
            tracer.withSpanInScope(span).use {
                val response: ResponseEntity<List<StudentResponseDto>> = studentClient.getStudent();
                log.info { response.body }
            }
        } finally {
            span.finish()
        }

    }

    private fun extractedTraceId(messageContent: JsonNode): String {
        return messageContent.get("traceId").asText()
    }
}