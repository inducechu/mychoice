package com.induce.universityservice.controller

import com.induce.universityservice.dto.CreateProgramRequest
import com.induce.universityservice.dto.ProgramResponse
import com.induce.universityservice.service.ProgramService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/university/creator")
class CreateController(
    private val programService: ProgramService,
) {
    @PostMapping("/program/{facultyId}")
    fun createProgram(
        @PathVariable facultyId: Long,
        @RequestBody request: CreateProgramRequest
    ): ProgramResponse {
        return programService.createProgram(facultyId, request)
    }
}
