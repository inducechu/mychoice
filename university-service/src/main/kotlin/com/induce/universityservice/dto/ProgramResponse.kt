package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree

data class ProgramResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val degree: Degree,
    val direction: DirectionResponse,
    val faculty: FacultyShortResponse
)
