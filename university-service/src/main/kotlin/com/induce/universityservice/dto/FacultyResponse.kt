package com.induce.universityservice.dto

import java.math.BigDecimal

data class FacultyResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val rating: BigDecimal,
    val university: UniversityShortResponse
)
