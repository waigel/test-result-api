package com.waigel.testresultapi.services

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.configurations.CWAConfiguration
import com.waigel.testresultapi.entities.CwaTransmissionDetails
import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.events.OnTestSubmitted
import com.waigel.testresultapi.models.cwa.CwaLucaResponseDTO
import com.waigel.testresultapi.models.cwa.CwaTestResultSetRequestDTO
import com.waigel.testresultapi.models.cwa.CwaTransmissionRegistrationResultDTO
import com.waigel.testresultapi.models.cwa.PersonalCwaTransmissionRequestDTO
import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.CWATransmissionStatus
import com.waigel.testresultapi.models.enums.TestResultType
import com.waigel.testresultapi.repositories.CwaTransmissionDetailsRepository
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class CwaTransmissionService(
    private val restTemplate: RestTemplate,
    private val config: CWAConfiguration,
    private val cwaTransmissionDetailsRepository: CwaTransmissionDetailsRepository
) {


    private val logger = LoggerFactory.getLogger(CwaTransmissionService::class.java)


    /**
     * Build the auth headers for the CWA API
     */
    private fun buildRequestHeader(tenant: Tenant?): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json")
        headers.add("Accept", "application/json")
        headers.add("ssl-client-verify", "SUCCESS")
        headers.add("ssl-client-subject-dn", "CN=${config.cn},O=Internet Widgits Pty Ltd,ST=Some-State,C=DE")
        headers.add(Constants.TENANT_HEADER, tenant?.id.toString())
        return headers
    }

    /**
     * Register a test result for CWA transmission and update database record
     * @param event the test submitted event
     * @param body the request body
     * @return the transmission details after db update
     */
    private fun registerTest(event: OnTestSubmitted, body: PersonalCwaTransmissionRequestDTO?): CwaTransmissionDetails {
        val entity = HttpEntity(body, buildRequestHeader(event.tenant))

        val cwaTransmissionDetails = event.testResult.cwaTransmissionDetails
        if (cwaTransmissionDetails == null) {
            logger.error("[${event.testResult.id}] CwaTransmissionDetails is null")
            throw IllegalStateException("CwaTransmissionDetails is null")
        }

        try {
            val response = restTemplate.postForEntity(
                "${config.endpoint}/api/v1/register/${if (body == null) "anonym" else ""}",
                entity,
                CwaTransmissionRegistrationResultDTO::class.java
            )
            cwaTransmissionDetails.apply {
                this.hash = response.body?.hash
                this.qrCode = response.body?.qrCode
                this.appLink = response.body?.appLink
                this.transmittedAt = LocalDateTime.now()
                this.status = CWATransmissionStatus.SUCCESS
            }
            logger.info("[${event.testResult.id}] CWA Transmission successful with status ${response.statusCode}")
        } catch (e: RestClientException) {
            cwaTransmissionDetails.apply {
                this.status = CWATransmissionStatus.FAILED
                this.transmittedAt = LocalDateTime.now()
            }
            logger.error("[${event.testResult.id}] Error while submitting personal test result", e)
        }
        logger.info("[${event.testResult.id}] Update CWA Transmission Details $cwaTransmissionDetails")
        return cwaTransmissionDetailsRepository.save(cwaTransmissionDetails)
    }

    /**
     * Register a personal test result for CWA transmission
     * @param event the test submitted event
     * @return the transmission details after db update
     */
    private fun registerPersonalTest(event: OnTestSubmitted): CwaTransmissionDetails {
        logger.info("[${event.testResult.id}] Submit personal test result")
        val personalCwaTransmissionRequest = PersonalCwaTransmissionRequestDTO.fromTestResult(event.testResult)
        return this.registerTest(event, personalCwaTransmissionRequest)
    }

    /**
     * Register an anonymous test result for CWA transmission and update database record
     * @param event the test submitted event
     * @return the transmission details after db update
     */
    private fun registerAnonymousTest(event: OnTestSubmitted): CwaTransmissionDetails {
        logger.info("[${event.testResult.id}] Submit anonymous test result")
        return this.registerTest(event, null)
    }

    private fun submitTestResult(status: CwaTransmissionDetails, event: OnTestSubmitted) {
        val body = CwaTestResultSetRequestDTO.fromTestResult(event.testResult, status.hash)
        val entity = HttpEntity(body, buildRequestHeader(null))
        try {
            val response = restTemplate.postForEntity(
                "${config.endpoint}/api/v1/result/",
                entity,
                CwaLucaResponseDTO::class.java
            )
            logger.info("[$event.testResult.id] CWA Transmission successful with status ${response.statusCode}")
        } catch (e: RestClientException) {
            logger.error("[$event.testResult.id] Error while submitting personal test result", e)
        }

    }

    @Async
    @EventListener(OnTestSubmitted::class)
    fun onTestSubmitted(event: OnTestSubmitted) {
        logger.info("Test ${event.testResult.testId} submitted, check if transmission to cwa is required")
        val transmissionType = event.cwaTransmission
        logger.info("Transmission type is $transmissionType")

        when (transmissionType) {
            CWATransmission.NO_TRANSMISSION -> {
                logger.info("No transmission required for test ${event.testResult.testId}")
                return;
            }

            CWATransmission.PERSONAL_TRANSMISSION -> {
                logger.info("Personal transmission required for test ${event.testResult.testId}")
                val registeredTest = this.registerPersonalTest(event)
                this.submitTestResult(registeredTest, event)
            }

            CWATransmission.ANONYMOUS_TRANSMISSION -> {
                logger.info("Anonymous transmission required for test ${event.testResult.testId}")
                val registeredTest = this.registerAnonymousTest(event)
                this.submitTestResult(registeredTest, event)
            }
        }

    }

    /**
     * Save the transmission details to the database
     */
    fun save(fromRequest: CwaTransmissionDetails): CwaTransmissionDetails {
        return cwaTransmissionDetailsRepository.save(fromRequest)
    }

}