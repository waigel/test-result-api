package com.waigel.testresultapi.models

import com.fasterxml.jackson.annotation.JsonProperty

class CFTurnstileResponse(
    var success: Boolean = false,
    @field:JsonProperty("error-codes")
    var errorCodes: List<String>?,
    var challenge_ts: String?,
    var hostname: String?,
) {
    override fun toString(): String {
        return "CFTurnstileResponse(success=$success, errorCodes=$errorCodes, challenge_ts=$challenge_ts, hostname=$hostname)"
    }
}