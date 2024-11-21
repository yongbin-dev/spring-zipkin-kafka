package com.yb.domain.reactive

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


@Table(name = "course_apply")
data class CourseApply(
    @Id
    val id: Long = 0,
    val courseId: Long,
    val studentId: Long,
    val createdAt: LocalDateTime
) {

    constructor(courseId: Long, studentId: Long) : this(
        0,
        courseId,
        studentId,
        LocalDateTime.now()
    )

}