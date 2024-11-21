package com.yb.service

import com.yb.domain.jpa.Student
import com.yb.dto.request.StudentRequestDto
import com.yb.dto.response.StudentResponseDto
import com.yb.exception.StudentException
import com.yb.exception.errorCode.StudentErrorCode
import com.yb.repository.jpa.StudentRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {

    fun removeStudent(studentId: Long) {
        val student = findStudent(studentId)
        studentRepository.delete(student)
    }

    fun createStudent(studentRequestDto: StudentRequestDto): StudentResponseDto {
        val student = Student.from(studentRequestDto)
        studentRepository.save(student)
        return StudentResponseDto.from(student)
    }

    fun findStudentById(studentId: Long): StudentResponseDto {
        val student = this.findStudent(studentId)
        return StudentResponseDto.from(student)
    }

    private fun findStudent(studentId: Long): Student {
        val student = studentRepository.findById(studentId)
            .orElseThrow() { throw StudentException(StudentErrorCode.NOT_FOUND_STUDENT) }
        return student;
    }
}