package com.induce.universityservice.repository

import com.induce.universityservice.model.University
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByCode(code: String): University?

    @EntityGraph(attributePaths = ["city"])
    override fun findAll(pageable: Pageable): Page<University>

    @EntityGraph(attributePaths = ["city"])
    fun findWithCityById(id: Long): University?
}
