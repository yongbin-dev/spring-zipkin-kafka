package com.yb.service

import com.yb.domain.reactive.Course
import com.yb.dto.request.CourseRequestDto
import com.yb.dto.response.CourseResponseDto
import com.yb.dto.response.StudentResponseDto
import com.yb.repository.reactive.CourseReactiveRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.core.ParameterizedTypeReference
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class CourseService(
    private val courseReactiveRepository: CourseReactiveRepository
) {

    private val studentWebClient = WebClient.create("http://localhost:8089")
    private val log = KotlinLogging.logger { }

    suspend fun getAll(): List<CourseResponseDto> {
        return courseReactiveRepository.findAll()
            .map { CourseResponseDto.from(it) }
            .toList()
    }

    suspend fun getCourseById(courseId: Long): CourseResponseDto {
        val student = studentWebClient.get()
            .uri("/student")
            .retrieve()
            .bodyToMono(object :
                ParameterizedTypeReference<List<StudentResponseDto>>() {})  // List로 변환
            .block()

        log.info { student }

        val course = courseReactiveRepository.findById(courseId) ?: throw NotFoundException()
        return CourseResponseDto.from(course);
    }


    suspend fun saveCourse(courseRequestDto: CourseRequestDto): CourseResponseDto {
        return CourseResponseDto.from(
            courseReactiveRepository.save(Course.from(courseRequestDto))
        );
    }


}