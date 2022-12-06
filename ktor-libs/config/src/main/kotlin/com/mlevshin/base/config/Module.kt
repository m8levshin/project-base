package com.mlevshin.base.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.server.application.*
import org.koin.core.module.Module
import org.koin.dsl.module
import java.net.URL
import java.util.*
import java.util.stream.Collectors

lateinit var objectMapper: ObjectMapper
lateinit var sourceList: List<URL>
lateinit var jacksonNodeEnvVariableProcessor: JacksonNodeProcessor

fun Application.buildConfigurationModule(env: String? = null, configure: Module.() -> Unit = {}) = module {
    jacksonNodeEnvVariableProcessor = JacksonNodeEnvVariableProcessor()
    registerYamlObjectMapper()
    sourceList = Application::class.java.getResource("/application.yml")?.let { mutableListOf(it) } ?: emptyList()
    val envSpecificResource = Application::class.java.getResource("/application-${env}.yml")
    if (env != null && envSpecificResource != null) {
        sourceList = sourceList.plus(envSpecificResource)
    }
    configure()
}

inline fun <reified T : Any> Module.addConfigBinding(prefix: String, clazz: Class<T>): T {
    val mergedNode = sourceList.stream()
        .map { getNodeByPath(it, prefix)}
        .filter(Objects::nonNull)
        .collect(Collectors.reducing(objectMapper.nullNode()) { accumulator, current ->
            objectMapper.updateValue(accumulator, current)
        })
    val envVariableProcessedNode = jacksonNodeEnvVariableProcessor.process(mergedNode)
    val resultConfiguration = objectMapper.treeToValue(envVariableProcessedNode, clazz)
    single<T> { resultConfiguration }
    return resultConfiguration
}

fun getNodeByPath(url: URL, prefix: String): JsonNode {
    val paths = prefix.split('.')
    var lastNode = objectMapper.readTree(url).get(paths[0])
    for (i in (1 until paths.size)) {
        lastNode = lastNode[paths[i]]
    }
    return lastNode;
}

private fun registerYamlObjectMapper() {
    objectMapper = ObjectMapper(YAMLFactory())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(KotlinModule.Builder().build())
}


