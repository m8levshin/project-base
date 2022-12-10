plugins {
    id("org.springframework.boot") version Versions.springBoot
    id("io.spring.dependency-management") version SpringDependencyManagement.pluginVersion
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
}

java.sourceCompatibility = JavaVersion.VERSION_17

dependencyManagement {
    imports {
        mavenBom(SpringDependencyManagement.springCloud)
        mavenBom(SpringDependencyManagement.springCloudSleuthOtel)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    runtimeOnly ("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation(Dependecies.Spring.authServer)
    implementation(Dependecies.Common.micrometerPrometheusRegistry)

    implementation(project(":spring-libs:service"))
    implementation(project(":libs:logging"))
}
