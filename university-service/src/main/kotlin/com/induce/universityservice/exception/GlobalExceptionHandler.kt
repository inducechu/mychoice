package com.induce.universityservice.exception

import com.induce.universityservice.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(EntityAlreadyExistsException::class)
    fun handleAlreadyExists(
        ex: EntityAlreadyExistsException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message,
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Data Integrity Violation",
            message = "Запись с такими уникальными данными уже существует в системе",
            path = request.requestURI
        )
        return ResponseEntity(error, HttpStatus.CONFLICT)
    }
}
