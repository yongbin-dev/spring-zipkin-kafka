package com.yb.exception

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Configuration
@RestControllerAdvice
class GlobalErrorHandling {

    private val log = LoggerFactory.getLogger(GlobalErrorHandling::class.java);

//    @ExceptionHandler(value = [WebClientException::class])
//    fun webClientRequestExceptionHandler() {
//        log.error("webClientRequestException 에러남")
//    }

    @ExceptionHandler(value = [NotFoundEmitterException::class])
    fun notFoundEmitterExceptionHandler() {
        log.error("NotFoundEmitterException")
    }
}
