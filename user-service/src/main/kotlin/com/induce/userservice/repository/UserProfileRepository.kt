package com.induce.userservice.repository

import com.induce.userservice.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    fun findByExternalId(externalId: UUID): UserProfile?

    fun findByUsername(username: String): UserProfile?
}
