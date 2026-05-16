package com.induce.reviewservice.service

import com.induce.reviewservice.model.ProgramStats
import com.induce.reviewservice.model.Review
import com.induce.reviewservice.repository.ProgramStatsRepository
import com.induce.reviewservice.repository.ReviewRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.UUID

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val programStatsRepository: ProgramStatsRepository
) {

    // m - минимальное количество отзывов для доверия рейтингу
    private val m = 5.0

    @Transactional
    fun saveOrUpdateReview(userId: UUID, programId: Long, score: Int, comment: String?): Review {
        val existingReview = reviewRepository.findByUserIdAndProgramId(userId, programId)

        val stats = programStatsRepository.findById(programId)
            .orElseGet { programStatsRepository.save(ProgramStats(programId = programId)) }

        return if (existingReview != null) {
            stats.totalScoreSum = stats.totalScoreSum - existingReview.score + score
            programStatsRepository.save(stats)

            existingReview.apply {
                this.score = score
                this.comment = comment
                this.updatedAt = LocalDateTime.now()
            }.let { reviewRepository.save(it) }
        } else {
            stats.totalScoreSum += score
            stats.reviewCount += 1
            programStatsRepository.save(stats)

            reviewRepository.save(
                Review(
                    userId = userId,
                    programId = programId,
                    score = score,
                    comment = comment
                )
            )
        }
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
