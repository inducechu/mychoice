package com.induce.reviewservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(
    name = "reviews",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["user_id", "program_id"])
    ]
)
class Review(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "program_id", nullable = false)
    val programId: Long,

    @Column(nullable = false)
    @field:Min(1)
    @field:Max(10)
    var score: Int,

    @Column(length = 2000)
    var comment: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
)
