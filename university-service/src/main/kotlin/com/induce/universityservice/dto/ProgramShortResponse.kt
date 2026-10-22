package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree

data class ProgramShortResponse(
    val id: Long,
    val name: String,
    val degree: Degree,
    val direction: DirectionResponse
)
