package com.induce.authservice.dto

import java.time.LocalDateTime

data class ErrorResponse(
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int
)
