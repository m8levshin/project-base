plugins {
    id("org.springframework.boot") version "2.7.6"
    id("io.spring.dependency-management") version SpringDependencyManagement.pluginVersion
    kotlin("jvm") version  Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
}

java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
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
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    implementation(Dependecies.Common.micrometerPrometheusRegistry)

    implementation(project(":libs:spring-libs:service"))
    implementation(project(":libs:logging"))
}
