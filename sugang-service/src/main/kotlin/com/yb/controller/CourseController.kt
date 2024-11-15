package com.yb.controller

import com.yb.dto.request.CourseRequestDto
import com.yb.dto.response.CourseResponseDto
import com.yb.service.CourseService
import org.springframework.web.bind.annotation.*


@RestController
class CourseController(
    val courseService: CourseService
) {

    @GetMapping("/course")
    suspend fun getAllCourse(): List<CourseResponseDto> {
        return courseService.getAll()
    }

    @GetMapping("/course/{courseId}")
    suspend fun getCourse(@PathVariable courseId: Long): CourseResponseDto {
        return courseService.getCourseB yId (courseId)
    }

    @PostMapping("/course")
    suspend fun saveCourse(@RequestBody courseRequestDto: CourseRequestDto): CourseResponseDto {
        return courseService.saveCourse(courseRequestDto)
    }
}