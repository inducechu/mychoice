package com.induce.authservice.service

import com.google.protobuf.Timestamp
import com.induce.authservice.dto.RegisterRequest
import com.induce.authservice.model.OutboxEvent
import com.induce.authservice.model.User
import com.induce.authservice.repository.OutboxEventRepository
import com.induce.common.event.KafkaTopics
import com.induce.events.UserRegisteredEvent
import com.induce.events.UserRole
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Component
class OutboxWriter(
    private val outboxEventRepository: OutboxEventRepository
) {

    @Transactional(propagation = Propagation.MANDATORY)
    fun userRegistered(user: User, request: RegisterRequest) {
        val userId = requireNotNull(user.id) { "User must be saved before writing outbox event" }
        val eventId = UUID.randomUUID()
        val now = Instant.now()

        val event = UserRegisteredEvent.newBuilder()
            .setEventId(eventId.toString())
            .setUserId(userId.toString())
            .setEmail(user.email)
            .setRole(UserRole.valueOf(user.role.name))
            .setFirstName(request.firstName)
            .setLastName(request.lastName)
            .setAge(request.age)
            .setOccurredAt(
                Timestamp.newBuilder()
                    .setSeconds(now.epochSecond)
                    .setNanos(now.nano)
            )
            .build()

        outboxEventRepository.save(
            OutboxEvent(
                id = eventId,
                aggregateType = "User",
                aggregateId = userId,
                eventType = "UserRegistered",
                topic = KafkaTopics.USER_REGISTERED,
                payload = event.toByteArray(),
                createdAt = now
            )
        )
    }
}
