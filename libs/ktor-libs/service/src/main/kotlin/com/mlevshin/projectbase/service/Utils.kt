package com.mlevshin.projectbase.service

import io.ktor.server.application.Application
import org.koin.core.error.InstanceCreationException
import org.koin.core.error.NoBeanDefFoundException
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.ktor.ext.get

inline fun <reified T : Any> Application.injectOrNull(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    try {
        get<T>(qualifier, parameters)
    } catch (e: InstanceCreationException) {
        null
    } catch (e: IllegalStateException) {
        null
    } catch (e: NoBeanDefFoundException) {
        null
    }
}