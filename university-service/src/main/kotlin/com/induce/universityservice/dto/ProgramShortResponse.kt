package com.induce.universityservice.dto

data class ProgramShortResponse(
    val id: Long,
    val name: String,
    val rating: String,
    val degree: String,
    val direction: DirectionResponse
)
