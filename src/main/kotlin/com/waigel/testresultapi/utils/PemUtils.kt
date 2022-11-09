package com.waigel.testresultapi.utils

import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemReader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.EncodedKeySpec
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec


object PemUtils {

    private var logger = LoggerFactory.getLogger(PemUtils::class.java)

    @Throws(IOException::class)
    private fun parsePEMFile(pemFile: File): ByteArray {
        if (!pemFile.isFile || !pemFile.exists()) {
            throw FileNotFoundException(
                java.lang.String.format(
                    "The file '%s' doesn't exist.",
                    pemFile.absolutePath
                )
            )
        }
        val reader = PemReader(FileReader(pemFile))
        val pemObject: PemObject = reader.readPemObject()
        val content: ByteArray = pemObject.content
        reader.close()
        return content
    }

    private fun getPublicKey(keyBytes: ByteArray, algorithm: String): PublicKey? {
        val publicKey: PublicKey;
        try {
            val kf: KeyFactory = KeyFactory.getInstance(algorithm)
            val keySpec: EncodedKeySpec = X509EncodedKeySpec(keyBytes)
            publicKey = kf.generatePublic(keySpec)
        } catch (e: NoSuchAlgorithmException) {
            logger.error("Could not reconstruct the public key, the given algorithm could not be found.", e)
            throw e
        } catch (e: InvalidKeySpecException) {
            logger.error("Could not reconstruct the public key", e)
            throw e
        }
        return publicKey
    }

    private fun getPrivateKey(keyBytes: ByteArray, algorithm: String): PrivateKey {
        val privateKey: PrivateKey;
        try {
            val kf: KeyFactory = KeyFactory.getInstance(algorithm)
            val keySpec: EncodedKeySpec = PKCS8EncodedKeySpec(keyBytes)
            privateKey = kf.generatePrivate(keySpec)
        } catch (e: NoSuchAlgorithmException) {
            logger.error("Could not reconstruct the private key, the given algorithm could not be found.", e)
            throw e
        } catch (e: InvalidKeySpecException) {
            logger.error("Could not reconstruct the private key", e)
            throw e
        }
        return privateKey
    }

    @Throws(IOException::class)
    fun readPublicKeyFromFile(filepath: String, algorithm: String): PublicKey? {
        val bytes = parsePEMFile(File(filepath))
        return getPublicKey(bytes, algorithm)
    }

    @Throws(IOException::class)
    fun readPrivateKeyFromFile(filepath: String, algorithm: String): PrivateKey {
        val bytes = parsePEMFile(File(filepath))
        return getPrivateKey(bytes, algorithm)
    }
}