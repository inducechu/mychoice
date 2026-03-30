package com.induce.authservice.config

import com.induce.userservice.grpc.UserServiceGrpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcClientConfig {

    @Bean
    fun userServiceStub(): UserServiceGrpc.UserServiceBlockingStub {
        val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 8082)
            .usePlaintext() // Используем без TLS для локальной разработки
            .build()

        return UserServiceGrpc.newBlockingStub(channel)
    }
}