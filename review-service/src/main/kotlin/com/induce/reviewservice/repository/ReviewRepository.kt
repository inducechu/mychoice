package com.induce.reviewservice.repository

import com.induce.reviewservice.model.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ReviewRepository : JpaRepository<Review, UUID> {
    fun existsByUserIdAndProgramId(userId: Long, programId: Long): Boolean

    @Query("SELECT COUNT(r), AVG(r.score) FROM Review r WHERE r.programId = :programId")
    fun getStatsByProgramId(programId: Long): Array<Any?>

    @Query("SELECT r.programId, COUNT(r), AVG(r.score) FROM Review r GROUP BY r.programId")
    fun getAllProgramsStats(): List<Array<Any>>

    @Query("SELECT AVG(r.score) FROM Review r")
    fun getGlobalAverageScore(): Double?
}
