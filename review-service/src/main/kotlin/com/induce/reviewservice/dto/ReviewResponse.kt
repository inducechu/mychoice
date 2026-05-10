package com.induce.reviewservice.dto

import com.induce.reviewservice.model.Review
import java.time.LocalDateTime
import java.util.UUID

data class ReviewResponse(
    val id: UUID,
    val programId: Long,
    val score: Int,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)

internal fun Review.toResponse() = ReviewResponse(
    id = this.id!!,
    programId = this.programId,
    score = this.score,
    comment = this.comment,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
