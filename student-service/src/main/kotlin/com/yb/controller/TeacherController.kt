package com.yb.controller

import com.yb.dto.request.TeacherRequestDto
import com.yb.dto.response.TeacherResponseDto
import com.yb.service.TeacherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TeacherController(
    @Autowired val teacherService: TeacherService
) {

    @GetMapping("/teacher")
    fun find(): ResponseEntity<List<TeacherResponseDto>> {
        return ResponseEntity.ok().body(teacherService.findByAllTeacher())
    }

    @PostMapping("/teacher")
    fun create(@RequestBody teacherDto: TeacherRequestDto): ResponseEntity<TeacherResponseDto> {
        return ResponseEntity.ok().body(teacherService.create(teacherDto))
    }

    @DeleteMapping("/teacher/{id}")
    fun delete(@PathVariable id: Long) {

    }

}