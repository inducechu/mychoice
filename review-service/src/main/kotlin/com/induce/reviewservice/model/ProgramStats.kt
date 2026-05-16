package com.induce.reviewservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "program_stats")
class ProgramStats(
    @Id
    @Column(name = "program_id")
    val programId: Long,

    @Column(name = "total_score_sum", nullable = false)
    var totalScoreSum: Long = 0,

    @Column(name = "review_count", nullable = false)
    var reviewCount: Long = 0
)
