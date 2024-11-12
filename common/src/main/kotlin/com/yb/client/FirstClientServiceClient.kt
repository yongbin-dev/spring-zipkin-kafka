package com.yb.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "fistClient", url = "http://localhost:8080")
interface FirstClientServiceClient {

    @GetMapping(value = ["/test"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun get(): ResponseEntity<String>

}



