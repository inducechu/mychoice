package com.induce.universityservice.service

import com.induce.universityservice.dto.CreateUniversityRequest
import com.induce.universityservice.dto.FacultyShortResponse
import com.induce.universityservice.dto.PageResponse
import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.exception.EntityAlreadyExistsException
import com.induce.universityservice.mapper.toResponse
import com.induce.universityservice.model.University
import com.induce.universityservice.repository.CityRepository
import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.UniversityRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UniversityService(
    private val universityRepository: UniversityRepository,
    private val facultyRepository: FacultyRepository,
    private val cityRepository: CityRepository
) {

    fun getUniversity(id: Long): UniversityResponse =
        (universityRepository.findWithCityById(id)
            ?: throw NoSuchElementException("University $id not found"))
            .toResponse()

    fun getFaculties(universityId: Long): List<FacultyShortResponse> =
        facultyRepository.findByUniversityId(universityId)
            .map { FacultyShortResponse(id = it.id!!, name = it.name) }

    @Transactional
    fun createUniversity(request: CreateUniversityRequest): UniversityResponse {
        if (universityRepository.findByCode(request.code) != null) {
            throw EntityAlreadyExistsException("University with code ${request.code} already exists")
        }
        val cityId = requireNotNull(request.cityId)
        val city = cityRepository.findById(cityId)
            .orElseThrow { NoSuchElementException("City $cityId not found") }

        val university = University(
            code = request.code,
            name = request.name,
            city = city,
            description = request.description
        )
        return universityRepository.save(university).toResponse()
    }

    fun getAllUniversities(page: Int, size: Int): PageResponse<UniversityResponse> {
        val pageable = PageRequest.of(page, size, Sort.by("name", "id"))
        val result = universityRepository.findAll(pageable)
        return PageResponse(
            content = result.content.map { it.toResponse() },
            page = result.number,
            size = result.size,
            totalElements = result.totalElements,
            totalPages = result.totalPages
        )
    }

}
