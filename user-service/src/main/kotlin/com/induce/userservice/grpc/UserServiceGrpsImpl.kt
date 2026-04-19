package com.induce.userservice.grpc

import com.induce.userservice.model.Role
import com.induce.userservice.model.UserProfile
import com.induce.userservice.repository.UserProfileRepository
import com.induce.userservice.util.UsernameGenerator
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import org.springframework.grpc.server.service.GrpcService
import java.util.UUID

@GrpcService
class UserServiceGrpcImpl(
    private val userProfileRepository: UserProfileRepository
) : UserServiceGrpc.UserServiceImplBase() {

    private val logger = LoggerFactory.getLogger(UserServiceGrpcImpl::class.java)

    override fun syncUser(
        request: SyncUserRequest,
        responseObserver: StreamObserver<SyncUserResponse>
    ) {
        try {
            logger.info("Received sync request for externalId: ${request.externalId}")

            val externalId = try {
                UUID.fromString(request.externalId)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid UUID format ${e.message}")
            }

            val existingUser = userProfileRepository.findByExternalId(externalId)

            val userToSave = existingUser?.apply {
                email = request.email
            } ?: UserProfile(
                externalId = externalId,
                username = UsernameGenerator.generate(),
                email = request.email,
                role = Role.valueOf(request.role.uppercase()),
                firstName = request.firstName,
                lastName = request.lastName,
                age = request.age,
                city = request.city
            )

            val savedUser = userProfileRepository.save(userToSave)

            val response = SyncUserResponse.newBuilder()
                .setInternalId(savedUser.id ?: 0L)
                .setSuccess(true)
                .setMessage("Profile synced successfully")
                .build()

            responseObserver.onNext(response)
            responseObserver.onCompleted()

        } catch (e: IllegalArgumentException) {
            logger.error("Error during syncUser: ${e.message}", e)
            responseObserver.onError(
                io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .asRuntimeException()
            )
        }
    }
}
