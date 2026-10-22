package com.induce.userservice.dto

data class AffiliationResponse(
    val id: Long,
    val universityId: Long,
    val facultyId: Long?,
    val programId: Long?,
    val enrollmentYear: Int?,
    val course: Int?
)
