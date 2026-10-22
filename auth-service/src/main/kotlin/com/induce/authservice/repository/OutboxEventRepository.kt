package com.induce.authservice.repository

import com.induce.authservice.model.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface OutboxEventRepository : JpaRepository<OutboxEvent, UUID> {

    @Query(
        value = """
            SELECT * FROM outbox_events
            WHERE status = 'NEW'
            ORDER BY created_at
            LIMIT :limit
            FOR UPDATE SKIP LOCKED
        """,
        nativeQuery = true
    )
    fun lockNextBatch(limit: Int): List<OutboxEvent>
}
