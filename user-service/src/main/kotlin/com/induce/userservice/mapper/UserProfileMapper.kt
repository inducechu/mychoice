package com.induce.userservice.mapper

import com.induce.userservice.dto.MyProfileResponse
import com.induce.userservice.dto.PublicProfileResponse
import com.induce.userservice.model.UserProfile

fun UserProfile.toMyProfile() = MyProfileResponse(
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    age = age,
    affiliations = affiliations.map { it.toResponse() }
)

fun UserProfile.toPublicProfile() = PublicProfileResponse(
    username = username,
    firstName = firstName,
    lastName = lastName,
    affiliations = affiliations.map { it.toResponse() }
)
