package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "testresultapi.cf.turnstile")
@ConstructorBinding
class CloudflareConfiguration(
    var url: String? = null,
    var secret: String? = null,
)