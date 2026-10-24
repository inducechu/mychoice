package com.induce.gatewayservice.config

import com.induce.gatewayservice.security.AuthHeaders
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Mono

@Configuration
class RateLimitConfig {

    @Bean
    fun userOrIpKeyResolver() = KeyResolver { exchange ->
        val userId = exchange.request.headers.getFirst(AuthHeaders.USER_ID)

        val ip = exchange.request.remoteAddress?.address?.hostAddress ?: "unknown"

        Mono.just(if (userId != null) "user:$userId" else "ip:$ip")
    }
}
