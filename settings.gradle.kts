// ktor libs
arrayOf(
    "service",
    "error",
    "plugins",
    "config",
    "oauth2-resource"
).forEach {
    include("libs:ktor-libs:$it")
}

// spring libs
arrayOf(
    "service"
).forEach {
    include("libs:spring-libs:$it")
}

// common libs
arrayOf(
    "logging",
).forEach {
    include("libs:$it")
}

// services
arrayOf(
    "spring-example-service",
    "token-handler-service",
    "ktor-example-service"
).forEach {
    include("services:$it")
}
