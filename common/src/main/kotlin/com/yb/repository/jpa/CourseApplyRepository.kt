package com.yb.repository.jpa

import com.yb.domain.jpa.CourseApply
import org.springframework.data.jpa.repository.JpaRepository

interface CourseApplyRepository : JpaRepository<CourseApply, Long> {
}