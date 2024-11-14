package com.yb.util

import brave.Tracer
import brave.propagation.TraceContext
import com.yb.dto.TraceDto
import java.math.BigInteger

class TraceUtil(
    private val tracer: Tracer
) {

    fun createTraceContext(traceDto: TraceDto): TraceContext {
        val contextBuilder: TraceContext.Builder = TraceContext.newBuilder();
        val traceId = traceDto.traceId

        if (traceId.length == 32) {
            val traceIdHigh = extractLongFromHex(traceId.substring(0, 16))
            val traceIdLow = extractLongFromHex(traceId.substring(16))

            contextBuilder.traceIdHigh(traceIdHigh).traceId(traceIdLow)
        } else {
            val traceIdLow = BigInteger(traceId, 16).toLong()
            contextBuilder.traceId(traceIdLow)
        }

        contextBuilder.spanId(tracer.nextSpan().context().spanId())

        return contextBuilder.build()
    }

    private fun extractLongFromHex(hexString: String): Long {
        return BigInteger(hexString, 16).toLong()
    }
}