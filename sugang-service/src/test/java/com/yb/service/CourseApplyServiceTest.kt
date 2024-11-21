//package com.yb.service
//
//import com.yb.domain.jpa.Student
//import com.yb.domain.reactive.Course
//import com.yb.domain.reactive.CourseApply
//import com.yb.dto.request.CourseApplyRequestDto
//import com.yb.exception.CourseException
//import com.yb.exception.StudentException
//import com.yb.repository.jpa.StudentRepository
//import com.yb.repository.reactive.CourseApplyReactiveCustomRepository
//import com.yb.repository.reactive.CourseApplyReactiveRepository
//import com.yb.repository.reactive.CourseReactiveRepository
//import io.mockk.coEvery
//import io.mockk.mockk
//import org.assertj.core.api.Assertions.assertThat
//import org.assertj.core.api.Assertions.assertThatThrownBy
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.mock.mockito.MockBean
//
//@SpringBootTest
//class CourseApplyServiceTest {
//
//    @MockBean
//    lateinit var courseApplyReactiveRepository: CourseApplyReactiveRepository
//
//    @MockBean
//    lateinit var courseReactiveRepository: CourseReactiveRepository
//
//    @MockBean
//    lateinit var courseApplyReactiveCustomRepository: CourseApplyReactiveCustomRepository
//
//    @MockBean
//    lateinit var studentRepository: StudentRepository
//
//    lateinit var courseApplyService: CourseApplyService
//
//    @BeforeEach
//    fun setup() {
//        courseApplyService = CourseApplyService(
//            courseApplyReactiveRepository,
//            courseReactiveRepository,
//            courseApplyReactiveCustomRepository,
//            studentRepository
//        )
//    }
//
//    @Test
//    fun `should apply course success`() {
//        val courseId = 1L
//        val studentId = 2L
//        val course = Course(1, "math", 40, length = 1, students = null)
//        val courseApplyDto = CourseApplyRequestDto(studentId, courseId)
//        val student = Student(
//            id = studentId,
//            studentNo = "1234",
//            major = "computer"
//        )
//
//        coEvery { courseReactiveRepository.findById(courseId) } returns course
//        coEvery { studentRepository.findById(studentId) }.returns(java.util.Optional.of(student))
//        coEvery { courseApplyReactiveCustomRepository.countByCourseId(courseId) }.returns(20)
//        coEvery {
//            courseApplyReactiveRepository.save(
//                CourseApply(
//                    courseId,
//                    studentId
//                )
//            )
//        } returns CourseApply(courseId, studentId)
//
//        val appliedCourse = courseApplyService.apply(courseApplyDto)
//
//        assertThat(appliedCourse).matches { it.courseId == courseId && it.studentId == studentId }.isNotNull
//
//    }
//
//    @Test
//    fun `should not apply course when course not exist`() {
//        val courseId = 1L
//        val studentId = 2L
//        val courseApplyDto = CourseApplyRequestDto(studentId, courseId)
//        val student = Student(
//            id = studentId,
//            studentNo = "1234",
//            major = "computer"
//        )
//
//        coEvery { courseReactiveRepository.findById(courseId) } returns null
//        coEvery { studentRepository.findById(studentId) }.returns(java.util.Optional.of(student))
//
//        assertThatThrownBy { courseApplyService.apply(courseApplyDto) }
//            .isInstanceOf(CourseException::class.java)
//    }
//
//    @Test
//    fun `should not apply course when student not exist`() {
//        val courseId = 1L
//        val studentId = 2L
//        val course = Course(1, "math", 40, length = 1, students = null)
//        val courseApplyDto = CourseApplyRequestDto(studentId, courseId)
//
//
//        coEvery { courseReactiveRepository.findById(courseId) } returns course
//        coEvery { studentRepository.findById(studentId) }.returns(java.util.Optional.empty())
//
//        assertThatThrownBy { courseApplyService.apply(courseApplyDto) }
//            .isInstanceOf(StudentException::class.java)
//    }
//
//    // Further test implementation for rest of check functions and rules, for example:
//    // Course capacity exceeding tests
//    // Duplicated course apply tests
//    // and others based on requirements
//}