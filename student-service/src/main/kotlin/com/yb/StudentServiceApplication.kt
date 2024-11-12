package com.yb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StudentServiceApplication

fun main(args: Array<String>) {
    runApplication<StudentServiceApplication>(*args)
}
