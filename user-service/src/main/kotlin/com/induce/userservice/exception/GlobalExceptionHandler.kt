package com.induce.userservice.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleBaseException(
        ex: BaseException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        val status = ex.errorCode.status

        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            errorCode = ex.errorCode,
            message = ex.message ?: "",
            path = request.requestURI
        )

        return ResponseEntity(error, status)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        val status = ErrorCode.INTERNAL_ERROR.status

        val error = ErrorResponse(
            timestamp = LocalDateTime.now(),
            status = status.value(),
            error = status.reasonPhrase,
            errorCode = ErrorCode.INTERNAL_ERROR,
            message = ex.message ?: "Unexpected error",
            path = request.requestURI
        )

        return ResponseEntity(error, status)
    }
}
