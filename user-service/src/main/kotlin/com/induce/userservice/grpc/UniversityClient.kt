package com.induce.userservice.grpc

import com.induce.universityservice.grpc.AffiliationCheckResult
import com.induce.universityservice.grpc.UniversityInternalServiceGrpc
import com.induce.universityservice.grpc.ValidateAffiliationRequest
import com.induce.userservice.exception.ServiceUnavailableException
import io.grpc.StatusRuntimeException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class UniversityClient(
    private val stub: UniversityInternalServiceGrpc.UniversityInternalServiceBlockingStub
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun checkAffiliation(universityId: Long, facultyId: Long?, programId: Long?): AffiliationCheckResult {
        val request = ValidateAffiliationRequest.newBuilder()
            .setUniversityId(universityId)
            .apply {
                facultyId?.let { setFacultyId(it) }
                programId?.let { setProgramId(it) }
            }
            .build()

        return try {
            stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                .validateAffiliation(request)
                .result
        } catch (e: StatusRuntimeException) {
            log.warn("university-service call failed: {}", e.status)
            throw ServiceUnavailableException("University service is unavailable, try again later")
        }
    }
}
