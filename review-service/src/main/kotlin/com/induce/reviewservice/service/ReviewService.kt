package com.induce.reviewservice.service

import com.induce.reviewservice.model.Review
import com.induce.reviewservice.repository.ReviewRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ReviewService(private val reviewRepository: ReviewRepository) {

    // m - минимальное количество отзывов для доверия рейтингу
    private val m = 5.0

    @Transactional
    fun createReview(userId: UUID, programId: Long, score: Int, comment: String?): Review {
        if (reviewRepository.existsByUserIdAndProgramId(userId, programId)) {
            throw IllegalStateException("Вы уже оставили отзыв на эту программу")
        }

        // TODO: Здесь будет gRPC вызов для проверки, учится ли студент на программе

        val review = Review(
            userId = userId,
            programId = programId,
            score = score,
            comment = comment
        )
        return reviewRepository.save(review)
    }

    /**
     * Расчет Bayesian Average для конкретной программы
     * WR = (v * R + m * C) / (v + m)
     */
    fun calculateBayesianRating(programId: Long): Double {
        val stats = reviewRepository.getStatsByProgramId(programId)
        val v = (stats[0] as? Long)?.toDouble() ?: 0.0
        val r = (stats[1] as? Double) ?: 0.0

        if (v == 0.0) return 0.0

        val c = reviewRepository.getGlobalAverageScore() ?: 0.0

        return (v * r + m * c) / (v + m)
    }
}
