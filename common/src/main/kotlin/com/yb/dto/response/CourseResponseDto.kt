package com.yb.dto.response

import java.time.LocalDateTime


data class CourseResponseDto(
    val year: Int,
    val courseName: String,
    val professor: String,
    val capacityCount: Int,
    val createdAt: LocalDateTime
) {

    companion object {
        fun from(course: com.yb.domain.reactive.Course): CourseResponseDto {
            return CourseResponseDto(
                course.year,
                course.courseName,
                course.professor,
                course.capacityCount,
                course.createdAt ?: LocalDateTime.now()
            )
        }
    }
}
