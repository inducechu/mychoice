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

    @Query("SELECT p FROM Program p JOIN FETCH p.direction JOIN FETCH p.faculty WHERE p.id = :id")
    fun findDetailedById(id: Long): Program?

    fun existsByIdAndFacultyId(id: Long, facultyId: Long): Boolean
}
