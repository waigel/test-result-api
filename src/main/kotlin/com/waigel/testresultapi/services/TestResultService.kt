package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.CwaTransmissionDetails
import com.waigel.testresultapi.entities.PersonalData
import com.waigel.testresultapi.entities.TestResult
import com.waigel.testresultapi.events.OnTestSubmitted
import com.waigel.testresultapi.events.OnUserAcceptTestExecution
import com.waigel.testresultapi.exceptions.Message
import com.waigel.testresultapi.exceptions.NotFoundException
import com.waigel.testresultapi.exceptions.PermissionDeniedException
import com.waigel.testresultapi.models.AccessTokenCreationResponseDTO
import com.waigel.testresultapi.models.JwtPayload
import com.waigel.testresultapi.models.SubmitTestRequestDTO
import com.waigel.testresultapi.repositories.PersonalDataRepository
import com.waigel.testresultapi.repositories.TestResultRepository
import com.waigel.testresultapi.utils.CryptoHelper
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.transaction.Transactional

@Service
class TestResultService(
    private val tenantService: TenantService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val testResultRepository: TestResultRepository,
    private val personalDataRepository: PersonalDataRepository,
    private val jwtService: JwtService,
    private val documentUploadService: DocumentUploadService,
    private val cwaTransmissionService: CwaTransmissionService
) {
    private val logger = LoggerFactory.getLogger(TestResultService::class.java)


    /**
     * Get test result by id
     */
    fun getById(id: UUID): TestResult {
        return testResultRepository.findById(id).orElseThrow {
            logger.info("Test result with id $id not found")
            throw NotFoundException(Message.RESOURCE_NOT_FOUND)
        }
    }


    /**
     * Submit test result
     * @param request the test result to submit
     * @param tenantId the tenant id
     */
    @Transactional
    fun submitTestResult(tenantId: UUID, request: SubmitTestRequestDTO): AccessTokenCreationResponseDTO {
        logger.info("[${request.testId}] Submit test result for tenant $tenantId")
        val tenant = tenantService.getById(tenantId)
        val transmissionStatus = request.cwaTransmission
        logger.info("[${request.testId}] Transmission status is $transmissionStatus")
        val encryptionKey = CryptoHelper.generateEncryptionKey()

        val personalData = personalDataRepository.save(PersonalData.fromRequest(request.userDetails))
        val cwaTransmissionDetails = cwaTransmissionService.save(CwaTransmissionDetails.fromRequest(request))
        val copyOfUnencryptedPersonalData = personalData.copy()

        CryptoHelper.encryptUserDetails(personalData, encryptionKey)
        val testResult = testResultRepository.save(
            TestResult.fromRequest(
                request,
                tenant,
                personalData,
                cwaTransmissionDetails
            )
        )
        //send OnTestSubmitted to trigger PDF generation and cwa transmission if necessary
        applicationEventPublisher.publishEvent(
            OnTestSubmitted(
                source = this,
                testResult = testResult.copy(copyOfUnencryptedPersonalData), // replace encrypted personal data with unencrypted personal data
                tenant = tenant,
                encryptionKey = encryptionKey,
                cwaTransmission = transmissionStatus
            )
        )

        val accessToken = jwtService.createAccessToken(encryptionKey, testResult.id)
        return AccessTokenCreationResponseDTO(accessToken)
    }

    /**
     * Get test result by id and validate birthdate
     */
    @Transactional
    fun getTestResult(decodedPayload: JwtPayload, birthdate: LocalDate, request: HttpServletRequest): TestResult {
        val testResult = this.getById(UUID.fromString(decodedPayload.trId))
        val secretKey = CryptoHelper.getSecretKeyFromBase64String(decodedPayload.key)
        val decryptedPersonalData = CryptoHelper.decryptUserDetails(testResult.personalData, secretKey)
        logger.info("Validate birthdate ${decryptedPersonalData.birthDate} with input $birthdate")
        if (decryptedPersonalData.birthDate != birthdate.format(PersonalData.BIRTH_DATE_FORMAT)) {
            throw PermissionDeniedException(Message.BIRTHDATE_WRONG)
        }

        //user has decrypted the test result, so we can trigger the webhook event
        try {
            applicationEventPublisher.publishEvent(
                OnUserAcceptTestExecution
                    (
                    source = this,
                    tenant = testResult.tenant!!,
                    testResult = testResult,
                    date = LocalDateTime.now(),
                    ipAddress = request.remoteAddr,
                    browserAgent = request.getHeader("User-Agent")
                )
            )
        } catch (e: Exception) {
            logger.info("Error while publishing event OnUserAcceptTestExecution")
        }

        return testResult
    }

    fun getDecryptedCertificate(
        decodedPayload: JwtPayload,
        birthDate: LocalDate,
        request: HttpServletRequest
    ): ByteArray {
        val testResult = this.getTestResult(decodedPayload, birthDate, request)
        val secretKey = CryptoHelper.getSecretKeyFromBase64String(decodedPayload.key)
        if (testResult.uploadedDocument == null) {
            throw NotFoundException(Message.RESOURCE_NOT_FOUND)
        }
        return documentUploadService.retrieve(testResult.uploadedDocument!!, secretKey)
    }
}