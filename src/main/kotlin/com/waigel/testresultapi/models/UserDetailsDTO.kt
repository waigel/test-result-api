package com.waigel.testresultapi.models

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class UserDetailsDTO(
    val firstName: String,
    val lastName: String,
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val birthDate: LocalDate,
    val street: String,
    val houseNumber: String,
    val city: String,
    val zipcode: String,
    val country: String,
) {
}