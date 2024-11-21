package com.yb.repository.reactive

import com.yb.domain.reactive.CourseApply
import org.springframework.data.repository.kotlin.CoroutineCrudRepository


interface CourseApplyReactiveRepository : CoroutineCrudRepository<CourseApply, Long> {

    fun findByCourseIdAndStudentId(courseId: Long, studentId: Long): CourseApply?
    fun findByIdAndCourseId(id: Long, courseId: Long): CourseApply?
    fun findByCourseId(courseId: Long): CourseApply?
}