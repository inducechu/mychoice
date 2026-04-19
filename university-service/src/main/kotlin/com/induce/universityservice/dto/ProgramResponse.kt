package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree
import java.math.BigDecimal

data class ProgramResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val rating: BigDecimal,
    val degree: Degree,
    val direction: DirectionResponse,
    val faculty: FacultyShortResponse
)
