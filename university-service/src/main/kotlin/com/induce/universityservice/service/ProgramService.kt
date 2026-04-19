package com.induce.universityservice.service

import com.induce.universityservice.dto.CreateProgramRequest
import com.induce.universityservice.dto.DirectionResponse
import com.induce.universityservice.dto.ProgramResponse
import com.induce.universityservice.model.Program
import com.induce.universityservice.repository.DirectionRepository
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.ProgramRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProgramService(
    private val programRepository: ProgramRepository,
    private val facultyRepository: FacultyRepository,
    private val directionRepository: DirectionRepository
) {

    fun getProgram(id: Long): ProgramResponse {
        val program = programRepository.findDetailedById(id)
            ?: throw RuntimeException("Program not found")

        return ProgramResponse(
            id = program.id!!,
            name = program.name,
            description = program.description,
            rating = program.rating.toString(),
            degree = program.degree.name,
            direction = DirectionResponse(
                code = program.direction.code,
                name = program.direction.name
            ),
            facultyId = program.faculty.id!!
        )
    }

    fun createProgram(
        facultyId: Long,
        request: CreateProgramRequest
    ): ProgramResponse {

        val faculty = facultyRepository.findById(facultyId)
            .orElseThrow { RuntimeException("Faculty not found") }

        val direction = directionRepository.findByCode(request.directionCode)
            ?: throw RuntimeException("Direction not found")

        val program = Program(
            direction = direction,
            name = request.name,
            description = request.description,
            rating = BigDecimal.ZERO,
            degree = request.degree,
            faculty = faculty
        )

        val saved = programRepository.save(program)

        return ProgramResponse(
            id = saved.id!!,
            name = saved.name,
            description = saved.description,
            rating = saved.rating.toString(),
            degree = saved.degree.name,
            direction = DirectionResponse(
                code = direction.code,
                name = direction.name
            ),
            facultyId = faculty.id!!
        )
    }
}
