package com.induce.universityservice.dto

import com.induce.universityservice.model.Degree
import java.math.BigDecimal

data class ProgramShortResponse(
    val id: Long,
    val name: String,
    val rating: BigDecimal,
    val degree: Degree,
    val direction: DirectionResponse
)
