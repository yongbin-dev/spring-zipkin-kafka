package com.yb.exception

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Configuration
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@Configuration
@RestControllerAdvice
class GlobalErrorHandling {

    private val log = KotlinLogging.logger { };

//    @ExceptionHandler(value = [WebClientException::class])
//    fun webClientRequestExceptionHandler() {
//        log.error("webClientRequestException 에러남")
//    }

    @ExceptionHandler(value = [NotFoundEmitterException::class])
    fun notFoundEmitterExceptionHandler() {
        log.error { "NotFoundEmitterException" }
    }

    @ExceptionHandler(value = [NotFoundException::class])
    fun notFoundExceptionHandler() {
        log.error { "NotFoundException" }
    }

}
