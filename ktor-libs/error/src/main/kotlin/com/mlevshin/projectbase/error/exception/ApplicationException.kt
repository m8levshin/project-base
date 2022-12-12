package com.mlevshin.projectbase.error.exception

import com.mlevshin.projectbase.error.AppError
import io.ktor.http.*

abstract class ApplicationException(
    val httpStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
    val errorMessages: MutableList<AppError> = mutableListOf()
) : RuntimeException()