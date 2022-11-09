package com.waigel.testresultapi.configurations

import com.waigel.testresultapi.components.JwtCertificateLoader
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ConfigurationBeans(
    private var jwtCertificateLoader: JwtCertificateLoader
) {
    @Bean
    fun loadJWTCertificates() {
        jwtCertificateLoader.load()
    }
}