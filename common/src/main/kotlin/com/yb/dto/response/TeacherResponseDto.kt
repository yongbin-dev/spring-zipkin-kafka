package com.yb.dto.response

import com.yb.domain.jpa.Teacher

data class TeacherResponseDto(
    var name: String,
    var age: Int,


    ) {
    companion object {
        fun from(teacher: Teacher): TeacherResponseDto {
            return TeacherResponseDto(
                teacher.name, teacher.age
            )
        }
    }
}
