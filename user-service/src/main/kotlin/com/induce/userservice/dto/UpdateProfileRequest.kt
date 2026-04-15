package com.induce.userservice.dto

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String
)
