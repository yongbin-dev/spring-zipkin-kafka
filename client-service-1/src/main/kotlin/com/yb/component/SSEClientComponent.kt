package com.yb.component

import brave.Tracing
import brave.propagation.TraceContext
import com.yb.client.StudentClient
import com.yb.dto.response.StudentResponseDto
import org.slf4j.LoggerFactory
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigInteger


@Component
class SSEClientComponent(
    private var webClient: WebClient,
    private val tracing: Tracing,
    private val studentClient: StudentClient
) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    init {
        webClient = WebClient.builder()
            .baseUrl("http://localhost:8083")
            .build()

        sseClientConnect()
    }

    private fun sseClientConnect() {
        val sseClient = webClient.get()
            .uri("/sse/client1")
            .retrieve()
            .bodyToFlux(object : ParameterizedTypeReference<ServerSentEvent<String>>() {})
//            .retryWhen(Retry.backoff(3, java.time.Duration.ofSeconds(5)))
            .doOnSubscribe {
                log.info(" Attempting to connect to SSE stream... ")
            }
            .doOnNext { response ->
                val data = response.data()!!
                val traceId = extractTraceId(data)
                val spanId = extractSpanId(data)

                val traceContext: TraceContext = createTraceContext(traceId, spanId)

                val span = tracing.tracer().newChild(traceContext)
                    .name("process-sse-event")
                    .start()

                log.info("traceId : {} ", traceId)
                try {
                    tracing.tracer().withSpanInScope(span).use {
                        this.getStudent(response)
                    }
                } finally {
                    span.finish()
                }
            }.doOnComplete {
                log.info(" Complete connected , retry SSE")
                sseClientConnect()
            }.doOnError {
                log.error(" Error connected , retry SSE")
                Thread.sleep(5000)
                sseClientConnect()
            }

        sseClient.subscribe()
    }


    fun getStudent(sse: ServerSentEvent<String>): String {
//        val response: ResponseEntity<List<StudentResponseDto>> =
//            restTemplate.getForEntity("http://localhost:8089/student")

        val response = studentClient.getStudent();
        val dto: List<StudentResponseDto> = response.body!!;
        tracing.tracer().currentSpan().tag("result-data", dto.toString())
        return dto.toString();
    }


    private fun extractTraceId(event: String): String {
        return event.split("TraceId: ")[1].split(",")[0].trim()
    }

    private fun extractSpanId(event: String): String {
        return event.split("SpanId: ")[1].split(",")[0].trim()
    }


    private fun createTraceContext(
        traceId: String,
        spanId: String
    ): TraceContext {
        if (traceId.length != 32 || !traceId.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) {
            throw IllegalArgumentException("Invalid traceId format")
        }
        if (spanId.length != 16 || !spanId.all { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' }) {
            throw IllegalArgumentException("Invalid spanId format")
        }

        val traceIdHigh = BigInteger(traceId.substring(0, 16), 16).toLong()
        val traceIdLow = BigInteger(traceId.substring(16), 16).toLong()
        val spanIdLong = BigInteger(spanId, 16).toLong()

        return TraceContext.newBuilder()
            .traceIdHigh(traceIdHigh)
            .traceId(traceIdLow)
            .spanId(spanIdLong)
            .build()
    }

//    private fun traceExchangeFilterFunction(): ExchangeFilterFunction {
//        return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
//            val currentSpan = tracing.tracer().currentSpan()
//            if (currentSpan != null) {
//                val context = currentSpan.context()
//                val request = ClientRequest.from(clientRequest)
//                    .header("X-B3-TraceId", context.traceIdString())
//                    .header("X-B3-SpanId", context.spanIdString())
//                    .header("X-B3-ParentSpanId", context.parentIdString())
//                    .build()
//                Mono.just(request)
//            } else {
//                Mono.just(clientRequest)
//            }
//        }
//    }

}