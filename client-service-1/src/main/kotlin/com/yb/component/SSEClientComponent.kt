package com.yb.component

import brave.Tracer
import brave.Tracing
import brave.propagation.TraceContext
import com.yb.client.StudentClient
import com.yb.dto.response.StudentResponseDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.math.BigInteger


@Component
class SSEClientComponent(
    @Autowired private val webClientBuilder: WebClient.Builder,
    private val restTemplate: RestTemplate,
    private val tracer: Tracer,
    private val tracing: Tracing,
    @Autowired private val studentClient: StudentClient
) {


    private val webClient: WebClient = webClientBuilder
        .baseUrl("http://localhost:8083")
        .filter(traceExchangeFilterFunction())
        .build()

    private val log = LoggerFactory.getLogger(this.javaClass)

    init {
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

                tracing.tracer().withSpanInScope(span).use { _ ->
                    try {

                        // 이벤트 처리 로직
                        val result: String = this.getStudent(response)
                        log.info("result : {} ", result)

                    } finally {
                        span.finish()
                    }
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
        val response: ResponseEntity<List<StudentResponseDto>> =
            restTemplate.getForEntity("http://localhost:8089/student")
//        val response: ResponseEntity<List<StudentResponseDto>> = studentClient.getStudent()
        val dto: List<StudentResponseDto> = response.body!!;
        tracing.tracer().currentSpan().tag("result-data", dto.toString())
        return dto.toString();
//        return response;
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

    private fun traceExchangeFilterFunction(): ExchangeFilterFunction {
        return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            val currentSpan = tracing.tracer().currentSpan()
            if (currentSpan != null) {
                val context = currentSpan.context()
                val request = ClientRequest.from(clientRequest)
                    .header("X-B3-TraceId", context.traceIdString())
                    .header("X-B3-SpanId", context.spanIdString())
                    .header("X-B3-ParentSpanId", context.parentIdString())
                    .build()
                Mono.just(request)
            } else {
                Mono.just(clientRequest)
            }
        }
    }

}