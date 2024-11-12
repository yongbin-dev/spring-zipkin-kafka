package com.yb.client

import com.yb.dto.response.StudentResponseDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "studentClient", url = "http://localhost:8089")
interface StudentClient {

    @GetMapping(value = ["/student"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun getStudent(): ResponseEntity<List<StudentResponseDto>>

}