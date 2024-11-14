package com.yb.dto

data class TraceDto(
    val traceId: String,
    val spanId: String,
) {
    constructor(traceId: String) : this(traceId, "")
}