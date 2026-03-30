package com.induce.userservice.dto

data class MyProfileResponse(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String,
)
