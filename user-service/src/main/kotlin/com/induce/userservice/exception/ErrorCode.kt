package com.induce.userservice.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus) {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR)
}
