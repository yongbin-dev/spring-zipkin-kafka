package com.yb.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "sseClient", url = "http://localhost:8083")
interface SseClient {

    @GetMapping(value = ["/produce"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun kafkaTest(): ResponseEntity<String>

}