package com.waigel.testresultapi.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class PublicDecryptDataRequest(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthDate: LocalDate
) {
}