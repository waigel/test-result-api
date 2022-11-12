package com.waigel.testresultapi.models.cwa

class CwaLucaResponseDTO(
    val lucaQrCode: String?,
    val lucaAppLink: String?
) {
    override fun toString(): String {
        return "CwaLucaResponseDTO(lucaQrCode=$lucaQrCode, lucaAppLink=$lucaAppLink)"
    }
}