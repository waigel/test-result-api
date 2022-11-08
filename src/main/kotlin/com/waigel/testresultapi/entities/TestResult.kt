package com.waigel.testresultapi.entities

import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.TestResult
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
    val testResult: TestResult = TestResult.INVALID,
    val testPerformedAt: LocalDateTime = LocalDateTime.now(),

    @OneToOne
    val personalData: PersonalData
) : AuditModel() {

}