package com.induce.universityservice.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateFacultyRequest(

    @field:NotBlank(message = "Faculty name must not be blank")
    @field:Size(max = 255, message = "Faculty name must be <= 255 characters")
    val name: String,

    @field:Size(max = 2000, message = "Description must be <= 2000 characters")
    val description: String?
)
