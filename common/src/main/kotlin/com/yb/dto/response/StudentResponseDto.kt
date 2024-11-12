package com.yb.dto.response

import com.yb.domain.Student


data class StudentResponseDto(
    var name: String,
    var age: Int,
    var teacherId: Long
) {

    companion object {
        fun from(student: Student): StudentResponseDto {
            return StudentResponseDto(
                student.name, student.age, student.teacherId
            )
        }
    }
}
