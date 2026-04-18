package com.induce.universityservice.service

import com.induce.universityservice.dto.FacultyShortResponse
import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.UniversityRepository
import org.springframework.stereotype.Service

@Service
class UniversityService(
    private val universityRepository: UniversityRepository,
    private val facultyRepository: FacultyRepository
) {

    fun getUniversity(id: Long): UniversityResponse {
        val entity = universityRepository.findById(id)
            .orElseThrow { RuntimeException("University not found") }

        return UniversityResponse(
            id = entity.id!!,
            code = entity.code,
            name = entity.name,
            city = entity.city,
            description = entity.description,
            rating = entity.rating.toString()
        )
    }

    fun getFaculties(universityId: Long): List<FacultyShortResponse> {
        val faculties = facultyRepository.findByUniversityId(universityId)

        return faculties.map {
            FacultyShortResponse(
                id = it.id!!,
                name = it.name,
                rating = it.rating.toString()
            )
        }
    }
}
