package com.waigel.testresultapi.configurations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "testresultapi.storage.s3")
@ConstructorBinding
class S3FileStorageConfig(
    var enabled: Boolean = false,
    var accessKey: String? = null,
    var secretKey: String? = null,
    var endpoint: String? = null,
    var signingRegion: String? = null,
    var bucketName: String? = null,
)