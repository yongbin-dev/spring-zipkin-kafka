package com.yb.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.yb.domain.jpa.Student
import com.yb.domain.reactive.Course
import com.yb.domain.reactive.CourseApply
import com.yb.dto.request.CourseApplyRequestDto
import com.yb.exception.CourseException
import com.yb.exception.StudentException
import com.yb.exception.errorCode.CourseErrorCode
import com.yb.exception.errorCode.StudentErrorCode
import com.yb.repository.jpa.StudentRepository
import com.yb.repository.reactive.CourseApplyReactiveCustomRepository
import com.yb.repository.reactive.CourseApplyReactiveRepository
import com.yb.repository.reactive.CourseReactiveRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import io.micrometer.tracing.Tracer
import kotlinx.coroutines.flow.toList
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CourseApplyService(
    private val courseApplyReactiveRepository: CourseApplyReactiveRepository,
    private val courseReactiveRepository: CourseReactiveRepository,
    private val courseApplyReactiveCustomRepository: CourseApplyReactiveCustomRepository,
    private val studentRepository: StudentRepository,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val tracer: Tracer,
) {

    private val log = KotlinLogging.logger { }

    @Transactional("r2dbcTransactionManager")
    suspend fun apply(courseApplyDto: CourseApplyRequestDto): CourseApply {
        val courseId = courseApplyDto.courseId
        val studentId = courseApplyDto.studentId

        val course: Course = courseReactiveRepository.findById(courseId)
            ?: throw CourseException(CourseErrorCode.COURSE_NOT_EXIST)

        val student: Student = studentRepository.findById(studentId)
            .orElseThrow { StudentException(StudentErrorCode.NOT_FOUND_STUDENT) }

        checkAvailableCourseApply(course, student)

        val courseApply = CourseApply(courseId, studentId)
        val savedCourseApply = courseApplyReactiveRepository.save(courseApply)

        val savedCourseApplyJson = objectMapper.writeValueAsString(savedCourseApply)
//
//        val traceDto = TraceDto(tracer.);
//        val messageDto = MessageDto(savedCourseApplyJson , )
//        kafkaTemplate.send(
//            "sugang-apply",
//            savedCourseApply.studentId.toString(),
//            savedCourseApplyJson
//        )


        return savedCourseApply
    }

    private suspend fun checkAvailableCourseApply(course: Course, student: Student) {
        checkLimitApply(course)
        checkDuplicateApply(course.id, student.id);
    }

    private suspend fun checkLimitApply(course: Course) {
        val courseApplyCount: Int = courseApplyReactiveCustomRepository.countByCourseId(course.id)
        if (courseApplyCount >= course.capacityCount) {
            throw CourseException(CourseErrorCode.INVALID_COURSE_QUANTITY)
        }
    }

    private suspend fun checkDuplicateApply(courseId: Long, studentId: Long) {
        courseApplyReactiveCustomRepository.findByCourseIdAndStudentId(courseId, studentId)
            ?.let { throw CourseException(CourseErrorCode.DUPLICATED_COURSE_ISSUE) }
    }

    suspend fun getAllApply(courseId: Long): List<CourseApply> {
        return courseApplyReactiveCustomRepository.findByCourseId(courseId).toList();
    }

    suspend fun getApplyByCourseIdAndStudentId(courseId: Long, studentId: Long): CourseApply {
        return courseApplyReactiveRepository.findByCourseIdAndStudentId(courseId, studentId)
            ?: throw NotFoundException();
    }

    suspend fun getApplyByIdAndCourseId(
        courseApplyId: Long,
        courseId: Long,
    ): CourseApply {
        return courseApplyReactiveRepository.findByIdAndCourseId(courseApplyId, courseId)
            ?: throw CourseException(CourseErrorCode.COURSE_NOT_EXIST);
    }
}