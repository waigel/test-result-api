package com.waigel.testresultapi.storage

import com.amazonaws.services.s3.AmazonS3
import com.waigel.testresultapi.configurations.LocalFileStorageConfig
import com.waigel.testresultapi.configurations.S3FileStorageConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileStorageConfiguration(
    private val s3Config: S3FileStorageConfig,
    private val localConfig: LocalFileStorageConfig,
    private val amazonS3: AmazonS3?
) {


    @Bean
    fun fileStorage(): FileStorage {
        if (s3Config.enabled && amazonS3 != null) {
            return S3FileStorage(config = s3Config, amazonS3)
        }
        return LocalFileStorage(config = localConfig)
    }
}
