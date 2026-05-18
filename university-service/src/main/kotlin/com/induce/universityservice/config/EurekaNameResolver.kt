package com.induce.universityservice.config

import io.grpc.Attributes
import io.grpc.EquivalentAddressGroup
import io.grpc.NameResolver
import io.grpc.Status
import org.springframework.cloud.client.discovery.DiscoveryClient
import java.net.InetSocketAddress
import java.net.URI

class EurekaNameResolver(
    targetUri: URI,
    private val discoveryClient: DiscoveryClient
) : NameResolver() {

    private val serviceId: String = targetUri.path?.let { path ->
        if (path.startsWith("/")) path.substring(1) else path
    } ?: ""

    private var listener: Listener2? = null

    override fun getServiceAuthority(): String = serviceId

    override fun start(listener: Listener2) {
        this.listener = listener
        resolve()
    }

    override fun refresh() {
        resolve()
    }

    private fun resolve() {
        val currentListener = listener ?: return

        try {
            val instances = discoveryClient.getInstances(serviceId)

            if (instances.isEmpty()) {
                currentListener.onError(
                    Status.UNAVAILABLE.withDescription("No instances found in Eureka for service: $serviceId")
                )
                return
            }

            val addresses = instances.map { instance ->
                val host = instance.host
                val grpcPortStr = instance.metadata!!.getOrDefault("grpc.port", "9090")
                val grpcPort = grpcPortStr.toInt()

                EquivalentAddressGroup(InetSocketAddress(host, grpcPort))
            }

            val result = ResolutionResult.newBuilder()
                .setAddresses(addresses)
                .setAttributes(Attributes.EMPTY)
                .build()

            currentListener.onResult(result)

        } catch (e: Exception) {
            currentListener.onError(
                Status.INTERNAL
                    .withDescription("Failed to resolve Eureka instances for $serviceId")
                    .withCause(e)
            )
        }
    }

    override fun shutdown() {
        // Управляемые Spring ресурсы очищать не нужно
    }
}

