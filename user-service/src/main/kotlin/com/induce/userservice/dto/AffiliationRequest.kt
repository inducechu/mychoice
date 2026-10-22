package com.induce.userservice.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class AffiliationRequest(
    @field:NotNull(message = "universityId is required")
    val universityId: Long?,
    val facultyId: Long? = null,
    val programId: Long? = null,
    @field:Min(value = 1950, message = "enrollmentYear is too old")
    val enrollmentYear: Int? = null
)
