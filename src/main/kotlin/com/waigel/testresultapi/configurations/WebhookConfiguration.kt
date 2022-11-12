package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "testresultapi.webhook")
@ConstructorBinding
class WebhookConfiguration(
    var url: String = "http://localhost:8080/api/v1/webhook",
    var secret: String? = null,
)