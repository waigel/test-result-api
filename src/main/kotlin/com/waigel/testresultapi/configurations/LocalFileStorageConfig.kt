package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "testresultapi.storage.local")
@ConstructorBinding
class LocalFileStorageConfig(
    var fsDataPath: String = """${System.getProperty("user.home")}/.testperfect""",
)