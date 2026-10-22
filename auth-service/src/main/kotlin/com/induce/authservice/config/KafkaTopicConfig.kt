package com.induce.authservice.config

import com.induce.common.event.KafkaTopics
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig {

    @Bean
    fun userRegisteredTopic(): NewTopic =
        TopicBuilder.name(KafkaTopics.USER_REGISTERED)
            .partitions(3)
            .replicas(1)
            .build()
}
