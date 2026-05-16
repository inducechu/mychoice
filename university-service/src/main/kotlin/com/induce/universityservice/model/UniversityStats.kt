package com.induce.universityservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "university_stats")
class UniversityStats(
    @Id
    @Column(name = "university_id")
    val universityId: Long,

    @Column(name = "total_faculty_rating_sum", nullable = false, precision = 6, scale = 2)
    var totalFacultyRatingSum: BigDecimal = BigDecimal.ZERO,

    @Column(name = "faculty_count", nullable = false)
    var facultyCount: Long = 0
)
