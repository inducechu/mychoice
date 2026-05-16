package com.induce.reviewservice.grpc

import com.induce.reviewservice.repository.ProgramStatsRepository
import com.induce.reviewservice.repository.ReviewRepository
import io.grpc.Status.INTERNAL
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class ReviewGrpcServiceImpl(
    private val programStatsRepository: ProgramStatsRepository
) : ReviewInternalServiceGrpc.ReviewInternalServiceImplBase() {

    private val logger = LoggerFactory.getLogger(ReviewGrpcServiceImpl::class.java)
    private val m = 5.0

    override fun getUpdatedRatings(request: Empty, responseObserver: StreamObserver<RatingList>) {
        try {
            logger.info("Fetching updated ratings from pre-calculated stats")

            val allStats = programStatsRepository.findAll()

            val totalScoreAllPrograms = allStats.sumOf { it.totalScoreSum }
            val totalReviewsAllPrograms = allStats.sumOf { it.reviewCount }
            val globalAverage = if (totalReviewsAllPrograms > 0) {
                totalScoreAllPrograms.toDouble() / totalReviewsAllPrograms
            } else 0.0

            val ratingListBuilder = RatingList.newBuilder()

            allStats.forEach { stat ->
                val v = stat.reviewCount.toDouble()
                if (v > 0) {
                    val r = stat.totalScoreSum.toDouble() / v
                    val bayesianRating = (v * r + m * globalAverage) / (v + m)

                    ratingListBuilder.addRatings(
                        RatingEntry.newBuilder()
                            .setProgramId(stat.programId)
                            .setBayesianRating(bayesianRating)
                            .build()
                    )
                }
            }

            responseObserver.onNext(ratingListBuilder.build())
            responseObserver.onCompleted()

        } catch (e: Exception) {
            logger.error("Error during getUpdatedRatings: ${e.message}", e)
            responseObserver.onError(INTERNAL.withDescription(e.message).asRuntimeException())
        }
    }
}
