package com.mlevshin.base.error.exception

import com.mlevshin.base.error.AppError
import io.ktor.http.*


class ValidationException() : ApplicationException(HttpStatusCode.BadRequest) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }
    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}