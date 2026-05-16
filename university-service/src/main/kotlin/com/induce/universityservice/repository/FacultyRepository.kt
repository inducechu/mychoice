package com.induce.universityservice.repository

import com.induce.universityservice.model.Faculty
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FacultyRepository : JpaRepository<Faculty, Long> {
    fun findByUniversityId(universityId: Long): List<Faculty>

    @Modifying
    @Query("""
        UPDATE faculties f
        SET rating = COALESCE((fs.total_program_rating_sum / fs.program_count), 0)
        FROM faculty_stats fs WHERE f.id = fs.faculty_id
    """, nativeQuery = true)
    fun syncRatings()
}
