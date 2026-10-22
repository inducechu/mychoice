package com.induce.universityservice.repository

import com.induce.universityservice.model.University
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByCode(code: String): University?

    fun findAllByOrderByNameAsc(pageable: Pageable): List<University>
}
