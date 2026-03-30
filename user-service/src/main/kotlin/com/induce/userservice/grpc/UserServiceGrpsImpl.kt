package com.induce.userservice.grpc

import com.induce.userservice.model.UserProfile
import com.induce.userservice.repository.UserRepository
import io.grpc.stub.StreamObserver
import org.springframework.grpc.server.service.GrpcService

@GrpcService
class UserServiceGrpcImpl(
    private val userRepository: UserRepository
) : UserServiceGrpc.UserServiceImplBase() {

    override fun syncUser(
        request: SyncUserRequest,
        responseObserver: StreamObserver<SyncUserResponse>
    ) {
        val existingUser = userRepository.findByExternalId(request.externalId)

        val userToSave = existingUser?.apply {
            email = request.email
        } ?: UserProfile(
            externalId = request.externalId,
            username = "user_${request.externalId.take(8)}",
            email = request.email,
            firstName = request.firstName,
            lastName = request.lastName,
            age = request.age,
            city = request.city
        )

        val savedUser = userRepository.save(userToSave)

        val response = SyncUserResponse.newBuilder()
            .setInternalId(savedUser.id!!)
            .setSuccess(true)
            .setMessage("Profile synced")
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}
