package com.yb.domain.reactive

import com.yb.dto.request.CourseRequestDto
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("course")
data class Course(
    @org.springframework.data.annotation.Id
    val id: Long? = null,
    val year: Int = 0,
    val courseName: String = "",
    val professor: String = "",
    val createdAt: LocalDateTime? = null
) {

    constructor(year: Int, courseName: String, professor: String) : this(
        null,
        year,
        courseName,
        professor,
        LocalDateTime.now()
    )

    companion object {
        fun from(courseRequestDto: CourseRequestDto): com.yb.domain.reactive.Course {
            return Course(
                courseRequestDto.year,
                courseRequestDto.courseName,
                courseRequestDto.professor
            )
        }
    }
}