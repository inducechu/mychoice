package com.induce.universityservice.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

@Entity
@Table(
    name = "faculties",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["university_id", "name"])
    ]
)
class Faculty(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(length = 2000)
    var description: String? = null,

    @Column(nullable = false, precision = 3, scale = 1)
    @field:DecimalMin("0.0")
    @field:DecimalMax("10.0")
    var rating: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    var university: University,

    @OneToMany(mappedBy = "faculty", cascade = [CascadeType.ALL], orphanRemoval = true)
    val programs: MutableList<Program> = mutableListOf()
)
