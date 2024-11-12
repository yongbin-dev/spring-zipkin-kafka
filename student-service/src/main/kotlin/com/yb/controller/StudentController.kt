package com.yb.controller

import com.yb.dto.request.StudentRequestDto
import com.yb.dto.response.StudentResponseDto
import com.yb.service.StudentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
class StudentController(
    @Autowired var studentService: StudentService,
) {

    private val logger: Logger = LoggerFactory.getLogger(StudentController::class.java)

    @GetMapping("/student")
    fun find(): ResponseEntity<List<StudentResponseDto>> {
        return ResponseEntity.ok(studentService.findByAllStudent())
    }

    @PostMapping("/student")
    fun create(@RequestBody studentDto: StudentRequestDto): ResponseEntity<StudentResponseDto> {
        return ResponseEntity.ok(studentService.create(studentDto))
    }

    @DeleteMapping("/student/{id}")
    fun delete(@PathVariable id: Long) {

    }

}