package com.waigel.testresultapi.models.cwa

import com.waigel.testresultapi.entities.TestResult
import java.time.format.DateTimeFormatter

class PersonalCwaTransmissionRequestDTO(
    val fn: String,
    val ln: String,
    val dob: String, //2001-06-29
    val testId: String
) {
    companion object {
        fun fromTestResult(testResult: TestResult): PersonalCwaTransmissionRequestDTO {
            return PersonalCwaTransmissionRequestDTO(
                fn = testResult.personalData.firstName,
                ln = testResult.personalData.lastName,
                dob = testResult.personalData.birthDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                testId = testResult.testId
            )
        }
    }
}