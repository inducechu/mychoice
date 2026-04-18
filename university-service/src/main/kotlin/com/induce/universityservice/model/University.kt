package com.induce.universityservice.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal

@Entity
@Table(name = "universities")
class University(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val code: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var city: String,

    @Column(length = 2000)
    var description: String? = null,

    @Column(nullable = false, precision = 3, scale = 1)
    @field:DecimalMin("0.0")
    @field:DecimalMax("10.0")
    var rating: BigDecimal,

    @OneToMany(mappedBy = "university", cascade = [CascadeType.ALL], orphanRemoval = true)
    val faculties: MutableList<Faculty> = mutableListOf()
)
