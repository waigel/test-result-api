package com.waigel.testresultapi.models

import com.waigel.testresultapi.entities.TestResult
import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.TestResultType
import java.time.LocalDateTime

class PublicDecryptedDataResponseDTO(
    val testId: String,
    val firstName: String,
    val lastName: String,
    val testPerformedAt: LocalDateTime,
    val testName: String,
    val testResult: TestResultType,
    val cwaTransmission: CWATransmission,

    val cwaAppLink: String?,
    val cwaQrCode: String?,
    val lucaAppLink: String?,
    val lucaQrCode: String?
) {
    companion object {
        fun fromTestResult(res: TestResult): PublicDecryptedDataResponseDTO {
            return PublicDecryptedDataResponseDTO(
                testId = res.testId,
                firstName = res.personalData.firstName,
                lastName = res.personalData.lastName,
                testPerformedAt = res.testPerformedAt,
                testName = res.testName,
                testResult = res.testResult,
                cwaTransmission = res.cwaTransmissionDetails?.type ?: CWATransmission.NO_TRANSMISSION,
                cwaAppLink = res.cwaTransmissionDetails?.appLink,
                cwaQrCode = res.cwaTransmissionDetails?.qrCode,
                lucaAppLink = res.cwaTransmissionDetails?.lucaAppLink,
                lucaQrCode = res.cwaTransmissionDetails?.lucaQrCode
            )
        }

    }
}