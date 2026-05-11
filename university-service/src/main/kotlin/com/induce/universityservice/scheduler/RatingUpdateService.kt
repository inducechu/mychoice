package com.induce.universityservice.scheduler

import com.induce.universityservice.grpc.ReviewClient
import com.induce.universityservice.repository.UniversityRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

        // 1. Получаем данные через наш новый клиент
        val ratingList = reviewClient.fetchUpdatedRatings()
        val ratingsMap = ratingList.ratingsList.associate { it.programId to it.bayesianRating }

        val universities = universityRepository.findAll()

        universities.forEach { university ->
            university.faculties.forEach { faculty ->
                faculty.programs.forEach { program ->
                    // Обновляем рейтинг программы, если он пришел из review-service
                    ratingsMap[program.id]?.let {
                        program.rating = it.toBigDecimal().setScale(1, RoundingMode.HALF_UP)
                    }
                }

                // 2. Рейтинг факультета = среднее по его программам
                if (faculty.programs.isNotEmpty()) {
                    val avg = faculty.programs.map { it.rating }.reduce { a, b -> a + b }
                        .divide(faculty.programs.size.toBigDecimal(), 1, RoundingMode.HALF_UP)
                    faculty.rating = avg
                }
            }

            // 3. Рейтинг ВУЗа = среднее по его факультетам
            if (university.faculties.isNotEmpty()) {
                val avg = university.faculties.map { it.rating }.reduce { a, b -> a + b }
                    .divide(university.faculties.size.toBigDecimal(), 1, RoundingMode.HALF_UP)
                university.rating = avg
            }
        }

        universityRepository.saveAll(universities)
        logger.info("Ratings hierarchy updated successfully")
    }
}
