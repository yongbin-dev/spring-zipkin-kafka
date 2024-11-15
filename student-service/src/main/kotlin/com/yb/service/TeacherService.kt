package com.yb.service

import com.yb.domain.jpa.Teacher
import com.yb.dto.request.TeacherRequestDto
import com.yb.dto.response.TeacherResponseDto
import com.yb.repository.jpa.TeacherRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TeacherService(
    val teacherRepository: TeacherRepository
) {

    fun findByAllTeacher(): List<TeacherResponseDto> {
        return teacherRepository.findAll().stream().map { t -> TeacherResponseDto.from(t) }.toList()
    }

    fun delete() {

    }

    @Transactional
    fun create(teacherRequestDto: TeacherRequestDto): TeacherResponseDto {
        val teacher: Teacher = Teacher.from(teacherRequestDto)
        teacherRepository.save(teacher)

        return TeacherResponseDto.from(teacher)
    }

}