package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree

data class CreateProgramRequest(
    val directionCode: String,
    val name: String,
    val description: String?,
    val degree: Degree
)
