package com.waigel.testresultapi.entities

import com.waigel.testresultapi.models.SubmitTestRequestDTO
import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.TestResultType
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity(name = "test_result")
class TestResult(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    val testId: String,

    @Enumerated(EnumType.STRING)
    val cwaTransmissionType: CWATransmission = CWATransmission.NO_TRANSMISSION,

    @Enumerated(EnumType.STRING)
    val testResult: TestResultType = TestResultType.INVALID,
    val testPerformedAt: LocalDateTime = LocalDateTime.now(),
    val testName: String = "",

    @OneToOne
    var personalData: PersonalData,

    @OneToOne(mappedBy = "testResult")
    var uploadedDocument: UploadedDocument? = null

) : AuditModel() {
    fun copy(personalData: PersonalData): TestResult {
        return TestResult(
            id = id,
            testId = testId,
            cwaTransmissionType = cwaTransmissionType,
            testResult = testResult,
            testPerformedAt = testPerformedAt,
            personalData = personalData
        )
    }

    companion object {
        fun fromRequest(request: SubmitTestRequestDTO, tenant: Tenant, personalData: PersonalData): TestResult {
            return TestResult(
                testId = request.testId,
                cwaTransmissionType = request.cwaTransmission,
                testResult = request.testResult,
                testPerformedAt = LocalDateTime.now(),
                personalData = personalData
            )
        }
    }

}