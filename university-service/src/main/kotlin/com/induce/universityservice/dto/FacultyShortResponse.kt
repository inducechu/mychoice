package com.induce.universityservice.dto

import java.math.BigDecimal

data class FacultyShortResponse(
    val id: Long,
    val name: String,
    val rating: BigDecimal
)
