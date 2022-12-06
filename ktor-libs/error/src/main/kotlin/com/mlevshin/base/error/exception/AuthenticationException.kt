package com.mlevshin.base.error.exception

import com.mlevshin.base.error.AppError
import io.ktor.http.*

class AuthenticationException() : ApplicationException(HttpStatusCode.Unauthorized) {
    constructor(error: AppError) : this() {
        errorMessages.add(error)
    }
    constructor(errors: List<AppError>) : this() {
        errorMessages.addAll(errors)
    }
}