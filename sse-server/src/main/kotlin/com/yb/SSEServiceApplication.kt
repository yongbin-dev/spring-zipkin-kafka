package com.yb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients


@SpringBootApplication
@EnableFeignClients
class SSEServiceApplication

fun main(args: Array<String>) {
    runApplication<SSEServiceApplication>(*args)
}