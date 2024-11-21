package com.yb.domain.reactive

import com.yb.dto.request.CourseRequestDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("course")
data class Course(
    @Id
    val id: Long = 0,
    val year: Int = 0,
    val courseName: String = "",
    val professor: String = "",
    val capacityCount: Int = 0,
    val createdAt: LocalDateTime? = null
) {

    constructor(year: Int, courseName: String, professor: String, capacityCount: Int) : this(
        0,
        year,
        courseName,
        professor,
        capacityCount,
        LocalDateTime.now()
    )

    companion object {
        fun from(courseRequestDto: CourseRequestDto): com.yb.domain.reactive.Course {
            return Course(
                courseRequestDto.year,
                courseRequestDto.courseName,
                courseRequestDto.professor,
                courseRequestDto.capacityCount
            )
        }
    }

//    fun availableCourseApply(): Boolean {
//        if (capacityCount == null) {
//            return true
//        }
//        return totalQuantity > issuedQuantity
//    }
//
//    fun availableIssueDate(): Boolean {
//        val now = LocalDateTime.now()
//        return dateIssueStart.isBefore(now) && dateIssueEnd.isAfter(now)
//    }
//
//    fun isIssueComplete(): Boolean {
//        val now = LocalDateTime.now()
//        return dateIssueEnd.isBefore(now) || !availableIssueQuantity()
//    }
//
//    fun issue() {
//        if (!availableCourseApply()) {
//            throw CouponIssueException(
//                INVALID_COUPON_ISSUE_QUANTITY,
//                "발급 가능한 수량을 초과합니다. total : %s, issued: %s".formatted(totalQuantity, issuedQuantity)
//            )
//        }
//        if (!availableIssueDate()) {
//            throw CouponIssueException(
//                INVALID_COUPON_ISSUE_DATE,
//                "발급 가능한 일자가 아닙니다. request : %s, issueStart: %s, issueEnd: %s".formatted(
//                    LocalDateTime.now(), dateIssueStart, dateIssueEnd
//                )
//            )
//        }
//        issuedQuantity++
//    }
}