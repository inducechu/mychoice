package com.induce.universityservice.dto

data class UniversityResponse(
    val id: Long,
    val code: String,
    val name: String,
    val cityId: Long,
    val cityName: String,
    val description: String?
)
