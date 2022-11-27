package com.mlevshin.crypto.utils

class OAuthConstants {
    companion object {
        const val CLIENT_ID_PARAM_NAME = "client_id"
        const val CLIENT_SECRET_PARAM_NAME = "client_secret"
        const val CODE_VERIFIER_PARAM_NAME = "code_verifier"
        const val GRANT_TYPE_PARAM_NAME = "grant_type"
        const val AUTH_CODE_PARAM_NAME = "code"
        const val AUTH_CODE_GRANT_TYPE_VALUE = "authorization_code"
        const val REFRESH_TOKEN_GRANT_TYPE_VALUE = "refresh_token"
        const val CODE_CHALLENGE_PARAM_NAME = "code_challenge"
        const val CODE_CHALLENGE_METHOD_PARAM_NAME = "code_challenge_method"
        const val RESPONSE_TYPE_PARAM_NAME = "response_type"
        const val SCOPE_PARAM_NAME = "scope"
        const val REDIRECT_URI_PARAM_NAME = "redirect_uri"

        const val SH256 = "S256"
    }
}