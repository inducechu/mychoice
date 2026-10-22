import com.google.protobuf.gradle.id

plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.protobuf)
}

group = "com.induce"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(platform(libs.spring.grpc.dependencies))

    api(libs.protobuf.java)
    api(libs.grpc.protobuf)
    api(libs.grpc.stub)

    implementation(libs.grpc.services)
    implementation(libs.kotlin.reflect)
    implementation(libs.javax.annotation.api)
}

kotlin {
    jvmToolchain(21)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    plugins {
        id("grpc") {
            artifact = libs.grpc.protoc.gen.java.get().toString()
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc") {
                    option("@generated=omit")
                }
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc", "build/generated/source/proto/main/java")
        }
    }
}
