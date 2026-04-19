package com.induce.universityservice.controller

import com.induce.universityservice.dto.CreateProgramRequest
import com.induce.universityservice.dto.FacultyResponse
import com.induce.universityservice.dto.ProgramResponse
import com.induce.universityservice.dto.ProgramShortResponse
import com.induce.universityservice.service.FacultyService
import com.induce.universityservice.service.ProgramService
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
@RequestMapping("/api/faculties")
@Tag(name = "Faculties", description = "Операции с факультетами и их программами")
class FacultyController(
    private val facultyService: FacultyService,
    private val programService: ProgramService
) {

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить факультет",
        description = "Возвращает подробную информацию о факультете, включая университет"
    )
    fun getFaculty(
        @Parameter(description = "ID факультета", example = "1")
        @PathVariable id: Long
    ): FacultyResponse {
        return facultyService.getFaculty(id)
    }

    @GetMapping("/{id}/programs")
    @Operation(
        summary = "Получить программы факультета",
        description = "Возвращает список образовательных программ, принадлежащих факультету"
    )
    fun getPrograms(
        @Parameter(description = "ID факультета", example = "1")
        @PathVariable id: Long
    ): List<ProgramShortResponse> {
        return facultyService.getPrograms(id)
    }

    @PostMapping("/{facultyId}/programs")
    @Operation(
        summary = "Создать программу",
        description = "Создаёт новую образовательную программу внутри указанного факультета"
    )
    fun createProgram(
        @Parameter(description = "ID факультета", example = "1")
        @PathVariable facultyId: Long,

        @RequestBody @Valid request: CreateProgramRequest
    ): ProgramResponse {
        return programService.createProgram(facultyId, request)
    }
}
