package com.induce.reviewservice.controller

import com.induce.reviewservice.dto.ReviewPageRequest
import com.induce.reviewservice.dto.ReviewPagedResponse
import com.induce.reviewservice.dto.ReviewRequest
import com.induce.reviewservice.dto.ReviewResponse
import com.induce.reviewservice.dto.toResponse
import com.induce.reviewservice.dto.toReviewPagedResponse
import com.induce.reviewservice.service.ReviewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/api/review")
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping("/save")
    fun saveReview(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @RequestBody request: ReviewRequest
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.saveOrUpdateReview(
            userId = userId,
            programId = request.programId,
            score = request.score,
            comment = request.comment
        )
        return ResponseEntity.ok(review.toResponse())
    }

    @GetMapping("/my")
    fun getMyReview(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @RequestParam programId: Long
    ): ResponseEntity<ReviewResponse> {
        val review = reviewService.getUserReview(userId, programId)
        return review?.let { ResponseEntity.ok(it.toResponse()) }
            ?: ResponseEntity.notFound().build()
    }

    @GetMapping("/program/{programId}")
    fun getProgramReviews(
        @PathVariable programId: Long,
        @ModelAttribute filter: ReviewPageRequest
    ): ResponseEntity<ReviewPagedResponse<ReviewResponse>> {

        val reviews = reviewService.getProgramReviews(
            programId = programId,
            pageable = filter.toPageable()
        )

        return ResponseEntity.ok(reviews.toReviewPagedResponse { it.toResponse() })
    }
}
