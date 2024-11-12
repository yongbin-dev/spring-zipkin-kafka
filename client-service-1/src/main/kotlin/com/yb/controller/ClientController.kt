package com.yb.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientController {

    @GetMapping("/test")
    fun test(): String {
        return "123123"
    }
}