package com.yb.exception.errorCode


enum class CourseErrorCode(val message: String) {
    INVALID_COURSE_QUANTITY("수강 신청 발급 수량이 유효 하지 않습니다."),

    //    INVALID_COUPON_ISSUE_DATE("수강 신청 발급 기간이 유효 하지 않습니다."),
    COURSE_NOT_EXIST("존재 하지 않는 수강 신청 입니다."),
    DUPLICATED_COURSE_ISSUE("이미 등록된 수강 신청 입니다."),
    FAIL_COURSE_ISSUE_REQUEST("수강 신청 요청에 실패 했습니다.")
}


