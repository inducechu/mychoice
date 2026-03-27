package com.induce.gatewayservice.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.webflux.error.ErrorAttributes
import org.springframework.boot.autoconfigure.web.WebProperties
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

            val throwable = getError(request)

            val status = when (throwable) {
                is org.springframework.cloud.gateway.support.NotFoundException -> HttpStatus.NOT_FOUND
                is io.jsonwebtoken.ExpiredJwtException -> HttpStatus.UNAUTHORIZED
                is io.jsonwebtoken.security.SignatureException -> HttpStatus.UNAUTHORIZED
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

            ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(map))
        }
    }
}
