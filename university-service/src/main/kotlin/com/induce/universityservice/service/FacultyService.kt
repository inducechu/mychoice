package com.induce.universityservice.service

import com.induce.universityservice.dto.CreateFacultyRequest
import com.induce.universityservice.dto.DirectionResponse
import com.induce.universityservice.dto.FacultyResponse
import com.induce.universityservice.dto.ProgramShortResponse
import com.induce.universityservice.dto.UniversityShortResponse
import com.induce.universityservice.model.Faculty
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.ProgramRepository
import com.induce.universityservice.repository.UniversityRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class FacultyService(
    private val facultyRepository: FacultyRepository,
    private val programRepository: ProgramRepository,
    private val universityRepository: UniversityRepository
) {

    fun getFaculty(id: Long): FacultyResponse {
        val faculty = facultyRepository.findById(id)
            .orElseThrow { RuntimeException("Faculty not found") }

        return FacultyResponse(
            id = faculty.id!!,
            name = faculty.name,
            description = faculty.description,
            rating = faculty.rating,
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
                rating = it.rating,
                degree = it.degree,
                direction = DirectionResponse(
                    code = it.direction.code,
                    name = it.direction.name
                )
            )
        }
    }

    fun createFaculty(
        universityId: Long,
        request: CreateFacultyRequest
    ): FacultyResponse {

        val university = universityRepository.findById(universityId)
            .orElseThrow { RuntimeException("University not found") }

        val faculty = Faculty(
            name = request.name,
            description = request.description,
            rating = BigDecimal.ZERO,
            university = university
        )

        val saved = facultyRepository.save(faculty)

        return FacultyResponse(
            id = saved.id!!,
            name = saved.name,
            description = saved.description,
            rating = saved.rating,
            university = UniversityShortResponse(
                id = saved.university.id!!,
                name = saved.university.name,
                code = saved.university.code
            )
        )
    }
}
