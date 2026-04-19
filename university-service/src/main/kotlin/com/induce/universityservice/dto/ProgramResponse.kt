package com.induce.universityservice.dto

data class ProgramResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val rating: String,
    val degree: String,
    val direction: DirectionResponse,
    val facultyId: Long
)
