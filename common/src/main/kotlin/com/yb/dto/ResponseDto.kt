package com.yb.dto

data class ResponseDto(
    var status: Boolean = false,
    var data: Any?,
    var errorMessage: String
) {

}