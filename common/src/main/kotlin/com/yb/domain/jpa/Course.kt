package com.yb.domain.jpa

import com.yb.dto.request.CourseRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        fun from(courseRequestDto: CourseRequestDto): Course {
            return Course(
                courseRequestDto.year,
                courseRequestDto.courseName,
                courseRequestDto.professor
            )
        }
    }
}