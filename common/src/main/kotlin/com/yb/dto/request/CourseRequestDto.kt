package com.yb.dto.request

data class CourseRequestDto(
    var professor: String,
    var year: Int,
    var courseName: String,
    var capacityCount: Int
)
