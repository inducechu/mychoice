package com.induce.universityservice.scheduler

import com.induce.universityservice.grpc.ReviewClient
import com.induce.universityservice.repository.UniversityRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class RatingUpdateService(
    private val reviewClient: ReviewClient,
    private val universityRepository: UniversityRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRateString = "\${app.scheduling.rate}")
    @Transactional
    fun updateHierarchyRatings() {
        logger.info("Starting scheduled rating recalculation...")

        val ratingsMap = reviewClient.fetchUpdatedRatings()
            .ratingsList
            .associate { it.programId to it.bayesianRating.toBigDecimal().setScale(1, RoundingMode.HALF_UP) }

        universityRepository.findAll().forEach { university ->
            university.faculties.forEach { faculty ->
                faculty.programs.forEach { program ->
                    ratingsMap[program.id]?.let { program.rating = it }
                }

                faculty.rating = faculty.programs
                    .map { it.rating }
                    .averageNonZero()
            }

            // Считаем ВУЗ
            university.rating = university.faculties
                .map { it.rating }
                .averageNonZero()
        }

        logger.info("Ratings hierarchy updated successfully")
    }

    /**
     * Extension-функция для красивого расчета среднего значения BigDecimal
     */
    private fun List<BigDecimal>.averageNonZero(): BigDecimal =
        this.filter { it > BigDecimal.ZERO }
            .takeIf { it.isNotEmpty() }
            ?.let { activeList ->
                activeList.reduce(BigDecimal::add)
                    .divide(activeList.size.toBigDecimal(), 1, RoundingMode.HALF_UP)
            } ?: BigDecimal.ZERO.setScale(1)
}
