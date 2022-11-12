package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


@ConfigurationProperties(prefix = "testresultapi.cwa")
@ConstructorBinding
class CWAConfiguration(
    var endpoint: String = "http://cwa-api-live.cwa-api.svc.cluster.local:8080",
    var cn: String = "TRA",
)