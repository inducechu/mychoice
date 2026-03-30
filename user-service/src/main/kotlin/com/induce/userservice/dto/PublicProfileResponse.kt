package com.induce.userservice.dto

data class PublicProfileResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val city: String
)