package com.induce.universityservice.controller

import com.induce.universityservice.dto.FacultyShortResponse
import com.induce.universityservice.dto.UniversityResponse
import com.induce.universityservice.service.UniversityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/universities")
class UniversityController(
    private val universityService: UniversityService
) {

    @GetMapping("/{id}")
    fun getUniversity(
        @PathVariable id: Long
    ): UniversityResponse {
        return universityService.getUniversity(id)
    }

    @GetMapping("/{id}/faculties")
    fun getFaculties(
        @PathVariable id: Long
    ): List<FacultyShortResponse> {
        return universityService.getFaculties(id)
    }
}
