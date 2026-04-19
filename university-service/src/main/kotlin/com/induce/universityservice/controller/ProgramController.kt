package com.induce.universityservice.controller

import com.induce.universityservice.dto.ProgramResponse
import com.induce.universityservice.service.ProgramService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/programs")
class ProgramController(
    private val programService: ProgramService
) {
    @GetMapping("/{id}")
    fun getProgram(
        @PathVariable id: Long
    ): ProgramResponse {
        return programService.getProgram(id)
    }
}
