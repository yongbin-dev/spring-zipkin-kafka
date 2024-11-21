package com.yb.exception

import com.yb.exception.errorCode.StudentErrorCode

class StudentException(
    val studentErrorCode: StudentErrorCode,
) : RuntimeException() {

}