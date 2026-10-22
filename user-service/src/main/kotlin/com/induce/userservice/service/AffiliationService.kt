package com.induce.userservice.service

import com.induce.universityservice.grpc.AffiliationCheckResult
import com.induce.userservice.dto.AffiliationRequest
import com.induce.userservice.dto.AffiliationResponse
import com.induce.userservice.exception.AffiliationNotFoundException
import com.induce.userservice.exception.BadRequestException
import com.induce.userservice.exception.ConflictException
import com.induce.userservice.exception.ServiceUnavailableException
import com.induce.userservice.exception.UserNotFoundException
import com.induce.userservice.grpc.UniversityClient
import com.induce.userservice.mapper.toEntity
import com.induce.userservice.mapper.toResponse
import com.induce.userservice.mapper.updateFrom
import com.induce.userservice.model.UserProfile
import com.induce.userservice.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.UUID

@Service
class AffiliationService(
    private val repository: UserProfileRepository,
    private val universityClient: UniversityClient
) {

    @Transactional(readOnly = true)
    fun getMyAffiliations(externalId: UUID): List<AffiliationResponse> =
        findProfile(externalId).affiliations.map { it.toResponse() }

    @Transactional
    fun add(externalId: UUID, request: AffiliationRequest): AffiliationResponse {
        val profile = findProfile(externalId)
        validate(request)
        ensureUnique(profile, request, exceptId = null)

        val affiliation = request.toEntity(profile)
        profile.affiliations.add(affiliation)
        repository.flush()
        return affiliation.toResponse()
    }

    @Transactional
    fun update(externalId: UUID, affiliationId: Long, request: AffiliationRequest): AffiliationResponse {
        val profile = findProfile(externalId)
        val affiliation = profile.affiliations.find { it.id == affiliationId }
            ?: throw AffiliationNotFoundException(affiliationId)
        validate(request)
        ensureUnique(profile, request, exceptId = affiliationId)

        affiliation.updateFrom(request)
        return affiliation.toResponse()
    }

    @Transactional
    fun delete(externalId: UUID, affiliationId: Long) {
        val profile = findProfile(externalId)
        if (!profile.affiliations.removeIf { it.id == affiliationId }) {
            throw AffiliationNotFoundException(affiliationId)
        }
    }

    private fun findProfile(externalId: UUID): UserProfile =
        repository.findByExternalId(externalId)
            ?: throw UserNotFoundException("User $externalId not found")

    private fun validate(request: AffiliationRequest) {
        if (request.programId != null && request.facultyId == null) {
            throw BadRequestException("programId requires facultyId")
        }
        request.enrollmentYear?.let {
            if (it > LocalDate.now().year) throw BadRequestException("enrollmentYear cannot be in the future")
        }

        val result = universityClient.checkAffiliation(
            universityId = requireNotNull(request.universityId),
            facultyId = request.facultyId,
            programId = request.programId
        )
        when (result) {
            AffiliationCheckResult.OK -> Unit
            AffiliationCheckResult.UNIVERSITY_NOT_FOUND -> throw BadRequestException("University ${request.universityId} not found")
            AffiliationCheckResult.FACULTY_NOT_FOUND -> throw BadRequestException("Faculty ${request.facultyId} not found")
            AffiliationCheckResult.FACULTY_NOT_IN_UNIVERSITY -> throw BadRequestException("Faculty does not belong to university")
            AffiliationCheckResult.PROGRAM_NOT_FOUND -> throw BadRequestException("Program ${request.programId} not found")
            AffiliationCheckResult.PROGRAM_NOT_IN_FACULTY -> throw BadRequestException("Program does not belong to faculty")
            else -> throw ServiceUnavailableException("Unexpected university check result: $result")
        }
    }

    private fun ensureUnique(profile: UserProfile, request: AffiliationRequest, exceptId: Long?) {
        val duplicate = profile.affiliations.any {
            it.id != exceptId &&
                    it.universityId == request.universityId &&
                    it.facultyId == request.facultyId &&
                    it.programId == request.programId
        }
        if (duplicate) throw ConflictException("Affiliation already exists")
    }
}
