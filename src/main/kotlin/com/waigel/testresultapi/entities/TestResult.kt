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
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity(name = "test_result")
class TestResult(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    val testId: String,

    @Enumerated(EnumType.STRING)
    val testResult: TestResultType = TestResultType.INVALID,
    val testPerformedAt: LocalDateTime = LocalDateTime.now(),
    val testName: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    var tenant: Tenant? = null,

    @OneToOne(fetch = FetchType.EAGER)
    var personalData: PersonalData,

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "testResult")
    var uploadedDocument: UploadedDocument? = null,

    @OneToOne(fetch = FetchType.EAGER)
    var cwaTransmissionDetails: CwaTransmissionDetails? = null,

) : AuditModel() {
    fun copy(personalData: PersonalData): TestResult {
        return TestResult(
            id = id,
            testId = testId,
            testResult = testResult,
            testPerformedAt = testPerformedAt,
            personalData = personalData,
            cwaTransmissionDetails = cwaTransmissionDetails,
            uploadedDocument = uploadedDocument,
            testName = testName,
            tenant = tenant,
        )
    }

    override fun toString(): String {
        return "TestResult(id=$id, testId='$testId', testResult=$testResult, testPerformedAt=$testPerformedAt, testName='$testName', personalData=$personalData, uploadedDocument=$uploadedDocument, cwaTransmissionDetails=$cwaTransmissionDetails)"
    }
    companion object {
        fun fromRequest(request: SubmitTestRequestDTO, tenant: Tenant, personalData: PersonalData, cwaTransmissionDetails: CwaTransmissionDetails): TestResult {
            return TestResult(
                testId = request.testId,
                testResult = request.testResult,
                testPerformedAt = LocalDateTime.now(),
                personalData = personalData,
                cwaTransmissionDetails = cwaTransmissionDetails,
                testName = request.testName,
                uploadedDocument = null,
                tenant = tenant,
                id = UUID.randomUUID()
            )
        }
    }

}