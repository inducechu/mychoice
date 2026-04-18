package com.induce.universityservice.service

import com.induce.universityservice.dto.DirectionResponse
import com.induce.universityservice.dto.FacultyResponse
import com.induce.universityservice.dto.ProgramShortResponse
import com.induce.universityservice.dto.UniversityShortResponse
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.ProgramRepository
import org.springframework.stereotype.Service

@Service
class FacultyService(
    private val facultyRepository: FacultyRepository,
    private val programRepository: ProgramRepository
) {

    fun getFaculty(id: Long): FacultyResponse {
        val faculty = facultyRepository.findById(id)
            .orElseThrow { RuntimeException("Faculty not found") }

        return FacultyResponse(
            id = faculty.id!!,
            name = faculty.name,
            description = faculty.description,
            rating = faculty.rating.toString(),
            university = UniversityShortResponse(
                id = faculty.university.id!!,
                name = faculty.university.name,
                code = faculty.university.code
            )
        )
    }

    fun getPrograms(facultyId: Long): List<ProgramShortResponse> {
        val programs = programRepository.findWithDirectionByFacultyId(facultyId)

        return programs.map {
            ProgramShortResponse(
                id = it.id!!,
                name = it.name,
                rating = it.rating.toString(),
                degree = it.degree.name,
                direction = DirectionResponse(
                    code = it.direction.code,
                    name = it.direction.name
                )
            )
        }
    }
}
