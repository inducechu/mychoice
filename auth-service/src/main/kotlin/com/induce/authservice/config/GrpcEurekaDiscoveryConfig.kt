package com.induce.authservice.config

import io.grpc.NameResolver
import io.grpc.NameResolverProvider
import io.grpc.NameResolverRegistry
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.context.annotation.Configuration
import java.net.URI

@Configuration
class GrpcEurekaDiscoveryConfig(
    private val discoveryClient: DiscoveryClient
) : NameResolverProvider() {

    @PostConstruct
    fun register() {
        NameResolverRegistry.getDefaultRegistry().register(this)
    }

    @PreDestroy
    fun deregister() {
        NameResolverRegistry.getDefaultRegistry().deregister(this)
    }

    override fun newNameResolver(targetUri: URI, args: NameResolver.Args): NameResolver? {
        return if ("eureka" == targetUri.scheme) {
            EurekaNameResolver(targetUri, discoveryClient)
        } else {
            null
        }
    }

    override fun isAvailable(): Boolean = true

    override fun priority(): Int = 6

    override fun getDefaultScheme(): String = "eureka"
}
