plugins {
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version SpringDependencyManagement.pluginVersion
    kotlin("jvm") version  Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
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
    api("org.springframework.boot:spring-boot-starter-webflux")
    api("org.springframework.cloud:spring-cloud-starter-sleuth") {
        exclude(group = "org.springframework.cloud", module = "spring-cloud-sleuth-brave")
    }
    api("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure")
    api("io.opentelemetry:opentelemetry-exporter-otlp")
}
