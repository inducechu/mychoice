package com.induce.authservice.exception

import org.springframework.http.HttpStatus

sealed class AuthException(
    override val message: String,
    val status: HttpStatus
) : RuntimeException(message)

class UserAlreadyExistsException(email: String) :
    AuthException("User with email '$email' already exists", HttpStatus.CONFLICT)

class InvalidCredentialsException :
    AuthException("Invalid email or password", HttpStatus.UNAUTHORIZED)

class UserNotFoundException(identifier: String) :
    AuthException("User '$identifier' not found", HttpStatus.NOT_FOUND)