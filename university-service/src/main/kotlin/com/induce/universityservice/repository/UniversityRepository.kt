package com.induce.universityservice.repository

import com.induce.universityservice.model.University
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByCode(code: String): University?

    @Modifying
    @Query("""
        UPDATE universities u
        SET rating = COALESCE((us.total_faculty_rating_sum / us.faculty_count), 0)
        FROM university_stats us WHERE u.id = us.university_id
    """, nativeQuery = true)
    fun syncRatings()
}
