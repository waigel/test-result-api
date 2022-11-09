package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.PersonalData
import com.waigel.testresultapi.entities.TestResult
import com.waigel.testresultapi.models.AccessTokenCreationResponseDTO
import com.waigel.testresultapi.models.SubmitTestResultDTO
import com.waigel.testresultapi.repositories.PersonalDataRepository
import com.waigel.testresultapi.repositories.TestResultRepository
import com.waigel.testresultapi.utils.CryptoHelper
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

@Service
class TestResultService(
    private val tenantService: TenantService,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val testResultRepository: TestResultRepository,
    private val personalDataRepository: PersonalDataRepository,
    private val jwtService: JwtService
) {
    private val logger = LoggerFactory.getLogger(TestResultService::class.java)

    /**
     * Submit test result
     * @param request the test result to submit
     * @param tenantId the tenant id
     */
    @Transactional
    fun submitTestResult(tenantId: UUID, request: SubmitTestResultDTO): AccessTokenCreationResponseDTO {
        logger.info("[${request.testId}] Submit test result for tenant $tenantId")
        val tenant = tenantService.getById(tenantId)
        val transmissionStatus = request.cwaTransmission
        logger.info("[${request.testId}] Transmission status is $transmissionStatus")
        val encryptionKey = CryptoHelper.generateEncryptionKey()

        //ToDo: send request to cwa-api
        val personalData =
            personalDataRepository.save(PersonalData.encryptAndBuildFromRequest(request.userDetails, encryptionKey))

        val testResult = testResultRepository.save(
            TestResult.fromRequest(
                request,
                tenant,
                personalData
            )
        )
        //create accessToken for testResult
        val accessToken = jwtService.createAccessToken(encryptionKey, testResult.id)
        return AccessTokenCreationResponseDTO(accessToken)
    }
}