package com.yb.controller

import brave.Tracer
import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.dto.MessageDto
import com.yb.dto.TraceDto
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KafkaTestController(
    val kafkaTemplate: KafkaTemplate<String, String>,
    val objectMapper: ObjectMapper,
    val tracer: Tracer

) {

    @GetMapping("/produce")
    fun producer(): String {
        val lst = mutableListOf<HashMap<String, Any>>()

        for (i: Int in 1..5) {
            val emptyMutableMap = hashMapOf<String, Any>()
            emptyMutableMap["OperationID"] = "value1 $i"
            emptyMutableMap["ExecutionStartDateTime"] = "value2 $i"
            emptyMutableMap["ProductionOrderID"] = "value3 $i"
            emptyMutableMap["ExecutionDate"] = "value4 $i"
            emptyMutableMap["ItemID"] = "value5 $i"
            lst.add(emptyMutableMap)
        }

        val traceId = tracer.currentSpan().context().traceIdString()
        val traceDto = TraceDto(traceId)

        val dto = MessageDto("client1", lst, traceDto)
        val value = objectMapper.writeValueAsString(dto)

        kafkaTemplate.send("zipkin-test", value)
        return "OK"
    }
}

