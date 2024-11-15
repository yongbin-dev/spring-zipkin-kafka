package com.yb.component

import brave.Tracer
import brave.propagation.TraceContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.client.StudentClient
import com.yb.dto.MessageDto
import com.yb.dto.response.StudentResponseDto
import com.yb.util.TraceUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient


@Component
class SSEClientComponent(
    private val tracer: Tracer,
    private val studentClient: StudentClient,
    private val objectMapper: ObjectMapper,
) {

    private val log = KotlinLogging.logger { }
    private val webClient = WebClient.create("http://localhost:8083")

    init {
        sseClientConnect()
    }

    private fun sseClientConnect() {
        val sseClient = webClient.get()
            .uri("/sse/client1")
            .retrieve()
            .bodyToFlux(object : ParameterizedTypeReference<ServerSentEvent<String>>() {})
            .doOnSubscribe {
                log.info { " Attempting to connect to SSE stream... " }
            }
            .doOnNext { response ->
                val data: String = response.data()!!
                val messageDto = objectMapper.readValue(data, MessageDto::class.java)
                val traceContext: TraceContext =
                    TraceUtil(tracer).createTraceContext(messageDto.traceDto)

                val span = tracer.newChild(traceContext)
                    .name("[CLIENT] /sse/client1 response")
                    .start()

                try {
                    tracer.withSpanInScope(span).use {
                        this.getStudent(response)
                    }
                } finally {
                    span.finish()
                }
            }.doOnComplete {
                log.info { "Complete connected , retry SSE" }
                sseClientConnect()
            }.doOnError {
                log.error { "Error connected , retry SSE" }
                Thread.sleep(5000)
                sseClientConnect()
            }

        sseClient.subscribe()
    }


    fun getStudent(sse: ServerSentEvent<String>): String {
        val response = studentClient.getStudent();
        val dto: List<StudentResponseDto> = response.body!!;
        tracer.currentSpan().tag("result-data", dto.toString())
        return dto.toString();
    }


}