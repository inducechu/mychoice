package com.induce.universityservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

@Entity
@Table(
    name = "programs",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["faculty_id", "name"])
    ]
)
class Program(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "direction_id", nullable = false)
    val direction: Direction,

    @Column(nullable = false)
    var name: String,

    @Column(length = 2000)
    var description: String? = null,

    @Column(nullable = false, precision = 3, scale = 1)
    @field:DecimalMin("0.0")
    @field:DecimalMax("10.0")
    var rating: BigDecimal,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var degree: Degree,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    var faculty: Faculty
)
