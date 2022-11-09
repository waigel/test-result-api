package com.waigel.testresultapi.components

import com.waigel.testresultapi.configurations.MainConfiguration
import com.waigel.testresultapi.utils.PemUtils
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

@Component
class JwtCertificateLoader(
    private val mainConfiguration: MainConfiguration
    ) {
    private var jwtPrivateKey: RSAPrivateKey? = null;
    private var jwtPublicKey: RSAPublicKey? = null;

    fun load() {
        this.jwtPrivateKey = PemUtils.readPrivateKeyFromFile(mainConfiguration.jwtPrivateKeyPath, "RSA") as RSAPrivateKey;
        this.jwtPublicKey = PemUtils.readPublicKeyFromFile(mainConfiguration.jwtPublicKeyPath, "RSA") as RSAPublicKey
    }

    fun getPrivate(): RSAPrivateKey {
        if(jwtPrivateKey == null) {
            load()
        }
        return jwtPrivateKey!!
    }
    fun getPublic(): RSAPublicKey {
        if(jwtPublicKey == null) {
            load()
        }
        return jwtPublicKey!!
    }
}