package com.mlevshin.projectbase.springexampleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.mlevshin"])
class SpringExampleServiceApplication

fun main(args: Array<String>) {
    runApplication<SpringExampleServiceApplication>(*args)
}