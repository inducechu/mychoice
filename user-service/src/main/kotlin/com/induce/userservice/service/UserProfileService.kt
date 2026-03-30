package com.induce.userservice.service

import com.induce.userservice.dto.MyProfileResponse
import com.induce.userservice.dto.PublicProfileResponse
import com.induce.userservice.repository.UserProfileRepository
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val repository: UserProfileRepository
) {
    fun getMyProfile(externalId: String): MyProfileResponse {
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
            ?: throw RuntimeException("User $username not found")

        return PublicProfileResponse(
            username = entity.username,
            firstName = entity.firstName,
            lastName = entity.lastName,
            city = entity.city
        )
    }
}
