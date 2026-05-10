package com.induce.reviewservice.dto

data class ReviewRequest(
    val programId: Long,
    val score: Int,
    val comment: String?
)
