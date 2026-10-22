package com.induce.userservice.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UpdateProfileRequest(
    @field:NotBlank(message = "First name cannot be blank")
    val firstName: String,

    @field:NotBlank(message = "Last name cannot be blank")
    val lastName: String,

    @field:Min(value = 14, message = "Age must be at least 14")
    @field:Max(value = 100, message = "Age must be at most 100")
    val age: Int,
)
