package com.induce.reviewservice.repository

import com.induce.reviewservice.model.ProgramStats
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProgramStatsRepository : JpaRepository<ProgramStats, Long>
