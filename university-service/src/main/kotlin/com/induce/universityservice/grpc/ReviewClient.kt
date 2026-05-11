package com.induce.universityservice.grpc

import com.induce.reviewservice.grpc.Empty
import com.induce.reviewservice.grpc.RatingList
import com.induce.reviewservice.grpc.ReviewInternalServiceGrpc
import org.springframework.stereotype.Service

@Service
class ReviewClient(
    private val reviewInternalServiceBlockingStub: ReviewInternalServiceGrpc.ReviewInternalServiceBlockingStub
) {
    /**
     * Запрашивает обновленные рейтинги (Bayesian Average) для всех программ
     */
    fun fetchUpdatedRatings(): RatingList {
        val request = Empty.newBuilder().build()

        return reviewInternalServiceBlockingStub.getUpdatedRatings(request)
    }
}
