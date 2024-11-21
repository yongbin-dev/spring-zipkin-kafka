package com.yb.dto.response

import com.yb.domain.jpa.Student
import java.time.LocalDateTime


data class StudentResponseDto(
    var id: Int?,
    var name: String,
    var age: Int,
    var createdAt: LocalDateTime
) {

    companion object {
        fun from(student: Student): StudentResponseDto {
            return StudentResponseDto(
                null, student.name, student.age, student.createdAt
            )
        }
    }
}
