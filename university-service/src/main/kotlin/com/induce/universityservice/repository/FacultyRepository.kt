package com.induce.universityservice.repository

import com.induce.universityservice.model.Faculty
import org.springframework.data.jpa.repository.JpaRepository

interface FacultyRepository : JpaRepository<Faculty, Long> {

    fun findByUniversityId(universityId: Long): List<Faculty>
}
