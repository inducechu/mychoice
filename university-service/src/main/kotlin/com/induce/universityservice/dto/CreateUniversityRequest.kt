package com.induce.universityservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class CreateUniversityRequest(

    @field:NotBlank(message = "University code must not be blank")
    @field:Size(max = 20, message = "University code must be <= 20 characters")
    @field:Pattern(
        regexp = "^[A-Z0-9_]+$",
        message = "University code must contain only uppercase letters, digits or underscore"
    )
    val code: String,

    @field:NotBlank(message = "University name must not be blank")
    @field:Size(max = 255, message = "University name must be <= 255 characters")
    val name: String,

    @field:NotBlank(message = "City must not be blank")
    @field:Size(max = 100, message = "City must be <= 100 characters")
    val city: String,

    @field:Size(max = 2000, message = "Description must be <= 2000 characters")
    val description: String?
)
