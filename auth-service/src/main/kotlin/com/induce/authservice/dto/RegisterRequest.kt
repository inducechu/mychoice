package com.induce.authservice.dto

import com.induce.authservice.model.Role
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email cannot be blank")
    val email: String,

    @field:NotBlank(message = "Password cannot be blank")
    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    val password: String,

    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,

    val role: Role? = Role.ABITURIENT
)
