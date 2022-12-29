package com.mlevshin.projectbase.utils


import org.apache.commons.lang3.RandomStringUtils
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

class CodeChallengeUtils {

    companion object {
        private const val CODE_CHALLENGE_LENGTH = 100
        fun getCodeVerifierString(): String = RandomStringUtils.randomAlphanumeric(CODE_CHALLENGE_LENGTH)
    }
}

fun String.toCodeChallenge(): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(this.toByteArray(StandardCharsets.US_ASCII))
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest)
}

