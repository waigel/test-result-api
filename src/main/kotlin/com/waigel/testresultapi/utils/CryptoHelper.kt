package com.waigel.testresultapi.utils

import com.waigel.testresultapi.entities.PersonalData
import java.io.ByteArrayOutputStream
import java.util.Base64
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
            this.firstName = AesGcmCryptoSystem.encryptString(this.firstName, encryptionKey)
            this.lastName = AesGcmCryptoSystem.encryptString(this.lastName, encryptionKey)
            this.birthDate = AesGcmCryptoSystem.encryptString(this.birthDate, encryptionKey)
            this.street = AesGcmCryptoSystem.encryptString(this.street, encryptionKey)
            this.houseNumber = AesGcmCryptoSystem.encryptString(this.houseNumber, encryptionKey)
            this.zipcode = AesGcmCryptoSystem.encryptString(this.zipcode, encryptionKey)
            this.city = AesGcmCryptoSystem.encryptString(this.city, encryptionKey)
            this.country = AesGcmCryptoSystem.encryptString(this.country, encryptionKey)
        }
    }

    fun decryptUserDetails(personalData: PersonalData, key: SecretKey): PersonalData {
        return personalData.apply {
            this.firstName = AesGcmCryptoSystem.decryptString(this.firstName, key)
            this.lastName = AesGcmCryptoSystem.decryptString(this.lastName, key)
            this.birthDate = AesGcmCryptoSystem.decryptString(this.birthDate, key)
            this.street = AesGcmCryptoSystem.decryptString(this.street, key)
            this.houseNumber = AesGcmCryptoSystem.decryptString(this.houseNumber, key)
            this.zipcode = AesGcmCryptoSystem.decryptString(this.zipcode, key)
            this.city = AesGcmCryptoSystem.decryptString(this.city, key)
            this.country = AesGcmCryptoSystem.decryptString(this.country, key)
        }

    }

    fun getSecretKeyFromBase64String(key: String): SecretKey {
        return javax.crypto.spec.SecretKeySpec(Base64.getDecoder().decode(key), "AES")
    }

    /**
     * Encrypt a file with AES256
     */
    fun encryptFile(document: ByteArrayOutputStream, encryptionKey: SecretKey): ByteArrayOutputStream {
        return AesGcmCryptoSystem.encryptFile(document, encryptionKey)
    }

    /**
     * Decrypt a file with AES256
     */
    fun decryptFile(document: ByteArray, secretKey: SecretKey): ByteArray {
        return AesGcmCryptoSystem.decrypt(document, secretKey)
    }
}
