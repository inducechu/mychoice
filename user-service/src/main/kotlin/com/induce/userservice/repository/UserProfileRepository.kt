package com.induce.userservice.repository

import com.induce.userservice.model.UserProfile
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, Long> {
    @EntityGraph(attributePaths = ["affiliations"])
    fun findByExternalId(externalId: UUID): UserProfile?

    @EntityGraph(attributePaths = ["affiliations"])
    fun findByUsername(username: String): UserProfile?

    fun existsByExternalId(externalId: UUID): Boolean
}
