package com.induce.gatewayservice.filter

import com.induce.common.model.Role
import com.induce.gatewayservice.exception.writeError
import com.induce.gatewayservice.security.AccessPolicy
import com.induce.gatewayservice.security.AuthHeaders
import com.induce.gatewayservice.security.isSafePath
import com.induce.gatewayservice.service.JwtService
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val accessPolicy: AccessPolicy,
) : GlobalFilter, Ordered {
    private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)

    private sealed interface AuthResult {
        data class Success(val userId: String, val role: Role) : AuthResult
        data class Failure(val status: HttpStatus, val message: String) : AuthResult
    }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.pathWithinApplication()
        val method = exchange.request.method

        if (!isSafePath(path)) {
            log.warn("Отклонён подозрительный путь: {} {}", method, exchange.request.path)
            return exchange.writeError(HttpStatus.BAD_REQUEST, "Некорректный путь запроса")
        }

        val request = stripAuthHeaders(exchange.request)

        if (accessPolicy.isOpen(path)) {
            log.debug("Доступ разрешен (open endpoint): {}", path)
            return chain.filter(exchange.mutate().request(request).build())
        }

        return when (val auth = authenticate(request)) {
            is AuthResult.Failure -> exchange.writeError(auth.status, auth.message)

            is AuthResult.Success -> {
                if (!accessPolicy.isAllowed(path, method, auth.role)) {
                    log.info("Доступ запрещён: {} {} для роли {} (userId={})", method, path, auth.role, auth.userId)
                    return exchange.writeError(HttpStatus.FORBIDDEN, "Недостаточно прав")
                }
                chain.filter(exchange.mutate().request(withAuthHeaders(request, auth)).build())
            }
        }
    }

    private fun stripAuthHeaders(request: ServerHttpRequest): ServerHttpRequest =
        request.mutate()
            .headers {
                it.remove(AuthHeaders.USER_ID)
                it.remove(AuthHeaders.USER_ROLE)
            }
            .build()

    private fun authenticate(request: ServerHttpRequest): AuthResult {
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return AuthResult.Failure(HttpStatus.UNAUTHORIZED, "Нужен заголовок Authorization: Bearer <token>")
        }

        val claims = jwtService.getClaims(authHeader.substring(7))
            ?: return AuthResult.Failure(HttpStatus.UNAUTHORIZED, "Токен недействителен или истёк")

        val userId = jwtService.extractUserId(claims)
        val roleName = jwtService.extractRole(claims)
        if (userId == null || roleName == null) {
            return AuthResult.Failure(HttpStatus.UNAUTHORIZED, "В токене нет id пользователя или роли")
        }

        val role = Role.entries.find { it.name == roleName }
        if (role == null) {
            log.warn("Неизвестная роль в токене: userId={}, role={}", userId, roleName)
            return AuthResult.Failure(HttpStatus.FORBIDDEN, "Неизвестная роль")
        }

        return AuthResult.Success(userId, role)
    }

    private fun withAuthHeaders(request: ServerHttpRequest, auth: AuthResult.Success): ServerHttpRequest =
        request.mutate()
            .header(AuthHeaders.USER_ID, auth.userId)
            .header(AuthHeaders.USER_ROLE, auth.role.name)
            .build()

    override fun getOrder(): Int = -2
}
