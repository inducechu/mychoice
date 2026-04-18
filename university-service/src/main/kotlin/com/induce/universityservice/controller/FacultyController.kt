package com.induce.universityservice.controller

import com.induce.universityservice.dto.FacultyResponse
import com.induce.universityservice.dto.ProgramShortResponse
import com.induce.universityservice.service.FacultyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/university/faculties")
class FacultyController(
    private val service: FacultyService
) {

    @GetMapping("/{id}")
    fun getFaculty(
        @PathVariable id: Long
    ): FacultyResponse {
        return service.getFaculty(id)
    }

    @GetMapping("/{id}/programs")
    fun getPrograms(
        @PathVariable id: Long
    ): List<ProgramShortResponse> {
        return service.getPrograms(id)
    }
}
