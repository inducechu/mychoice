package com.induce.authservice.service

import com.induce.authservice.model.OutboxStatus
import com.induce.authservice.repository.OutboxEventRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class OutboxPublisher(
    private val outboxEventRepository: OutboxEventRepository,
    private val kafkaTemplate: KafkaTemplate<String, ByteArray>,
    @Value("\${outbox.publisher.batch-size}") private val batchSize: Int,
    @Value("\${outbox.publisher.max-attempts}") private val maxAttempts: Int,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelayString = "\${outbox.publisher.fixed-delay-ms}")
    @Transactional
    fun publishBatch() {
        val batch = outboxEventRepository.lockNextBatch(batchSize)
        if (batch.isEmpty()) return

        for (event in batch) {
            try {
                kafkaTemplate.send(event.topic, event.aggregateId.toString(), event.payload)
                    .get(10, TimeUnit.SECONDS)

                event.status = OutboxStatus.PUBLISHED
                event.publishedAt = Instant.now()
                event.lastError = null
            } catch (e: Exception) {
                event.attempts++
                event.lastError = e.message?.take(1000)
                if (event.attempts >= maxAttempts) {
                    event.status = OutboxStatus.FAILED
                }
                log.warn("Outbox event {} not published (attempt {}): {}", event.id, event.attempts, e.message)
                break
            }
        }
    }
}
