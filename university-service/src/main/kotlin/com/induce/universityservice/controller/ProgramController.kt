package com.induce.universityservice.controller

import com.induce.universityservice.dto.CreateDirectionRequest
import com.induce.universityservice.dto.DirectionResponse
import com.induce.universityservice.dto.ProgramResponse
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
@RequestMapping("/api/programs")
@Tag(name = "Programs", description = "Операции с образовательными программами")
class ProgramController(
    private val programService: ProgramService
) {

    @GetMapping("/{id}")
    @Operation(
        summary = "Получить программу",
        description = "Возвращает полную информацию об образовательной программе, включая направление и факультет"
    )
    fun getProgram(
        @Parameter(description = "ID программы", example = "1")
        @PathVariable id: Long
    ): ProgramResponse {
        return programService.getProgram(id)
    }

    @PostMapping
    @Operation(
        summary = "Создать новое направление подготовки",
        description = "Добавляет уникальное направление (например, бакалавриат/магистратуру) по коду ОКСО"
    )
    fun createDirection(
        @Valid @RequestBody request: CreateDirectionRequest
    ): DirectionResponse {
        return programService.createDirection(request)
    }
}
