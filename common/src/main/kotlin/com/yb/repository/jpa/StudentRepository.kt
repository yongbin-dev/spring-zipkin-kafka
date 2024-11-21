package com.yb.repository.jpa

import com.yb.domain.jpa.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional

@Transactional("transactionManager")
interface StudentRepository : JpaRepository<Student, Long>