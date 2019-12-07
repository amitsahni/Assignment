package com.assignment.data.bean

data class ErrorResponse(
    val message: String,
    val code: Int = -1
)