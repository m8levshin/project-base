package com.mlevshin.projectbase.error.exception

import com.mlevshin.projectbase.error.AppError
import io.ktor.http.HttpStatusCode

class ForbiddenException() : ApplicationException(HttpStatusCode.Forbidden) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }

    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}