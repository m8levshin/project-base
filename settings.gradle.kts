// ktor libs
arrayOf(
    "error",
    "plugins",
    "config"
).forEach {
    include("ktor-libs:$it")
}

// services
arrayOf(
    "authorization-service",
    "token-handler-service"
).forEach {
    include(it)
}
