package com.induce.userservice.util

import java.util.UUID

object UsernameGenerator {

    fun generate(): String {
        val suffix = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .take(12)

        return "user_$suffix"
    }
}
