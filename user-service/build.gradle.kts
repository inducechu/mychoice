plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

group = "com.induce"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation(project(":common-proto"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    implementation(libs.spring.grpc.server.starter)

    implementation(libs.grpc.services)
    implementation(libs.grpc.netty.shaded)
    modules {
        module("io.grpc:grpc-netty") {
            replacedBy("io.grpc:grpc-netty-shaded", "Use Netty shaded instead of regular Netty")
        }
    }

    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)

    runtimeOnly(libs.postgresql)

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.spring.grpc.test)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom(libs.spring.grpc.dependencies.get().toString())
        mavenBom(libs.spring.cloud.dependencies.get().toString())
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
