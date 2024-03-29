package com.mlevshin.projectbase.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode

//Regexp string for parsing env variables in configuration files. Supported formats: ${ENV}, ${ENV:::default_value}
private const val ENV_VARIABLE_REGEXP_STRING = "\\$\\{([^}:]+)(?::::([^}]+))?}"
class JacksonNodeEnvVariableProcessor : JacksonNodeProcessor {
    private val envValueRegex = ENV_VARIABLE_REGEXP_STRING.toRegex()
    override fun process(node: JsonNode): JsonNode {
        return when (node) {
            is ArrayNode -> processArrayNode(node)
            is ObjectNode -> processObjectNode(node)
            is TextNode -> processTextNodeWithEnvVariableReplacing(node)
            else -> node
        }
    }

    private fun processTextNodeWithEnvVariableReplacing(node: JsonNode): JsonNode {
        val matchResult = envValueRegex.find(node.textValue()) ?: return node
        val envVariableName = matchResult.groups[1]?.value

        val envVariableValue = System.getenv(envVariableName) ?: if (matchResult.groups[2] != null) {
            matchResult.groups[2]!!.value
        } else {
            throw AssertionError("There isn't $envVariableName env variable or default value for it.")
        }
        val resultValue = envValueRegex.replace(node.textValue(), envVariableValue)
        return TextNode(resultValue)
    }

    private fun processObjectNode(node: ObjectNode): JsonNode {
        for (fieldName in node.fieldNames()) {
            val processedValue = process(node.get(fieldName))
            node.replace(fieldName, processedValue)
        }
        return node
    }

    private fun processArrayNode(node: ArrayNode): JsonNode {
        val processedArrayElements = node.map(::process)
        node.removeAll()
        node.addAll(processedArrayElements)
        return node
    }
}