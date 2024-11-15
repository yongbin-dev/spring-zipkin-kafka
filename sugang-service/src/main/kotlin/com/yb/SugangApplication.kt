package com.yb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication(scanBasePackages = ["com.yb"])
class SugangApplication

fun main(args: Array<String>) {
    runApplication<SugangApplication>(*args)
}
