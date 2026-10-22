package com.induce.userservice.config

import com.induce.universityservice.grpc.UniversityInternalServiceGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.grpc.client.GrpcChannelFactory

@Configuration
class GrpcClientConfig {

    @Bean
    fun universityStub(channels: GrpcChannelFactory): UniversityInternalServiceGrpc.UniversityInternalServiceBlockingStub =
        UniversityInternalServiceGrpc.newBlockingStub(channels.createChannel("university-service"))
}
