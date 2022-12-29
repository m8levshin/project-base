package com.mlevshin.projectbase


import com.mlevshin.projectbase.config.configureRouting
import com.mlevshin.projectbase.service.configureAndRunKtorApplication

fun main(args: Array<String>) =
    configureAndRunKtorApplication {
        configureRouting()
    }