package com.waigel.testresultapi.models

import org.springframework.lang.Nullable
import java.time.LocalDate

class SubmitTestResultDTO(
    val testId: String,
    val testResult: String,
    val birthDate: LocalDate,
    @field:Nullable
    val cwaUserDetails: CoronaWarnAppUserDetailsDTO?
) {
}