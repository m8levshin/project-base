package com.mlevshin.crypto.shared.exception

import com.mlevshin.crypto.shared.AppError
import io.ktor.http.*

abstract class ApplicationException(
    val httpStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
    val errorMessages: MutableList<AppError> = mutableListOf()
) : RuntimeException()