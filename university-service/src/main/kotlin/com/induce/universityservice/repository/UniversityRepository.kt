package com.induce.universityservice.repository

import com.induce.universityservice.model.University
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UniversityRepository : JpaRepository<University, Long> {
    fun findByCode(code: String): University?

    fun findAllByOrderByNameAsc(pageable: Pageable): List<University>
}
