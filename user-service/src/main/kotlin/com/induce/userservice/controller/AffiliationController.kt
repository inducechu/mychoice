package com.induce.userservice.controller

import com.induce.userservice.dto.AffiliationRequest
import com.induce.userservice.dto.AffiliationResponse
import com.induce.userservice.service.AffiliationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users/me/affiliations")
class AffiliationController(
    private val affiliationService: AffiliationService
) {

    @GetMapping
    fun getMine(
        @RequestHeader("X-Auth-User-Id") userId: UUID
    ): ResponseEntity<List<AffiliationResponse>> =
        ResponseEntity.ok(affiliationService.getMyAffiliations(userId))

    @PostMapping
    fun add(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @Valid @RequestBody request: AffiliationRequest
    ): ResponseEntity<AffiliationResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(affiliationService.add(userId, request))

    @PutMapping("/{id}")
    fun update(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @PathVariable id: Long,
        @Valid @RequestBody request: AffiliationRequest
    ): ResponseEntity<AffiliationResponse> =
        ResponseEntity.ok(affiliationService.update(userId, id, request))

    @DeleteMapping("/{id}")
    fun delete(
        @RequestHeader("X-Auth-User-Id") userId: UUID,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        affiliationService.delete(userId, id)
        return ResponseEntity.noContent().build()
    }
}
