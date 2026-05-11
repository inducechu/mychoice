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
    private val logger = LoggerFactory.getLogger(RatingUpdateService::class.java)

    @Scheduled(fixedRateString = "\${app.scheduling.rate}")
    @Transactional
    fun updateHierarchyRatings() {
        logger.info("Starting scheduled rating recalculation...")

        val ratingList = reviewClient.fetchUpdatedRatings()
        val ratingsMap = ratingList.ratingsList.associate { it.programId to it.bayesianRating }

        val universities = universityRepository.findAll()

        universities.forEach { university ->
            university.faculties.forEach { faculty ->
                faculty.programs.forEach { program ->
                    ratingsMap[program.id]?.let {
                        program.rating = it.toBigDecimal().setScale(1, RoundingMode.HALF_UP)
                    }
                }

                val activePrograms = faculty.programs.filter { it.rating > BigDecimal.ZERO }

                if (activePrograms.isNotEmpty()) {
                    val sum = activePrograms.map { it.rating }.reduce { a, b -> a + b }
                    faculty.rating = sum.divide(activePrograms.size.toBigDecimal(), 1, RoundingMode.HALF_UP)
                } else {
                    faculty.rating = BigDecimal.ZERO.setScale(1)
                }
            }

            val activeFaculties = university.faculties.filter { it.rating > BigDecimal.ZERO }

            if (activeFaculties.isNotEmpty()) {
                val sum = activeFaculties.map { it.rating }.reduce { a, b -> a + b }
                university.rating = sum.divide(activeFaculties.size.toBigDecimal(), 1, RoundingMode.HALF_UP)
            } else {
                university.rating = BigDecimal.ZERO.setScale(1)
            }
        }

        universityRepository.saveAll(universities)
        logger.info("Ratings hierarchy updated successfully (zeros excluded)")
    }
}
