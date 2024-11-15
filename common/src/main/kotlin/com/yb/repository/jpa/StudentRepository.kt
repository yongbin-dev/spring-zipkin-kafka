package com.yb.repository.jpa

import com.yb.domain.jpa.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long>