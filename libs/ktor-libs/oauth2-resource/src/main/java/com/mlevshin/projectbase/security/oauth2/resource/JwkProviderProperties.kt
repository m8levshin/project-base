package com.mlevshin.projectbase.security.oauth2.resource

data class JwkProviderProperties(
    val issuer: String,
    val audiences: Array<String>,
    val jwkSetUrl: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JwkProviderProperties

        if (issuer != other.issuer) return false
        if (!audiences.contentEquals(other.audiences)) return false
        if (jwkSetUrl != other.jwkSetUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = issuer.hashCode()
        result = 31 * result + audiences.contentHashCode()
        result = 31 * result + jwkSetUrl.hashCode()
        return result
    }
}