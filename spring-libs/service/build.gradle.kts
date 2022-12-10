plugins {
    id("io.spring.dependency-management") version SpringDependencyManagement.pluginVersion
    kotlin("jvm") version Versions.kotlin
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(SpringDependencyManagement.springCloud)
        mavenBom(SpringDependencyManagement.springCloudSleuthOtel)
    }
}

dependencies {
    api("org.springframework.cloud:spring-cloud-starter-sleuth") {
        exclude(group = "org.springframework.cloud", module = "spring-cloud-sleuth-brave")
    }
    api("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure")
    api("io.opentelemetry:opentelemetry-exporter-otlp")
}
