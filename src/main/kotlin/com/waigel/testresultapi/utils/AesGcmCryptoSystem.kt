package com.waigel.testresultapi.utils

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


/**
 * Utilities for encrypting and decrypting data using AES-GCM.
 *
 * @see [https://mkyong.com/java/java-aes-encryption-and-decryption](https://mkyong.com/java/java-aes-encryption-and-decryption/)
 */
object AesGcmCryptoSystem {
    private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BIT = 128
    private const val IV_LENGTH_BYTE = 12
    private fun getRandomNonce(numBytes: Int): ByteArray {
        val nonce = ByteArray(numBytes)
        SecureRandom().nextBytes(nonce)
        return nonce
    }

    /**
     * Derive the AES key from a password and a salt. Pure method, same parameters result in same
     * output. Uses the PBKDF2 algorithm.
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun deriveSecretKey(password: String, salt: String): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt.toByteArray(), 65536, 256)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    @Throws(GeneralSecurityException::class)
    private fun encryptWithGivenIV(plainText: ByteArray, secret: SecretKey, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)
        cipher.init(Cipher.ENCRYPT_MODE, secret, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        return cipher.doFinal(plainText)
    }

    /**
     * Takes a plainText, generates random iv, encrypts it to cipherText [...encryptedText, ...tag]
     * and returns a byte array of form [...iv, ...cipherText]
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(plainText: ByteArray, secret: SecretKey): ByteArray {
        val iv = getRandomNonce(IV_LENGTH_BYTE)
        val cipherText = encryptWithGivenIV(plainText, secret, iv)
        return ByteBuffer.allocate(iv.size + cipherText.size).put(iv).put(cipherText).array()
    }

    /**
     * Takes a string and encrypts it's UTF-8 Content
     */
    @Throws(GeneralSecurityException::class)
    fun encrypt(plainText: String, secret: SecretKey): ByteArray {
        return encrypt(plainText.toByteArray(StandardCharsets.ISO_8859_1), secret)
    }

    @Throws(GeneralSecurityException::class)
    fun encryptString(plainText: String, secret: SecretKey): String {
        val encrypted =  encrypt(plainText.toByteArray(StandardCharsets.ISO_8859_1), secret)
        return java.util.Base64.getEncoder().encodeToString(encrypted)
    }

    @Throws(GeneralSecurityException::class)
    private fun decryptWithGivenIV(encryptedText: ByteArray, secret: SecretKey, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)
        cipher.init(Cipher.DECRYPT_MODE, secret, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        return cipher.doFinal(encryptedText)
    }

    /**
     * Takes a byte array of form [...iv, ...cipherText] and returns the decrypted data
     */
    @Throws(GeneralSecurityException::class)
    fun decrypt(encryptedText: ByteArray?, secret: SecretKey): ByteArray {
        val bb = ByteBuffer.wrap(encryptedText)
        val iv = ByteArray(IV_LENGTH_BYTE)
        bb[iv] // equiv to bb.get(iv, 0, iv.length);
        val cipherText = ByteArray(bb.remaining())
        bb[cipherText]
        return decryptWithGivenIV(cipherText, secret, iv)
    }
}
