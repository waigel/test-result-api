package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "testresultapi.pdf")
class PDFConfiguration(
    var creatorName: String = "",
)