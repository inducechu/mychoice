package com.induce.universityservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class CreateDirectionRequest(
    @field:NotBlank(message = "Код направления не может быть пустым")
    @field:Pattern(
        regexp = "^\\d{2}\\.\\d{2}\\.\\d{2}$",
        message = "Код направления должен соответствовать формату ХХ.ХХ.ХХ (например, 09.03.04)"
    )
    val code: String,

    @field:NotBlank(message = "Название направления не может быть пустым")
    val name: String
)
