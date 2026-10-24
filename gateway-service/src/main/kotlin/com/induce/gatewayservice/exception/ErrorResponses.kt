package com.induce.gatewayservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.time.OffsetDateTime

fun ServerWebExchange.writeError(status: HttpStatus, message: String): Mono<Void> {
    response.statusCode = status
    response.headers.contentType = MediaType.APPLICATION_JSON

    val body = """{"timestamp":"${OffsetDateTime.now()}","status":${status.value()},"error":"${status.reasonPhrase}","message":"$message"}"""
    val buffer = response.bufferFactory().wrap(body.toByteArray(Charsets.UTF_8))
    return response.writeWith(Mono.just(buffer))
}
