package com.yb.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.dto.MessageDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KafkaTestController(
    @Autowired val kafkaTemplate: KafkaTemplate<String, String>,
    @Autowired val objectMapper: ObjectMapper
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
        val dto = MessageDto("client1", lst)

        val value = objectMapper.writeValueAsString(dto)
        kafkaTemplate.send("zipkin-test", value)
        return "OK"
    }
}

