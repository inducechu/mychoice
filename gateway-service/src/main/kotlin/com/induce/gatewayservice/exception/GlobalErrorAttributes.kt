package com.induce.gatewayservice.exception

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {

    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any?> {
        val map = super.getErrorAttributes(request, options)

        val throwable = getError(request)
        map["message"] = throwable.message ?: "Unknown error"
        map.remove("path")
        return map
    }
}