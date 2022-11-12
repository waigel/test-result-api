package com.waigel.testresultapi.models

import com.waigel.testresultapi.events.OnUserAcceptTestExecution
import java.util.UUID

class WebhookUserAcceptTestExecutionRequest(
    var tenantId: UUID,
    var testId: String,
    var date: String,
    var ipAddress: String,
    var browserAgent: String
) {

    companion object {
        fun fromEvent(event: OnUserAcceptTestExecution): WebhookUserAcceptTestExecutionRequest {
            return WebhookUserAcceptTestExecutionRequest(
                tenantId = event.tenant.id,
                testId = event.testResult.testId,
                date = event.date.toString(),
                ipAddress = event.ipAddress,
                browserAgent = event.browserAgent
            )

        }
    }
}