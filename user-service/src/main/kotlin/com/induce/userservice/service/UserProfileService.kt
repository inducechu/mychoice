package com.induce.userservice.service

import com.induce.userservice.dto.MyProfileResponse
import com.induce.userservice.dto.PublicProfileResponse
import com.induce.userservice.dto.UpdateProfileRequest
import com.induce.userservice.exception.UserNotFoundException
import com.induce.userservice.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserProfileService(
    private val repository: UserProfileRepository
) {
    fun getMyProfile(externalId: UUID): MyProfileResponse {
        val entity = repository.findByExternalId(externalId)
            ?: throw RuntimeException("Profile not found")

        return MyProfileResponse(
            username = entity.username,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            age = entity.age,
            city = entity.city
        )
    }

    fun getPublicProfile(username: String): PublicProfileResponse {
        val entity = repository.findByUsername(username)
            ?: throw UserNotFoundException("User $username not found")

        return PublicProfileResponse(
            username = entity.username,
            firstName = entity.firstName,
            lastName = entity.lastName,
            city = entity.city
        )
    }

    @Transactional
    fun updateProfile(externalId: UUID, request: UpdateProfileRequest): MyProfileResponse {
        val entity = repository.findByExternalId(externalId)
            ?: throw UserNotFoundException("User $externalId not found")

        entity.firstName = request.firstName
        entity.lastName = request.lastName
        entity.age = request.age
        entity.city = request.city

        return MyProfileResponse(
            username = entity.username,
            email = entity.email,
            firstName = entity.firstName,
            lastName = entity.lastName,
            age = entity.age,
            city = entity.city
        )
    }
}
