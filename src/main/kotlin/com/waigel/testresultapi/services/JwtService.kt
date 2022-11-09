package com.waigel.testresultapi.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.waigel.testresultapi.components.JwtCertificateLoader
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService(
    private var jwt: JwtCertificateLoader
) {

    /**
     * Create a JWT token for a test result
     * @param testResultId the test result id
     * @param encryptionKey the generated encryption key
     * @return the jwt token
     * @throws Exception
     *
     */
    fun createAccessToken(encryptionKey: SecretKey, testResultId: UUID): String {
        val algorithm: Algorithm = Algorithm.RSA256(jwt.getPublic(), jwt.getPrivate())
        return JWT.create()
            .withClaim("trId", testResultId.toString())
            .withClaim("key", Base64.getEncoder().encodeToString(encryptionKey.encoded))
            .withIssuer("TRA/v1")
            .sign(algorithm);
    }
}