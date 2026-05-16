package com.induce.universityservice.repository

import com.induce.universityservice.model.FacultyStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FacultyStatsRepository : JpaRepository<FacultyStats, Long> {

    @Modifying
    @Query("""
        INSERT INTO faculty_stats (faculty_id, total_program_rating_sum, program_count)
        SELECT faculty_id, SUM(rating), COUNT(*)
        FROM programs WHERE rating > 0 GROUP BY faculty_id
        ON CONFLICT (faculty_id) DO UPDATE SET 
            total_program_rating_sum = EXCLUDED.total_program_rating_sum,
            program_count = EXCLUDED.program_count
    """, nativeQuery = true)
    fun recalculateStats()
}
