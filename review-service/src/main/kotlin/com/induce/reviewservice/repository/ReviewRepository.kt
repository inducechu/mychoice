package com.induce.reviewservice.repository

import com.induce.reviewservice.model.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ReviewRepository : JpaRepository<Review, UUID> {
    fun findByUserIdAndProgramId(userId: UUID, programId: Long): Review?

    fun findAllByProgramId(programId: Long, pageable: Pageable): Page<Review>

    @Query("SELECT r.programId, COUNT(r), AVG(r.score) FROM Review r GROUP BY r.programId")
    fun getAllProgramsStats(): List<Array<Any>>

    @Query("SELECT AVG(r.score) FROM Review r")
    fun getGlobalAverageScore(): Double?
}
