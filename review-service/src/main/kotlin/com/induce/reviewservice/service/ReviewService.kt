package com.induce.reviewservice.service

import com.induce.reviewservice.model.Review
import com.induce.reviewservice.repository.ReviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    // m - минимальное количество отзывов для доверия рейтингу
    private val m = 5.0

    @Transactional
    fun saveOrUpdateReview(userId: UUID, programId: Long, score: Int, comment: String?): Review {
        val existingReview = reviewRepository.findByUserIdAndProgramId(userId, programId)

        return existingReview?.apply {
            this.score = score
            this.comment = comment
            this.updatedAt = LocalDateTime.now()
        }?.let { reviewRepository.save(it) }
            ?: reviewRepository.save(
                Review(
                    userId = userId,
                    programId = programId,
                    score = score,
                    comment = comment
                )
            )
    }

    @Transactional(readOnly = true)
    fun getUserReview(userId: UUID, programId: Long): Review? {
        return reviewRepository.findByUserIdAndProgramId(userId, programId)
    }

    @Transactional(readOnly = true)
    fun getProgramReviews(programId: Long, pageable: Pageable): Page<Review> {
        return reviewRepository.findAllByProgramId(programId, pageable)
    }
}
