package com.waigel.testresultapi.models

import org.springframework.lang.Nullable
import java.time.LocalDate

/**
 * CoronaWarnAppUserDetailsDTO
 * If firstName or/and  lastName are null, anonymous corona warn app transmission is used
 */
class CoronaWarnAppUserDetailsDTO(
    val testId: String,
    @field:Nullable
    val firstName: String?,
    @field:Nullable
    val lastName: String?,
    val birthDate: LocalDate,
) {
}