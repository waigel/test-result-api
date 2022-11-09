package com.waigel.testresultapi.controllers

import com.waigel.testresultapi.Constants
import com.waigel.testresultapi.models.AccessTokenCreationResponseDTO
import com.waigel.testresultapi.models.SubmitTestResultDTO
import com.waigel.testresultapi.services.TestResultService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

@RestController
@RequestMapping(Constants.API_TCA_SUBMIT_TEST_RESULT_PATH)
@Tag(name = "Submit Test Result API", description = "This API receive incoming test results from the TCA")
class SubmitTestResultController(private val testResultService: TestResultService) {
    private val logger = LoggerFactory.getLogger(SubmitTestResultController::class.java)

    /**
     * Submit test result
     */
    @PostMapping(value = [""])
    @Operation(summary = "Submit a test result endpoint")
    fun submitTestResult(
        @Valid @RequestBody submitTestResultRequest: SubmitTestResultDTO,
        @RequestHeader(Constants.TENANT_HEADER) tenant: UUID,
    ): AccessTokenCreationResponseDTO {
        logger.info("[${submitTestResultRequest.testId}] Submit test result for tenant $tenant")
        return testResultService.submitTestResult(tenant, submitTestResultRequest)

    }


}