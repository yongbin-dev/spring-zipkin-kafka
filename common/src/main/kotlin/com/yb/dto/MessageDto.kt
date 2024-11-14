package com.yb.dto


data class MessageDto(
    val id: String,
    val data: List<Map<String, Any>>,
    val traceDto: TraceDto
)