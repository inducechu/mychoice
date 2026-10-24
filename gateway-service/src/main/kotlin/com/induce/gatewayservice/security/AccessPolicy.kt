package com.induce.gatewayservice.security

import com.induce.common.model.Role
import com.induce.gatewayservice.config.SecurityConfigProps
import com.induce.gatewayservice.model.DefaultAccess
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.server.PathContainer
import org.springframework.stereotype.Component
import org.springframework.web.util.pattern.PathPattern

@Component
class AccessPolicy(props: SecurityConfigProps) {
    private val log = LoggerFactory.getLogger(AccessPolicy::class.java)

    private class CompiledRule(
        val source: String,
        val path: PathPattern,
        val methods: Set<HttpMethod>,
        val roles: Set<Role>,
    )

    private val openEndpoints = props.openEndpoints.map(::parsePath)
    private val defaultAccess = props.defaultAccess
    private val rules = props.rules.mapIndexed { i, rule ->
        require(rule.path.isNotBlank()) { "app.security.rules[$i]: не задан path" }
        CompiledRule(
            source = "${rule.methods.ifEmpty { setOf("*") }} ${rule.path} -> ${rule.roles.ifEmpty { "любой с токеном" }}",
            path = parsePath(rule.path),
            methods = parseMethods(rule.methods, "app.security.rules[$i]"),
            roles = rule.roles,
        )
    }

    init {
        log.info("Открытые эндпоинты: {}", props.openEndpoints)
        rules.forEach { log.info("Правило доступа: {}", it.source) }
        log.info("Если правило не найдено: {}", defaultAccess)
    }

    fun isOpen(path: PathContainer): Boolean = openEndpoints.any { it.matches(path) }

    fun isAllowed(path: PathContainer, method: HttpMethod, role: Role): Boolean {
        val rule = rules.firstOrNull { it.path.matches(path) && it.methods.allows(method) }
            ?: return defaultAccess == DefaultAccess.AUTHENTICATED

        return rule.roles.isEmpty() || role in rule.roles
    }
}
