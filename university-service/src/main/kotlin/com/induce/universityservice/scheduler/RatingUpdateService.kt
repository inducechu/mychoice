package com.induce.universityservice.scheduler

import com.induce.universityservice.grpc.ReviewClient
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.FacultyStatsRepository
import com.induce.universityservice.repository.ProgramRepository
import com.induce.universityservice.repository.UniversityRepository
import com.induce.universityservice.repository.UniversityStatsRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class RatingUpdateService(
    private val reviewClient: ReviewClient,
    private val programRepository: ProgramRepository,
    private val facultyRepository: FacultyRepository,
    private val facultyStatsRepository: FacultyStatsRepository,
    private val universityRepository: UniversityRepository,
    private val universityStatsRepository: UniversityStatsRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedRateString = "\${app.scheduling.rate}")
    @Transactional
    fun updateHierarchyRatings() {
        logger.info("Starting optimized database rating recalculation...")

        val ratingsList = reviewClient.fetchUpdatedRatings().ratingsList
        if (ratingsList.isEmpty()) {
            logger.info("No ratings to update.")
            return
        }

        ratingsList.forEach { protoRating ->
            programRepository.updateRatingById(
                id = protoRating.programId,
                rating = BigDecimal.valueOf(protoRating.bayesianRating)
            )
        }

        facultyStatsRepository.recalculateStats()
        facultyRepository.syncRatings()

        universityStatsRepository.recalculateStats()
        universityRepository.syncRatings()

        logger.info("Database ratings hierarchy updated successfully with proper repository isolation")
    }
}
