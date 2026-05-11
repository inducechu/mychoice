package com.induce.reviewservice.dto

import org.springframework.data.domain.Page

data class ReviewPagedResponse<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val last: Boolean
)

fun <T : Any, R> Page<T>.toReviewPagedResponse(mapper: (T) -> R): ReviewPagedResponse<R> {
    return ReviewPagedResponse(
        content = this.content.map(mapper),
        totalElements = this.totalElements,
        totalPages = this.totalPages,
        last = this.isLast
    )
}
