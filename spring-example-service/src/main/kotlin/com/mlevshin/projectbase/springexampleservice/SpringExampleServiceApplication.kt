package com.mlevshin.projectbase.springexampleservice

import com.mlevshin.projectbase.commonlibs.logging.ServiceJsonLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SpringExampleServiceApplication

fun main(args: Array<String>) {
    ServiceJsonLayout.serviceName = "spring-example-service"
    runApplication<SpringExampleServiceApplication>(*args)
}


@RestController
class TestRestController {
    @GetMapping
    suspend fun root() : Flow<String> {
        LoggerFactory.getLogger(this.javaClass).info("asdfsafasf")
        return listOf("mene", "mene", "tekel", "upharsin").asFlow()
    }
}