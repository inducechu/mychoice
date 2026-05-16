package com.induce.universityservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "faculty_stats")
class FacultyStats(
    @Id
    @Column(name = "faculty_id")
    val facultyId: Long,

    @Column(name = "total_program_rating_sum", nullable = false, precision = 6, scale = 2)
    var totalProgramRatingSum: BigDecimal = BigDecimal.ZERO,

    @Column(name = "program_count", nullable = false)
    var programCount: Long = 0
)
