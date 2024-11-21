package com.yb.domain.jpa

import com.yb.dto.request.CourseRequestDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "course")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val year: Int = 0,
    @Column(name = "course_name")
    val courseName: String = "",
    val professor: String = "",

    @Column(name = "capacity_count")
    val capacityCount: Int = 0,
    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {

    constructor(year: Int, courseName: String, professor: String, capacityCount: Int) : this(
        0,
        year,
        courseName,
        professor,
        capacityCount,
        LocalDateTime.now()
    )

    companion object {
        fun from(courseRequestDto: CourseRequestDto): Course {
            return Course(
                courseRequestDto.year,
                courseRequestDto.courseName,
                courseRequestDto.professor,
                courseRequestDto.capacityCount
            )
        }
    }
}