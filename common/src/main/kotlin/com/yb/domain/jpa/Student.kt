package com.yb.domain.jpa

import com.yb.dto.request.StudentRequestDto
import jakarta.persistence.*

@Entity
data class Student(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false)
    val teacherId: Long,
) {


    constructor(name: String, age: Int, teacher: Long) : this(null, name, age, teacher)

    companion object {
        fun from(studentRequestDto: StudentRequestDto): Student {
            return Student(
                studentRequestDto.name,
                studentRequestDto.age,
                studentRequestDto.teacher
            )
        }
    }
}