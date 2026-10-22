package com.induce.authservice.service

import com.induce.authservice.dto.LoginRequest
import com.induce.authservice.dto.RegisterRequest
import com.induce.authservice.exception.InvalidCredentialsException
import com.induce.authservice.exception.RoleNotAllowedException
import com.induce.authservice.exception.UserAlreadyExistsException
import com.induce.authservice.exception.UserNotFoundException
import com.induce.authservice.model.User
import com.induce.authservice.repository.UserRepository
import com.induce.common.model.Role
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService,
    private val outboxWriter: OutboxWriter
) {

    @Transactional
    fun register(request: RegisterRequest): User {
        if (request.role !in SELF_REGISTRATION_ROLES) {
            throw RoleNotAllowedException(request.role)
        }

        if (userRepository.findByEmail(request.email) != null) {
            throw UserAlreadyExistsException(request.email)
        }

        val newUser = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password)!!,
            role = request.role,
        )

        val savedUser = userRepository.save(newUser)
        outboxWriter.userRegistered(savedUser, request)

        return savedUser
    }

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException(request.email)

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw InvalidCredentialsException()
        }

        return jwtService.generateToken(user)
    }

    companion object {
        private val SELF_REGISTRATION_ROLES = setOf(Role.ABITURIENT, Role.STUDENT, Role.TEACHER)
    }

}
