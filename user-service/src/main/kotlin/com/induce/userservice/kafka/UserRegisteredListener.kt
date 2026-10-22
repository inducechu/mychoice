package com.induce.userservice.kafka

import com.induce.common.event.KafkaTopics
import com.induce.events.UserRegisteredEvent
import com.induce.userservice.service.UserProfileService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class UserRegisteredListener(
    private val userProfileService: UserProfileService
) {

    @KafkaListener(topics = [KafkaTopics.USER_REGISTERED])
    fun onUserRegistered(record: ConsumerRecord<String, ByteArray>) {
        val event = UserRegisteredEvent.parseFrom(record.value())
        userProfileService.createFromRegistration(event)
    }
}
