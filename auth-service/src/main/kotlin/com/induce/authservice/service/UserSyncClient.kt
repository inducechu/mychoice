package com.induce.authservice.service

import com.induce.authservice.dto.RegisterRequest
import com.induce.userservice.grpc.SyncUserRequest
import com.induce.userservice.grpc.UserServiceGrpc
import org.springframework.stereotype.Service

@Service
class UserSyncClient(
    private val userServiceStub: UserServiceGrpc.UserServiceBlockingStub
) {
    fun sendUserToProfile(request: RegisterRequest, externalId: String) {
        val grpcRequest = SyncUserRequest.newBuilder()
            .setExternalId(externalId)
            .setEmail(request.email)
            .setRole(request.role?.name ?: "ABITURIENT")
            .setFirstName(request.firstName)
            .setLastName(request.lastName)
            .setAge(request.age)
            .setCity(request.city)
            .build()

        userServiceStub.syncUser(grpcRequest)
    }
}
