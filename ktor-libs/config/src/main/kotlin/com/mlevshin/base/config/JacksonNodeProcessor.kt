package com.mlevshin.base.config

import com.fasterxml.jackson.databind.JsonNode

interface JacksonNodeProcessor {
    fun process(node: JsonNode): JsonNode
}