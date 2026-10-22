package com.induce.userservice.mapper

import com.induce.common.model.Role
import com.induce.events.UserRegisteredEvent
import com.induce.events.UserRole
import com.induce.userservice.model.UserProfile
import java.util.UUID

fun UserRegisteredEvent.toUserProfile(username: String) = UserProfile(
    externalId = UUID.fromString(userId),
    username = username,
    email = email,
    role = role.toDomain(),
    firstName = firstName,
    lastName = lastName,
    age = age
)

fun UserRole.toDomain(): Role {
    require(this != UserRole.USER_ROLE_UNSPECIFIED && this != UserRole.UNRECOGNIZED) {
        "Role is not set in event"
    }
    return Role.valueOf(name)
}
