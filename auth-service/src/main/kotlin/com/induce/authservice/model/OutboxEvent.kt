package com.induce.authservice.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID



@Entity
@Table(name = "outbox_events")
class OutboxEvent(
    @Id
    val id: UUID,

    @Column(nullable = false)
    val aggregateType: String,

    @Column(nullable = false)
    val aggregateId: UUID,

    @Column(nullable = false)
    val eventType: String,

    @Column(nullable = false)
    val topic: String,

    @Column(nullable = false)
    val payload: ByteArray,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OutboxStatus = OutboxStatus.NEW,

    @Column(nullable = false)
    var attempts: Int = 0,

    @Column(columnDefinition = "text")
    var lastError: String? = null,

    @Column(nullable = false)
    val createdAt: Instant = Instant.now(),

    var publishedAt: Instant? = null
)
