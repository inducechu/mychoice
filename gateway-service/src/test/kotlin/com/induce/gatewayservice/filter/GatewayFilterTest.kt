package com.induce.gatewayservice.filter

import com.induce.common.model.Role
import com.induce.gatewayservice.model.AccessRule
import com.induce.gatewayservice.config.SecurityConfigProps
import com.induce.gatewayservice.security.AccessPolicy
import com.induce.gatewayservice.security.AuthHeaders
import com.induce.gatewayservice.service.JwtService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpStatus
import org.springframework.mock.http.server.reactive.MockServerHttpRequest
import org.springframework.mock.web.server.MockServerWebExchange
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GatewayFiltersTest {
    private val secret = "test-secret-test-secret-test-secret-test-secret"
    private val jwtFilter = JwtAuthenticationFilter(
        JwtService(secret),
        AccessPolicy(SecurityConfigProps().apply {
            openEndpoints = listOf("/api/auth/login")
            rules = listOf(
                AccessRule().apply { path = "/api/universities/**"; methods = setOf("GET") },
                AccessRule().apply { path = "/api/universities/**"; roles = setOf(Role.MODERATOR, Role.ADMIN) },
            )
        }),
    )

    private class CapturingChain : GatewayFilterChain {
        var passed: ServerWebExchange? = null
        override fun filter(exchange: ServerWebExchange): Mono<Void> { passed = exchange; return Mono.empty() }
    }

    private fun token(role: String) = Jwts.builder().subject("user-42").claim("role", role)
        .signWith(Keys.hmacShaKeyFor(secret.toByteArray())).compact()

    private fun run(request: MockServerHttpRequest.BaseBuilder<*>): Pair<MockServerWebExchange, CapturingChain> {
        val exchange = MockServerWebExchange.from(request)
        val chain = CapturingChain()
        jwtFilter.filter(exchange, chain).block()
        return exchange to chain
    }

    @Test
    @DisplayName("без токена 401")
    fun noToken401() {
        val (exchange, chain) = run(MockServerHttpRequest.get("/api/universities"))
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.response.statusCode)
        assertNull(chain.passed)
    }

    @Test
    @DisplayName("студент не может создать вуз")
    fun studentCannotCreateUniversity() {
        val (exchange, chain) = run(MockServerHttpRequest.post("/api/universities").header("Authorization", "Bearer ${token("STUDENT")}"))
        assertEquals(HttpStatus.FORBIDDEN, exchange.response.statusCode)
        assertNull(chain.passed)
    }

    @Test
    @DisplayName("модератор может, и в сервис уходят заголовки из токена")
    fun moderatorAllowedAndHeadersFromToken() {
        val (_, chain) = run(
            MockServerHttpRequest.post("/api/universities")
                .header("Authorization", "Bearer ${token("MODERATOR")}")
                .header(AuthHeaders.USER_ID, "hacker")
        )
        val headers = chain.passed!!.request.headers
        assertEquals(listOf("user-42"), headers[AuthHeaders.USER_ID])
        assertEquals(listOf("MODERATOR"), headers[AuthHeaders.USER_ROLE])
    }

    @Test
    @DisplayName("на открытом эндпоинте поддельные заголовки вырезаются")
    fun spoofedHeadersStrippedOnOpenEndpoint() {
        val (_, chain) = run(
            MockServerHttpRequest.post("/api/auth/login")
                .header(AuthHeaders.USER_ID, "admin-id")
                .header(AuthHeaders.USER_ROLE, "ADMIN")
        )
        val headers = chain.passed!!.request.headers
        assertNull(headers.getFirst(AuthHeaders.USER_ID))
        assertNull(headers.getFirst(AuthHeaders.USER_ROLE))
    }

    @Test
    @DisplayName("неизвестная роль 403")
    fun unknownRole403() {
        val (exchange, _) = run(MockServerHttpRequest.get("/api/universities").header("Authorization", "Bearer ${token("HACKER")}"))
        assertEquals(HttpStatus.FORBIDDEN, exchange.response.statusCode)
    }

    @Test
    @DisplayName("обход через ;параметры в пути 400")
    fun matrixParamsBypass400() {
        val (exchange, chain) = run(MockServerHttpRequest.post("/api/universities;x=1").header("Authorization", "Bearer ${token("STUDENT")}"))
        assertEquals(HttpStatus.BAD_REQUEST, exchange.response.statusCode)
        assertNull(chain.passed)
    }
}
