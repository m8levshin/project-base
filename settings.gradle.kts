// ktor libs
arrayOf(
    "service",
    "error",
    "plugins",
    "config",
    "oauth2-resource"
).forEach {
    include("ktor-libs:$it")
}

// spring libs
arrayOf(
    "service"
).forEach {
    include("spring-libs:$it")
}

// common libs
arrayOf(
    "logging",
).forEach {
    include("libs:$it")
}

// services
arrayOf(
    "authorization-service",
    "spring-example-service",
    "token-handler-service",
    "ktor-example-service"
).forEach {
    include(it)
}
