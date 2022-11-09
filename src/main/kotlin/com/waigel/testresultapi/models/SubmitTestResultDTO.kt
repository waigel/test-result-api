package com.waigel.testresultapi.models

import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.TestResultType

class SubmitTestResultDTO(
    val testId: String,
    val testResult: TestResultType,

    val userDetails: UserDetailsDTO,
    val cwaTransmission: CWATransmission
) {
}