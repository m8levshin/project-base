package com.mlevshin.crypto.shared.exception

import com.mlevshin.crypto.shared.AppError
import io.ktor.http.*

class ForbiddenException() : ApplicationException(HttpStatusCode.Forbidden) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }

    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}