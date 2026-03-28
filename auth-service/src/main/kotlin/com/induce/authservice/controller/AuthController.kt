package com.induce.authservice.controller

import com.induce.authservice.dto.LoginRequest
import com.induce.authservice.dto.RegisterRequest
import com.induce.authservice.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<Map<String, String>> {
        authService.register(request)
        return ResponseEntity.ok(mapOf("message" to "User registered successfully"))
    }

    @Operation(summary = "Вход в систему", description = "Возвращает JWT токен при успешной проверке email и пароля")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<Map<String, String>> {
        val token = authService.login(request)
        return ResponseEntity.ok(mapOf("token" to token))
    }
}
