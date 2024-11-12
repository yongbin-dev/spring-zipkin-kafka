package com.yb.service

import com.yb.domain.Student
import com.yb.domain.repository.StudentRepository
import com.yb.dto.request.StudentRequestDto
import com.yb.dto.response.StudentResponseDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StudentService(
    @Autowired val studentRepository: StudentRepository
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