package com.induce.gatewayservice.filter

import com.induce.gatewayservice.config.SecurityConfigProps
import com.induce.gatewayservice.service.JwtService
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val props: SecurityConfigProps,
) : GlobalFilter, Ordered {
    private val log = org.slf4j.LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    private val pathMatcher = org.springframework.util.AntPathMatcher()

    init {
        log.info("JWT Filter: Загружены эндпоинты: {}", props.openEndpoints)
    }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val path = exchange.request.path.value()

        val isOpen = props.openEndpoints.any { pattern -> pathMatcher.match(pattern, path) }

        if (isOpen) {
            log.debug("Доступ разрешен (open endpoint): {}", path)
            return chain.filter(exchange)
        }

        val authHeader = exchange.request.headers.getFirst("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, HttpStatus.UNAUTHORIZED)
        }

        val token = authHeader.substring(7)

        val claims = jwtService.getClaims(token)

        claims ?: return onError(exchange, HttpStatus.UNAUTHORIZED)

        val userId = jwtService.extractUserId(claims)
        val role = jwtService.extractRole(claims)

        return if (userId != null && role != null) {
            val modifiedRequest = exchange.request.mutate()
                .header("X-Auth-User-Id", userId)
                .header("X-Auth-User-Role", role)
                .build()

            chain.filter(exchange.mutate().request(modifiedRequest).build())
        } else {
            onError(exchange, HttpStatus.UNAUTHORIZED)
        }
    }

    private fun onError(exchange: ServerWebExchange, httpStatus: HttpStatus): Mono<Void> {
        exchange.response.statusCode = httpStatus
        return exchange.response.setComplete()
    }

    override fun getOrder(): Int = -2
}