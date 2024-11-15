package com.yb.repository.jpa

import com.yb.domain.jpa.Course
import org.springframework.data.jpa.repository.JpaRepository

interface CourseRepository : JpaRepository<Course, Long>