package com.induce.gatewayservice.security

import com.induce.common.model.Role
import com.induce.gatewayservice.config.SecurityConfigProps
import com.induce.gatewayservice.model.AccessRule
import com.induce.gatewayservice.model.DefaultAccess
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpMethod
import org.springframework.http.server.PathContainer
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccessPolicyTest {

    private fun rule(path: String, methods: Set<String> = emptySet(), roles: Set<Role> = emptySet()) =
        AccessRule().apply { this.path = path; this.methods = methods; this.roles = roles }

    private fun policy(default: DefaultAccess = DefaultAccess.AUTHENTICATED) = AccessPolicy(SecurityConfigProps().apply {
        openEndpoints = listOf("/api/auth/login")
        defaultAccess = default
        rules = listOf(
            rule("/api/universities/**", methods = setOf("get")),
            rule("/api/universities/**", roles = setOf(Role.MODERATOR, Role.ADMIN)),
        )
    })

    private fun p(path: String) = PathContainer.parsePath(path)

    @Test
    @DisplayName("читать каталог может любая роль")
    fun anyRoleCanReadCatalog() {
        Role.entries.forEach { assertTrue(policy().isAllowed(p("/api/universities/1"), HttpMethod.GET, it)) }
        assertTrue(policy().isAllowed(p("/api/universities"), HttpMethod.GET, Role.ABITURIENT))
    }

    @Test
    @DisplayName("менять каталог могут только модератор и админ")
    fun onlyModeratorAndAdminCanEditCatalog() {
        assertFalse(policy().isAllowed(p("/api/universities"), HttpMethod.POST, Role.STUDENT))
        assertFalse(policy().isAllowed(p("/api/universities/1"), HttpMethod.DELETE, Role.TEACHER))
        assertTrue(policy().isAllowed(p("/api/universities"), HttpMethod.POST, Role.MODERATOR))
        assertTrue(policy().isAllowed(p("/api/universities/1"), HttpMethod.DELETE, Role.ADMIN))
    }

    @Test
    @DisplayName("путь без правила зависит от default-access")
    fun pathWithoutRuleUsesDefaultAccess() {
        assertTrue(policy().isAllowed(p("/api/users/me"), HttpMethod.GET, Role.STUDENT))
        assertFalse(policy(DefaultAccess.DENY).isAllowed(p("/api/users/me"), HttpMethod.GET, Role.ADMIN))
    }

    @Test
    @DisplayName("открытые эндпоинты")
    fun openEndpoints() {
        assertTrue(policy().isOpen(p("/api/auth/login")))
        assertFalse(policy().isOpen(p("/api/auth/me")))
    }

    @Test
    @DisplayName("опечатка в методе роняет старт")
    fun typoInMethodFailsStartup() {
        assertThrows<IllegalStateException> {
            AccessPolicy(SecurityConfigProps().apply { rules = listOf(rule("/api/**", methods = setOf("GTE"))) })
        }
    }

    @Test
    @DisplayName("подозрительные пути")
    fun suspiciousPaths() {
        assertTrue(isSafePath(p("/api/universities/1")))
        assertTrue(isSafePath(p("/api/universities/")))
        assertFalse(isSafePath(p("//api/universities")))
        assertFalse(isSafePath(p("/api//universities")))
        assertFalse(isSafePath(p("/api/users/me/../../universities")))
        assertFalse(isSafePath(p("/api/universities;x=1")))
        assertFalse(isSafePath(p("/api/users%2F..%2Funiversities")))
    }
}
