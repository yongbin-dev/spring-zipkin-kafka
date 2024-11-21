package com.yb.controller

import com.yb.domain.reactive.CourseApply
import com.yb.dto.request.CourseApplyRequestDto
import com.yb.service.CourseApplyService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/course-apply")
class CourseApplyController(
    private val courseApplyService: CourseApplyService
) {

    private val log = KotlinLogging.logger { }

    @PostMapping("")
    suspend fun apply(@RequestBody courseApplyRequestDto: CourseApplyRequestDto): CourseApply {
        log.info { courseApplyRequestDto.toString() }
        return courseApplyService.apply(courseApplyRequestDto);
    }

    @GetMapping("/{courseId}")
    suspend fun get(@PathVariable courseId: Long): List<CourseApply> {
        return courseApplyService.getAllApply(courseId)
    }

    @GetMapping("/search")
    suspend fun get(
        @RequestParam courseId: Long,
        @RequestParam studentId: Long?,
        @RequestParam courseApplyId: Long?
    ): CourseApply {
        return when {
            studentId != null ->
                courseApplyService.getApplyByCourseIdAndStudentId(
                    courseId, studentId
                )

            courseApplyId != null ->
                courseApplyService.getApplyByIdAndCourseId(
                    courseApplyId, courseId
                )

            else -> throw IllegalArgumentException("Invalid request parameters")
        }
    }


}