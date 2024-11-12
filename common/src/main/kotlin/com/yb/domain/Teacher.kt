package com.yb.domain

import com.yb.dto.request.TeacherRequestDto
import jakarta.persistence.*

@Entity
data class Teacher(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var age: Int,

    ) {
    constructor(name: String, age: Int) : this(null, name, age)

    companion object {
        fun from(teacherRequestDto: TeacherRequestDto): Teacher {
            return Teacher(
                teacherRequestDto.name,
                teacherRequestDto.age
            )
        }
    }

}