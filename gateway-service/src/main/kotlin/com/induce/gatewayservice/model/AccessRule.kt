package com.induce.gatewayservice.model

import com.induce.common.model.Role

class AccessRule {
    var path: String = ""
    var methods: Set<String> = emptySet()
    var roles: Set<Role> = emptySet()
}
