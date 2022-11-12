package com.waigel.testresultapi.entities

import com.waigel.testresultapi.models.SubmitTestRequestDTO
import com.waigel.testresultapi.models.enums.CWATransmission
import com.waigel.testresultapi.models.enums.CWATransmissionStatus
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.OneToOne

@Entity(name = "cwa_transmission_details")
class CwaTransmissionDetails(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var status: CWATransmissionStatus,
    @Enumerated(EnumType.STRING)
    var type: CWATransmission = CWATransmission.NO_TRANSMISSION,

    var transmittedAt: LocalDateTime? = LocalDateTime.now(),

    var hash: String? = null,

    @Lob
    var qrCode: String? = null,
    @Column(length = 2048)
    var appLink: String? = null,

    /**
     * The CWA-Api supports luca jwt generation, so we store the result if available
     * so that user can also use the luca app to scan the qr code or use the app link
     */
    @Column(length = 2048)
    var lucaAppLink: String? = null,
    @Lob
    var lucaQrCode: String? = null

) : AuditModel() {
    companion object {
        fun fromRequest(request: SubmitTestRequestDTO): CwaTransmissionDetails {
            var cwaTransmissionStatus = CWATransmissionStatus.PENDING
            if (request.cwaTransmission == CWATransmission.NO_TRANSMISSION) {
                cwaTransmissionStatus = CWATransmissionStatus.NO_TRANSMISSION
            }

            return CwaTransmissionDetails(
                id = UUID.randomUUID(),
                status = cwaTransmissionStatus,
                type = request.cwaTransmission
            )
        }
    }
}