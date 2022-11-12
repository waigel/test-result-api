package com.waigel.testresultapi.models.enums

import com.itextpdf.text.BaseColor

/**
 * TestResult - Enum for test result
 */
enum class TestResultType {
    NEGATIVE,
    POSITIVE,
    INVALID;

    val color: BaseColor
        get() = when (this) {
            NEGATIVE -> BaseColor(84, 176, 2)
            POSITIVE -> BaseColor(255, 0, 0)
            INVALID -> BaseColor(51, 51, 51)
        }

    val displayText: String
        get() = when (this) {
            NEGATIVE -> "Negativ / Negative"
            POSITIVE -> "Positiv / Positive"
            INVALID -> "Ungültig / Invalid"
        }


}