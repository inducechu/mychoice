package com.induce.universityservice

import com.induce.reviewservice.grpc.ReviewInternalServiceGrpc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.grpc.client.ImportGrpcClients
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ImportGrpcClients(basePackageClasses = [ReviewInternalServiceGrpc::class])
class UniversityServiceApplication

fun main(args: Array<String>) {
    runApplication<UniversityServiceApplication>(*args)
}
