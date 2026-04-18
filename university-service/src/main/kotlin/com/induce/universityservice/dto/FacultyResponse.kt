package com.induce.universityservice.dto

data class FacultyResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val rating: String,
    val university: UniversityShortResponse
)
