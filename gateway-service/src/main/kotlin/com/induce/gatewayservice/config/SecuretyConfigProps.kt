package com.induce.gatewayservice.config

import com.induce.gatewayservice.model.AccessRule
import com.induce.gatewayservice.model.DefaultAccess
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.security")
class SecurityConfigProps {
    var openEndpoints: List<String> = mutableListOf()

    var defaultAccess: DefaultAccess = DefaultAccess.AUTHENTICATED

    var rules: List<AccessRule> = mutableListOf()
}
