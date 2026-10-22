package com.induce.userservice.service

import com.induce.events.UserRegisteredEvent
import com.induce.userservice.dto.MyProfileResponse
import com.induce.userservice.dto.PublicProfileResponse
import com.induce.userservice.dto.UpdateProfileRequest
import com.induce.userservice.exception.UserNotFoundException
import com.induce.userservice.mapper.toMyProfile
import com.induce.userservice.mapper.toPublicProfile
import com.induce.userservice.mapper.toUserProfile
import com.induce.userservice.model.UserProfile
import com.induce.userservice.repository.UserProfileRepository
import com.induce.userservice.util.UsernameGenerator
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserProfileService(
    private val repository: UserProfileRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun createFromRegistration(event: UserRegisteredEvent) {
        val externalId = UUID.fromString(event.userId)

        if (repository.existsByExternalId(externalId)) {
            log.info("Profile for {} already exists, event {} skipped", externalId, event.eventId)
            return
        }

        repository.save(event.toUserProfile(UsernameGenerator.generate()))
        log.info("Profile created for {} from event {}", externalId, event.eventId)
    }

    @Transactional(readOnly = true)
    fun getMyProfile(externalId: UUID): MyProfileResponse =
        findByExternalId(externalId).toMyProfile()

    @Transactional(readOnly = true)
    fun getPublicProfile(username: String): PublicProfileResponse {
        val entity = repository.findByUsername(username)
            ?: throw UserNotFoundException("User $username not found")
        return entity.toPublicProfile()
    }

    @Transactional
    fun updateProfile(externalId: UUID, request: UpdateProfileRequest): MyProfileResponse {
        val entity = findByExternalId(externalId)

        entity.firstName = request.firstName
        entity.lastName = request.lastName
        entity.age = request.age

        return entity.toMyProfile()
    }

    private fun findByExternalId(externalId: UUID): UserProfile =
        repository.findByExternalId(externalId)
            ?: throw UserNotFoundException("User $externalId not found")
}
