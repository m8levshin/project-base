package com.mlevshin.crypto.shared.exception

import com.mlevshin.crypto.shared.AppError
import io.ktor.http.*


class ValidationException() : ApplicationException(HttpStatusCode.BadRequest) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }
    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}