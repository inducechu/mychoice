package com.induce.universityservice.repository

import com.induce.universityservice.model.Program
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProgramRepository : JpaRepository<Program, Long> {

    @Query("""
        SELECT p FROM Program p
        JOIN FETCH p.direction
        WHERE p.faculty.id = :facultyId
    """)
    fun findWithDirectionByFacultyId(facultyId: Long): List<Program>

    fun findWithDirectionById(id: Long): Program?

    @Query("""
    SELECT p FROM Program p
    JOIN FETCH p.direction
    WHERE p.id = :id
""")
    fun findDetailedById(id: Long): Program?
}
