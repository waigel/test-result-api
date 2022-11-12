package com.waigel.testresultapi.utils

import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacSha256CryptoSystem {

    fun hmacSha256(message: String, secret: String): String {
        val key = SecretKeySpec(secret.toByteArray(), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(key)
        val bytes = mac.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(bytes)
    }
}
