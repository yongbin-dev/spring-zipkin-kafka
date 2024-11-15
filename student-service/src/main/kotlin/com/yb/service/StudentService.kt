package com.yb.service

import com.yb.domain.jpa.Student
import com.yb.dto.request.StudentRequestDto
import com.yb.dto.response.StudentResponseDto
import com.yb.repository.jpa.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {
    fun findByAllStudent(): List<StudentResponseDto> {
        return studentRepository.findAll().map { s -> StudentResponseDto.from(s) }
            .toList()
    }

    fun delete() {

    }

    @Transactional
    fun create(studentRequestDto: StudentRequestDto): StudentResponseDto {
        val student: Student = Student.from(studentRequestDto)
        studentRepository.save(student)

        return StudentResponseDto.from(student)
    }
}