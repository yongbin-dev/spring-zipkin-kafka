package com.yb.repository.reactive

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional("r2dbcTransactionManager")
interface CourseReactiveRepository : CoroutineCrudRepository<com.yb.domain.reactive.Course, Long>