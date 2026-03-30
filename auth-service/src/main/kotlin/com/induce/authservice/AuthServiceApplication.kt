package com.induce.authservice

import com.induce.userservice.grpc.UserServiceGrpc
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.grpc.client.ImportGrpcClients

@SpringBootApplication
@ImportGrpcClients(basePackageClasses = [UserServiceGrpc::class])
class AuthServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}
