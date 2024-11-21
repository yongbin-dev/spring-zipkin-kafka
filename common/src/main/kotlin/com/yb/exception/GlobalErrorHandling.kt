package com.yb.exception

import com.yb.dto.ResponseDto
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalErrorHandling {

    private val log = KotlinLogging.logger { };

    @ExceptionHandler(
        value = [
            NotFoundEntityException::class,
            NotFoundException::class,
            NotFoundEmitterException::class
        ]
    )
    fun handleException(e: RuntimeException): ResponseEntity<ResponseDto> {
        log.error { e.message }
        val responseDto = ResponseDto(true, null, e.message ?: "")
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(value = [CourseException::class])
    fun handleCourseException(e: CourseException): ResponseEntity<ResponseDto> {
        val responseDto = ResponseDto(true, null, e.courseErrorCode.message)
        return ResponseEntity.ok(responseDto);
    }

    @ExceptionHandler(value = [StudentException::class])
    fun handleStudentException(e: StudentException): ResponseEntity<ResponseDto> {
        val responseDto = ResponseDto(true, null, e.studentErrorCode.message)
        return ResponseEntity.ok(responseDto);
    }

}
