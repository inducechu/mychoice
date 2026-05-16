package com.induce.universityservice.repository

import com.induce.universityservice.model.UniversityStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UniversityStatsRepository : JpaRepository<UniversityStats, Long> {

    @Modifying
    @Query(
        """
        INSERT INTO university_stats (university_id, total_faculty_rating_sum, faculty_count)
        SELECT university_id, SUM(rating), COUNT(*)
        FROM faculties WHERE rating > 0 GROUP BY university_id
        ON CONFLICT (university_id) DO UPDATE SET 
            total_faculty_rating_sum = EXCLUDED.total_faculty_rating_sum,
            faculty_count = EXCLUDED.faculty_count
    """, nativeQuery = true
    )
    fun recalculateStats()
}
