package com.induce.userservice.repository

import com.induce.userservice.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserProfile, Long> {
    fun findByExternalId(externalId: String): UserProfile?
}
