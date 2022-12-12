package com.mlevshin.projectbase.error.exception

import com.mlevshin.projectbase.error.AppError
import io.ktor.http.*


class ValidationException() : ApplicationException(HttpStatusCode.BadRequest) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }
    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}