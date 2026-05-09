package com.induce.universityservice.service

import com.induce.universityservice.dto.CreateUniversityRequest
import com.induce.universityservice.dto.FacultyShortResponse
import com.induce.universityservice.dto.PageResponse
import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.exception.EntityAlreadyExistsException
import com.induce.universityservice.model.University
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.UniversityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.math.BigDecimal

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
            rating = entity.rating
        )
    }

    fun getFaculties(universityId: Long): List<FacultyShortResponse> {
        val faculties = facultyRepository.findByUniversityId(universityId)

        return faculties.map {
            FacultyShortResponse(
                id = it.id!!,
                name = it.name,
                rating = it.rating
            )
        }
    }

    fun createUniversity(request: CreateUniversityRequest): UniversityResponse {

        if (universityRepository.findByCode(request.code) != null) {
            throw EntityAlreadyExistsException("University with code ${request.code} already exists")
        }

        val university = University(
            code = request.code,
            name = request.name,
            city = request.city,
            description = request.description,
            rating = BigDecimal.ZERO
        )

        val saved = universityRepository.save(university)

        return UniversityResponse(
            id = saved.id!!,
            code = saved.code,
            name = saved.name,
            city = saved.city,
            description = saved.description,
            rating = saved.rating
        )
    }

    fun getAllUniversities(page: Int, size: Int): PageResponse<UniversityResponse> {
        val pageable = PageRequest.of(page, size)
        val result = universityRepository.findAll(pageable)

        return PageResponse(
            content = result.content.map {
                UniversityResponse(
                    id = it.id!!,
                    code = it.code,
                    name = it.name,
                    city = it.city,
                    description = it.description,
                    rating = it.rating
                )
            },
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages
        )
    }
}
