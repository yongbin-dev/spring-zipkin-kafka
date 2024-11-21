package com.yb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients


@SpringBootApplication(scanBasePackages = ["com.yb"])
@EnableFeignClients
class SugangApplication

fun main(args: Array<String>) {
    runApplication<SugangApplication>(*args)
}
