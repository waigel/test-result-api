package com.waigel.testresultapi.models.cwa

class CwaTransmissionRegistrationResultDTO(
    val qrCode: String,
    val appLink: String,
    val hash: String
) {
    override fun toString(): String {
        return "CwaTransmissionRegistrationResultDTO(qrCode='$qrCode', appLink='$appLink', hash='$hash')"
    }
}