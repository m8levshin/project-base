package com.mlevshin.projectbase.springexampleservice

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestRestController {
    @GetMapping("/api/test")
    suspend fun root(): Flow<String> {
        LoggerFactory.getLogger(this.javaClass).info("asdfsafasf")
        return listOf("mene", "mene", "tekel", "upharsin").asFlow()
    }
}