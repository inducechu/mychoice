package com.induce.reviewservice.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

data class ReviewPageRequest(
    val page: Int = 0,
    val size: Int = 10
) {
    fun toPageable(): Pageable = PageRequest.of(
        page,
        if (size > 30) 30 else size,
        Sort.by("createdAt").descending()
    )
}
