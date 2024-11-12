package com.yb.config

import brave.Tracer
import brave.Tracing
import io.micrometer.observation.Observation.Context
import io.micrometer.observation.ObservationHandler
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ObservationConfig(
    private val tracer: Tracer,
    private val tracing: Tracing

) {

    private val log = LoggerFactory.getLogger(this.javaClass)!!

    @Bean
    fun observationTextPublisher(): ObservationHandler<Context> {
        return object : ObservationHandler<Context> {
            override fun supportsContext(context: Context): Boolean {
                return true
            }

            override fun onStart(context: Context) {
                val currentSpan = tracer.currentSpan()
                val traceId = currentSpan?.context()?.traceIdString()
                val spanId = currentSpan?.context()?.spanIdString()


                if (traceId != null) {
                    currentSpan.tag("contextName", context.name)
                    log.info("Before running the observation for context: ${context.name}")
                    log.info("TraceId: $traceId")
                    log.info("SpanId: $spanId")
                }
            }

            override fun onStop(context: Context) {
                val currentSpan = tracer.currentSpan()
            }
        }
    }
}