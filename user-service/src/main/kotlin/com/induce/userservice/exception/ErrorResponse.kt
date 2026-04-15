package com.induce.userservice.exception

import java.time.LocalDateTime

data class  ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val errorCode: ErrorCode,
    val message: String,
    val path: String
)
