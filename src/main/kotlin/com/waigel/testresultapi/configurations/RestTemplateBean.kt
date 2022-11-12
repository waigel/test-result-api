package com.waigel.testresultapi.configurations

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class RestTemplateBean {
    @Bean
    fun restTemplate(): org.springframework.web.client.RestTemplate {
        return org.springframework.web.client.RestTemplate()
    }
}