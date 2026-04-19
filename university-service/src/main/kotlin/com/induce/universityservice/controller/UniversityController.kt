package com.induce.universityservice.controller

import com.induce.universityservice.dto.CreateFacultyRequest
import com.induce.universityservice.dto.CreateUniversityRequest
import com.induce.universityservice.dto.FacultyResponse
import com.induce.universityservice.dto.FacultyShortResponse
import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.service.FacultyService
import com.induce.universityservice.service.UniversityService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/universities")
@Tag(name = "Universities", description = "Операции с университетами и их факультетами")
class UniversityController(
    private val universityService: UniversityService,
    private val facultyService: FacultyService,
) {

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить университет",
        description = "Возвращает подробную информацию об университете"
    )
    fun getUniversity(
        @Parameter(description = "ID университета", example = "1")
        @PathVariable id: Long
    ): UniversityResponse {
        return universityService.getUniversity(id)
    }

    @GetMapping("/{id}/faculties")
    @Operation(
        summary = "Получить факультеты университета",
        description = "Возвращает список факультетов, принадлежащих университету"
    )
    fun getFaculties(
        @Parameter(description = "ID университета", example = "1")
        @PathVariable id: Long
    ): List<FacultyShortResponse> {
        return universityService.getFaculties(id)
    }

    @PostMapping
    @Operation(
        summary = "Создать университет",
        description = "Создаёт новый университет"
    )
    fun createUniversity(
        @RequestBody @Valid request: CreateUniversityRequest
    ): UniversityResponse {
        return universityService.createUniversity(request)
    }

    @PostMapping("/{universityId}/faculties")
    @Operation(
        summary = "Создать факультет",
        description = "Создаёт новый факультет внутри указанного университета"
    )
    fun createFaculty(
        @Parameter(description = "ID университета", example = "1")
        @PathVariable universityId: Long,

        @RequestBody @Valid request: CreateFacultyRequest
    ): FacultyResponse {
        return facultyService.createFaculty(universityId, request)
    }
}
