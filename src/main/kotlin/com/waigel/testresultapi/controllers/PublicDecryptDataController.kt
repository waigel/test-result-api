package com.waigel.testresultapi.controllers

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.models.PublicDecryptDataRequest
import com.waigel.testresultapi.models.PublicDecryptedDataResponseDTO
import com.waigel.testresultapi.services.JwtService
import com.waigel.testresultapi.services.TestResultService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping(Constants.API_PUBLIC_DECRYPT_DATA_PATH)
@Tag(name = "Public Decrypt Data API", description = "This API is used to decrypt data.")
class PublicDecryptDataController(
    private val jwtService: JwtService,
    private val testResultService: TestResultService
) {

    private val logger = LoggerFactory.getLogger(PublicDecryptDataController::class.java)

    @PostMapping(value = [""])
    @Operation(summary = "Decrypt data like certificate and corona-warn-app qr code")
    fun decryptData(
        @RequestHeader(Constants.ACCESS_TOKEN_HEADER) accessToken: String,
        @Valid @RequestBody publicDecryptDataRequest: PublicDecryptDataRequest
    ): PublicDecryptedDataResponseDTO {
        logger.info("Decrypt data for test result")
        val decodedPayload = jwtService.validateAccessToken(accessToken)
        logger.info("Decoded payload: $decodedPayload")
        val res = testResultService.getTestResult(decodedPayload, publicDecryptDataRequest.birthDate)
        logger.info("Personal data decrypted for test result with id: ${res.id}")
        return PublicDecryptedDataResponseDTO.fromTestResult(res)
    }

    @PostMapping(value = ["/certificate"])
    @Operation(summary = "Decrypt certificate")
    fun decryptCertificate(
        @RequestHeader(Constants.ACCESS_TOKEN_HEADER) accessToken: String,
        @Valid @RequestBody publicDecryptDataRequest: PublicDecryptDataRequest,
        response: HttpServletResponse,
    ): ByteArray {
        logger.info("Decrypt certificate")
        val decodedPayload = jwtService.validateAccessToken(accessToken)
        logger.info("Decoded payload: $decodedPayload")
        response.addHeader("Cache-Control", "max-age=365, must-revalidate, no-transform")
        return testResultService.getDecryptedCertificate(decodedPayload, publicDecryptDataRequest.birthDate)
    }

}