package com.yb.domain.jpa

import com.yb.dto.request.StudentRequestDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int,

    @Column(name = "created_at")
    val createdAt: LocalDateTime
) {


    constructor(name: String, age: Int) : this(0, name, age, LocalDateTime.now())

    companion object {
        fun from(studentRequestDto: StudentRequestDto): Student {
            return Student(
                studentRequestDto.name,
                studentRequestDto.age,
            )
        }
    }
}