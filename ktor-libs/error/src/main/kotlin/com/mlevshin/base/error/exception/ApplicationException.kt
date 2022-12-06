package com.mlevshin.base.error.exception

import com.mlevshin.base.error.AppError
import io.ktor.http.*

abstract class ApplicationException(
    val httpStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
    val errorMessages: MutableList<AppError> = mutableListOf()
) : RuntimeException()