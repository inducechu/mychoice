package com.induce.userservice.grpc

import com.induce.userservice.model.UserProfile
import com.induce.userservice.repository.UserRepository
import io.grpc.stub.StreamObserver
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class UserServiceGrpcImpl(
    private val userRepository: UserRepository
) : UserServiceGrpc.UserServiceImplBase() {

    private val logger = org.slf4j.LoggerFactory.getLogger(UserServiceGrpcImpl::class.java)

    override fun syncUser(
        request: SyncUserRequest,
        responseObserver: StreamObserver<SyncUserResponse>
    ) {
        try {
            logger.info("Received sync request for externalId: ${request.externalId}")

            val safeExternalId = if (request.externalId.isNullOrBlank()) "unknown" else request.externalId

            val existingUser = userRepository.findByExternalId(safeExternalId)

            val userToSave = existingUser?.apply {
                email = request.email
            } ?: UserProfile(
                externalId = safeExternalId,
                username = "user_${safeExternalId.take(8)}",
                email = request.email,
                firstName = request.firstName,
                lastName = request.lastName,
                age = request.age,
                city = request.city
            )

            val savedUser = userRepository.save(userToSave)

            val response = SyncUserResponse.newBuilder()
                .setInternalId(savedUser.id ?: 0L)
                .setSuccess(true)
                .setMessage("Profile synced successfully")
                .build()

            responseObserver.onNext(response)
            responseObserver.onCompleted()

        } catch (e: Exception) {
            logger.error("Error during syncUser: ${e.message}", e)
            responseObserver.onError(
                io.grpc.Status.INTERNAL
                    .withDescription("Internal server error during sync: ${e.message}")
                    .asRuntimeException()
            )
        }
    }
}
