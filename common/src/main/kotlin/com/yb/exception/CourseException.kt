package com.yb.exception

import com.yb.exception.errorCode.CourseErrorCode

class CourseException(
    val courseErrorCode: CourseErrorCode,
) : RuntimeException() {

}