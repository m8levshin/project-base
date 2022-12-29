package com.mlevshin.projectbase.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.mlevshin.projectbase.commonlibs.logging.utils.logger
import io.ktor.server.application.Application
import org.koin.core.module.Module
import org.koin.dsl.module
import java.net.URL
import java.util.*
import java.util.stream.Collectors
import kotlin.reflect.KClass

class PropertiesKoinModuleInitializer(
    profileName: String,
    configurationFilePath: String = "/application.yml",
    val objectMapper: ObjectMapper = registerYamlObjectMapper(),
    var jacksonNodeEnvVariableProcessor: JacksonNodeProcessor = JacksonNodeEnvVariableProcessor()
) {

    val configuredProperties: MutableMap<KClass<out Any>, Any> = mutableMapOf()
    val configurationModule: Module = module { }
    val sourceList: MutableList<URL> =
        Application::class.java.getResource(configurationFilePath)?.let { mutableListOf(it) } ?: mutableListOf()

    init {
        val profileSpecificPropertiesFile: URL? = Application::class.java
            .getResource(resolveEnvSpecificFileName(configurationFilePath, profileName))
        profileSpecificPropertiesFile?.let { sourceList.add(it) }
    }

    inline fun <reified T : Any> getProperties(clazz: KClass<T>): T? = configuredProperties[clazz] as T?
    inline fun <reified T : Any> addPropertiesBinding(prefix: String, clazz: KClass<T>): T? {
        val mergedNode: JsonNode = sourceList
            .stream()
            .map { getNodeByPath(it, prefix) }
            .filter(Objects::nonNull)
            .collect(Collectors.reducing(objectMapper.nullNode()) { accumulator, current ->
                objectMapper.updateValue(accumulator, current)
            })

        if (!mergedNode.isNull) {
            logger().warn("Initialized configuration for $clazz class")
            val envVariableProcessedNode = jacksonNodeEnvVariableProcessor.process(mergedNode)
            val resultConfiguration = objectMapper.treeToValue(envVariableProcessedNode, clazz.javaObjectType)
            configurationModule.single<T> { resultConfiguration }
            configuredProperties[clazz] = resultConfiguration
            return resultConfiguration
        }
        logger().warn("There isn't initialized configuration for $clazz class")
        return null
    }

    fun getNodeByPath(url: URL, prefix: String): JsonNode? {
        val paths = prefix.split('.')
        var lastNode = objectMapper.readTree(url).get(paths[0])
        if (lastNode != null) {
            for (i in (1 until paths.size)) {
                lastNode = lastNode[paths[i]]
            }
        }
        return lastNode;
    }

    companion object {
        private fun registerYamlObjectMapper(): ObjectMapper {
            return ObjectMapper(YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(KotlinModule.Builder().build())
        }

        private fun resolveEnvSpecificFileName(filePath: String, envName: String): String {
            val extensionDelimiter = '.'
            val splitPath = filePath.split(extensionDelimiter)
            return splitPath.mapIndexed { index: Int, value: String ->
                if (index == splitPath.size - 2) {
                    value + "-${envName}"
                } else {
                    value
                }
            }.joinToString(separator = extensionDelimiter.toString())
        }
    }

}