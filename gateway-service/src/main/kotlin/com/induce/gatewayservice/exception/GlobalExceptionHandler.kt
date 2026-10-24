package com.induce.gatewayservice.exception

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.webflux.error.ErrorAttributes
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.cloud.gateway.support.NotFoundException
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ResponseStatusException

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: ErrorAttributes,
    resourceProperties: WebProperties,
    applicationContext: ApplicationContext,
    configurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resourceProperties.resources, applicationContext) {

    init {
        this.setMessageWriters(configurer.writers)
        this.setMessageReaders(configurer.readers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all()) { request ->
            val map = getErrorAttributes(request, ErrorAttributeOptions.defaults())

            val status = when (val throwable = getError(request)) {
                is NotFoundException -> HttpStatus.NOT_FOUND
                is ExpiredJwtException -> HttpStatus.UNAUTHORIZED
                is SignatureException -> HttpStatus.UNAUTHORIZED
                is ResponseStatusException -> HttpStatus.valueOf(throwable.statusCode.value())
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

            ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(map))
        }
    }
}
