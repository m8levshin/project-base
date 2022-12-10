// ktor libs
arrayOf(
    "service",
    "error",
    "plugins",
    "config"
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
    "token-handler-service"
).forEach {
    include(it)
}
