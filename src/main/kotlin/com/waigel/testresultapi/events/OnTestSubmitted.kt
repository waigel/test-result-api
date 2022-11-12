package com.waigel.testresultapi.events

import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.entities.TestResult
import com.waigel.testresultapi.models.enums.CWATransmission
import org.springframework.context.ApplicationEvent
import javax.crypto.SecretKey

class OnTestSubmitted(
    source: Any,
    val testResult: TestResult,
    val tenant: Tenant,
    val encryptionKey: SecretKey,
    val cwaTransmission: CWATransmission
) : ApplicationEvent(source)