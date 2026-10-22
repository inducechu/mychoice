package com.induce.userservice.mapper

import com.induce.userservice.dto.AffiliationRequest
import com.induce.userservice.dto.AffiliationResponse
import com.induce.userservice.model.Affiliation
import com.induce.userservice.model.UserProfile
import java.time.LocalDate

fun Affiliation.toResponse(today: LocalDate = LocalDate.now()) = AffiliationResponse(
    id = requireNotNull(id) { "Affiliation is not persisted yet" },
    universityId = universityId,
    facultyId = facultyId,
    programId = programId,
    enrollmentYear = enrollmentYear,
    course = enrollmentYear?.let { currentCourse(it, today) }
)

fun AffiliationRequest.toEntity(profile: UserProfile) = Affiliation(
    userProfile = profile,
    universityId = requireNotNull(universityId),
    facultyId = facultyId,
    programId = programId,
    enrollmentYear = enrollmentYear
)

fun Affiliation.updateFrom(request: AffiliationRequest) {
    universityId = requireNotNull(request.universityId)
    facultyId = request.facultyId
    programId = request.programId
    enrollmentYear = request.enrollmentYear
}

fun currentCourse(enrollmentYear: Int, today: LocalDate): Int {
    val academicYearStart = if (today.monthValue >= 9) today.year else today.year - 1
    return academicYearStart - enrollmentYear + 1
}
