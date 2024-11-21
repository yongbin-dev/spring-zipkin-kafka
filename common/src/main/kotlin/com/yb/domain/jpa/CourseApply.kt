package com.yb.domain.jpa

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "course_apply")
data class CourseApply(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(name = "course_id")
    val courseId: Long,
    @Column(name = "student_id")
    val studentId: Long,
    @Column(name = "created_at")
    val createdAt: LocalDateTime
) {
    constructor(courseId: Long, studentId: Long) : this(
        0,
        courseId,
        studentId,
        LocalDateTime.now()
    )
}