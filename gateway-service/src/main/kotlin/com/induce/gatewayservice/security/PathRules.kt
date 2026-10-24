package com.induce.gatewayservice.security

import org.springframework.http.HttpMethod
import org.springframework.http.server.PathContainer
import org.springframework.web.util.pattern.PathPattern
import org.springframework.web.util.pattern.PathPatternParser

private val parser = PathPatternParser.defaultInstance

fun parsePath(pattern: String): PathPattern = parser.parse(pattern)

fun parseMethods(methods: Set<String>, where: String): Set<HttpMethod> =
    methods.map { name ->
        HttpMethod.values().find { it.name() == name.trim().uppercase() }
            ?: throw IllegalStateException("$where: неизвестный HTTP-метод '$name'")
    }.toSet()

fun Set<HttpMethod>.allows(method: HttpMethod) = isEmpty() || method in this

fun isSafePath(path: PathContainer): Boolean {
    val elements = path.elements()
    return elements.withIndex().all { (i, element) ->
        when (element) {
            is PathContainer.PathSegment -> {
                val value = element.valueToMatch()
                value.isNotEmpty() && value != "." && value != ".." &&
                        !value.contains('/') && !value.contains('\\') &&
                        element.parameters().isEmpty()
            }
            else -> i == 0 || elements[i - 1] is PathContainer.PathSegment
        }
    }
}
