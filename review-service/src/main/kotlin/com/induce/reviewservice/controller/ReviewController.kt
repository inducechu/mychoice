package com.induce.reviewservice.controller

import com.induce.reviewservice.dto.ReviewRequest
import com.induce.reviewservice.dto.ReviewResponse
import com.induce.reviewservice.dto.toResponse
import com.induce.reviewservice.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/reviews")
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping
    fun createReview(
        @RequestHeader("X-User-Id") userId: Long,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.createReview(
            userId = userId,
            programId = request.programId,
            score = request.score,
            comment = request.comment
        )
        return ResponseEntity.ok(review.toResponse())
    }
}
