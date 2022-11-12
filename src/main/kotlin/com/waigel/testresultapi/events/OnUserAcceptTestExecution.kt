package com.waigel.testresultapi.events

import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.entities.TestResult
import org.springframework.context.ApplicationEvent
import java.time.LocalDateTime

class OnUserAcceptTestExecution(
    source: Any,
    val tenant: Tenant,
    val testResult: TestResult,
    val date: LocalDateTime,
    val ipAddress: String,
    val browserAgent: String
) : ApplicationEvent(source)