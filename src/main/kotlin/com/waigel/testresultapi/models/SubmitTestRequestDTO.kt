package com.waigel.testresultapi.models

import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.TestResultType
import org.jetbrains.annotations.NotNull

class SubmitTestRequestDTO(
    val testId: String,
    val testResult: TestResultType,
    @field:NotNull
    val testName: String,

    val userDetails: UserDetailsDTO,
    val cwaTransmission: CWATransmission
) {
}