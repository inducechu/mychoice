package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateProgramRequest(

    @field:NotBlank(message = "Direction code must not be blank")
    @field:Size(max = 20, message = "Direction code must be <= 20 characters")
    val directionCode: String,

    @field:NotBlank(message = "Program name must not be blank")
    @field:Size(max = 255, message = "Program name must be <= 255 characters")
    val name: String,

    @field:Size(max = 2000, message = "Description must be <= 2000 characters")
    val description: String?,

    val degree: Degree
)
