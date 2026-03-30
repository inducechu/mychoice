package com.induce.authservice.service

import com.induce.authservice.dto.LoginRequest
import com.induce.authservice.dto.RegisterRequest
import com.induce.authservice.exception.InvalidCredentialsException
import com.induce.authservice.exception.UserAlreadyExistsException
import com.induce.authservice.exception.UserNotFoundException
import com.induce.authservice.model.Role
import com.induce.authservice.model.User
import com.induce.authservice.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtService: JwtService,
    private val userSyncClient: UserSyncClient,
) {

    @Transactional
    fun register(request: RegisterRequest): User {
        if (userRepository.findByEmail(request.email) != null) {
            throw UserAlreadyExistsException(request.email)
        }

        val hash = passwordEncoder.encode(request.password)!!

        val newUser = User(
            email = request.email,
            passwordHash = hash,
            role = request.role ?: Role.ABITURIENT,
        )

        val savedUser = userRepository.save(newUser)
        userSyncClient.sendUserToProfile(request, savedUser.id.toString())

        return savedUser
    }

    fun login(request: LoginRequest): String {
        val user = userRepository.findByEmail(request.email)
            ?: throw UserNotFoundException(request.email)

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw InvalidCredentialsException()
        }

        return jwtService.generateToken(user)
    }

}
