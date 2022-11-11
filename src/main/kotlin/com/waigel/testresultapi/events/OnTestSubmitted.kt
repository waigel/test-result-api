package com.waigel.testresultapi.events

import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.entities.TestResult
import org.springframework.context.ApplicationEvent
import javax.crypto.SecretKey

class OnTestSubmitted(
    source: Any,
    val testResult: TestResult,
    val tenant: Tenant,
    val encryptionKey: SecretKey
) : ApplicationEvent(source)