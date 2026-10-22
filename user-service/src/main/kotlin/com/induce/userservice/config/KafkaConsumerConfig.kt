package com.induce.userservice.config

import com.google.protobuf.InvalidProtocolBufferException
import com.induce.common.event.KafkaTopics
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.TopicPartition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConsumerConfig {

    @Bean
    fun userRegisteredDltTopic(): NewTopic =
        TopicBuilder.name(DLT_TOPIC).partitions(1).replicas(1).build()

    @Bean
    fun kafkaErrorHandler(kafkaTemplate: KafkaTemplate<String, ByteArray>): DefaultErrorHandler {
        val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { _, _ ->
            TopicPartition(DLT_TOPIC, -1)
        }
        return DefaultErrorHandler(recoverer, FixedBackOff(1000L, 3L)).apply {
            addNotRetryableExceptions(
                InvalidProtocolBufferException::class.java,
                IllegalArgumentException::class.java
            )
        }
    }

    companion object {
        const val DLT_TOPIC = "${KafkaTopics.USER_REGISTERED}.DLT"
    }
}
