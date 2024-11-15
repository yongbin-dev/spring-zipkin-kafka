package com.yb.repository.reactive

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CourseReactiveRepository : CoroutineCrudRepository<com.yb.domain.reactive.Course, Long>