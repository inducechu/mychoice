package com.induce.gatewayservice.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtService(
    @param:Value("\${jwt.secret}") private val secret: String,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(JwtService::class.java)
    private val key by lazy { Keys.hmacShaKeyFor(secret.toByteArray()) }

    fun getClaims(token: String): Claims? {
        return try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            log.error("JWT Token истек: {}", e.message)
            null
        } catch (e: SignatureException) {
            log.error("Неверная подпись JWT: {}", e.message)
            null
        } catch (e: Exception) {
            log.error("Ошибка валидации токена: {}", e.message)
            null
        }
    }

    fun extractUserId(claims: Claims): String? = claims.subject
    fun extractRole(claims: Claims): String? = claims["role"] as? String
}
