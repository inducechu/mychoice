package com.induce.universityservice.dto

data class UniversityResponse(
    val id: Long,
    val code: String,
    val name: String,
    val city: String,
    val description: String?,
    val rating: String
)
