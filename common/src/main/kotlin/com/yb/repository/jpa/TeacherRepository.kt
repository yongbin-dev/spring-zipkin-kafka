package com.yb.repository.jpa

import com.yb.domain.jpa.Teacher
import org.springframework.data.jpa.repository.JpaRepository

interface TeacherRepository : JpaRepository<Teacher, Long>