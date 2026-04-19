package com.induce.universityservice.repository

import com.induce.universityservice.model.Direction
import org.springframework.data.jpa.repository.JpaRepository

interface DirectionRepository : JpaRepository<Direction, Long> {

    fun findByCode(code: String): Direction?
}
