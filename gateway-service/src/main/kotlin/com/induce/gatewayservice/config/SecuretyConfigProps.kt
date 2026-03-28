package com.induce.gatewayservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.security")
class SecurityConfigProps {
    var openEndpoints: List<String> = mutableListOf()
}