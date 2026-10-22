package com.induce.universityservice.repository

import com.induce.universityservice.model.City
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Long> {
    fun findByNameContainingIgnoreCase(query: String, pageable: Pageable): List<City>
}
