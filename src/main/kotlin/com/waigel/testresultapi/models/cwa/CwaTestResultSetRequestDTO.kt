package com.waigel.testresultapi.models.cwa

import com.waigel.testresultapi.entities.TestResult

class CwaTestResultSetRequestDTO(
    val sc: Long,
    val testResult: String,
    val hash: String,
    val testIdentifier: Int
) {
    companion object {
        fun fromTestResult(testResult: TestResult, hash: String?): CwaTestResultSetRequestDTO {
            return CwaTestResultSetRequestDTO(
                sc = testResult.testPerformedAt.toEpochSecond(java.time.ZoneOffset.UTC),
                testResult = testResult.testResult.toString(),
                hash = hash ?: "",
                testIdentifier = 2579 //TODO: get from config
            )

        }
    }
}