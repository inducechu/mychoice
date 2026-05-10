package com.induce.reviewservice.grpc

import com.induce.reviewservice.repository.ReviewRepository
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class ReviewGrpcServiceImpl(
    private val reviewRepository: ReviewRepository
) : ReviewInternalServiceGrpc.ReviewInternalServiceImplBase() {

    private val logger = LoggerFactory.getLogger(ReviewGrpcServiceImpl::class.java)

    // m - константа для Bayesian Average (минимальное кол-во голосов для доверия)
    private val m = 5.0

    override fun getUpdatedRatings(
        request: Empty,
        responseObserver: StreamObserver<RatingList>
    ) {
        try {
            logger.info("Received request to fetch updated ratings for all programs")

            val globalAverage = reviewRepository.getGlobalAverageScore() ?: 0.0

            val allStats = reviewRepository.getAllProgramsStats()

            val ratingListBuilder = RatingList.newBuilder()

            allStats.forEach { stat ->
                val programId = stat[0] as Long
                val v = (stat[1] as Long).toDouble()
                val r = (stat[2] as Double)

                val bayesianRating = (v * r + m * globalAverage) / (v + m)

                ratingListBuilder.addRatings(
                    RatingEntry.newBuilder()
                        .setProgramId(programId)
                        .setBayesianRating(bayesianRating)
                        .build()
                )
            }

            val response = ratingListBuilder.build()

            logger.info("Successfully calculated ratings for ${response.ratingsCount} programs")

            responseObserver.onNext(response)
            responseObserver.onCompleted()

        } catch (e: Exception) {
            logger.error("Error during getUpdatedRatings: ${e.message}", e)
            responseObserver.onError(
                io.grpc.Status.INTERNAL
                    .withDescription("Failed to calculate ratings: ${e.message}")
                    .asRuntimeException()
            )
        }
    }
}
