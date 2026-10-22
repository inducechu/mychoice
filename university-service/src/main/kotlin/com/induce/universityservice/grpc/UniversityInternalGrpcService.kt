package com.induce.universityservice.grpc

import com.induce.universityservice.repository.FacultyRepository
import com.induce.universityservice.repository.ProgramRepository
import com.induce.universityservice.repository.UniversityRepository
import io.grpc.stub.StreamObserver
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class UniversityInternalGrpcService(
    private val universityRepository: UniversityRepository,
    private val facultyRepository: FacultyRepository,
    private val programRepository: ProgramRepository,
) : UniversityInternalServiceGrpc.UniversityInternalServiceImplBase() {

    override fun validateAffiliation(
        request: ValidateAffiliationRequest,
        responseObserver: StreamObserver<ValidateAffiliationResponse>
    ) {
        val response = ValidateAffiliationResponse.newBuilder()
            .setResult(check(request))
            .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    private fun check(request: ValidateAffiliationRequest): AffiliationCheckResult {
        if (!universityRepository.existsById(request.universityId)) {
            return AffiliationCheckResult.UNIVERSITY_NOT_FOUND
        }
        if (request.hasFacultyId()) {
            if (!facultyRepository.existsById(request.facultyId)) {
                return AffiliationCheckResult.FACULTY_NOT_FOUND
            }
            if (!facultyRepository.existsByIdAndUniversityId(request.facultyId, request.universityId)) {
                return AffiliationCheckResult.FACULTY_NOT_IN_UNIVERSITY
            }
        }
        if (request.hasProgramId()) {
            if (!programRepository.existsById(request.programId)) {
                return AffiliationCheckResult.PROGRAM_NOT_FOUND
            }
            if (!programRepository.existsByIdAndFacultyId(request.programId, request.facultyId)) {
                return AffiliationCheckResult.PROGRAM_NOT_IN_FACULTY
            }
        }
        return AffiliationCheckResult.OK
    }
}
