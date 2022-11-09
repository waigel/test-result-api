package com.waigel.testresultapi.utils

import com.waigel.testresultapi.entities.PersonalData
import java.util.Random
import javax.crypto.SecretKey


object CryptoHelper {
    private fun getRandomHexString(numChars: Int): String {
        val r = Random()
        val sb = StringBuilder()
        while (sb.length < numChars) {
            sb.append(Integer.toHexString(r.nextInt()))
        }
        return sb.substring(0, numChars)
    }

    fun generateEncryptionKey(): SecretKey {
        val key = getRandomHexString(32)
        return javax.crypto.spec.SecretKeySpec(key.toByteArray(), "AES")
    }

    /**
     * Encrypt each value in PersonalData object with AES256
     */
    fun encryptUserDetails(personalData: PersonalData, encryptionKey: SecretKey): PersonalData {
        return personalData.apply {
            this.firstName = AesGcmCryptoSystem.encryptString(this.lastName, encryptionKey)
            this.lastName = AesGcmCryptoSystem.encryptString(this.lastName, encryptionKey)
            this.birthDate = AesGcmCryptoSystem.encryptString(this.birthDate, encryptionKey)
            this.street = AesGcmCryptoSystem.encryptString(this.street, encryptionKey)
            this.houseNumber = AesGcmCryptoSystem.encryptString(this.houseNumber, encryptionKey)
            this.zipcode = AesGcmCryptoSystem.encryptString(this.zipcode, encryptionKey)
            this.city = AesGcmCryptoSystem.encryptString(this.city, encryptionKey)
            this.country = AesGcmCryptoSystem.encryptString(this.country, encryptionKey)
        }
    }
}
