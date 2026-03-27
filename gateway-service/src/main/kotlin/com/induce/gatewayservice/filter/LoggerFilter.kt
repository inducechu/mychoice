package com.induce.gatewayservice.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingFilter : GlobalFilter, Ordered {

    private val logger = LoggerFactory.getLogger(LoggingFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val startTime = System.currentTimeMillis()
        val path = exchange.request.path.value()
        val method = exchange.request.method

        logger.info(">>> Incoming request: $method $path")

        return chain.filter(exchange).then(Mono.fromRunnable {
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            val statusCode = exchange.response.statusCode

            logger.info("<<< Outgoing response: $path | Status: $statusCode | Duration: ${duration}ms")
        })
    }

    override fun getOrder(): Int = -1
}