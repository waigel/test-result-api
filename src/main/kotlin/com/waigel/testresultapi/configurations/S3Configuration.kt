package com.waigel.testresultapi.configurations

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Configuration(private val s3config: S3FileStorageConfig) {


    @Bean
    fun s3(): AmazonS3? {
        if (s3config.enabled) {
            val credentials: AWSCredentials = BasicAWSCredentials(
                s3config.accessKey,
                s3config.secretKey
            )

            val endpointConfig = AwsClientBuilder.EndpointConfiguration(s3config.endpoint, s3config.signingRegion)

            return AmazonS3ClientBuilder.standard().withCredentials(AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfig)
                .enablePathStyleAccess()
                .build()
        }
        return null
    }
}
