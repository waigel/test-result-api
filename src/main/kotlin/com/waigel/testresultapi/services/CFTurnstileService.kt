package com.waigel.testresultapi.services

import com.waigel.testresultapi.configurations.CloudflareConfiguration
import com.waigel.testresultapi.exceptions.CaptchaValidationException
import com.waigel.testresultapi.exceptions.Message
import com.waigel.testresultapi.models.CFTurnstileResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class CFTurnstileService(
    private val config: CloudflareConfiguration,
    private val restTemplate: RestTemplate
) {
    private val logger = LoggerFactory.getLogger(CFTurnstileService::class.java)

    fun validateResponse(response: String): Boolean {

        //x-www-form-urlencoded secret and response
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val map: MultiValueMap<String, String> = LinkedMultiValueMap()
        map.add("secret", config.secret)
        map.add("response", response)
        val request = HttpEntity(map, headers)
        //send request with config.url!!
        val res = restTemplate.postForObject(config.url!!, request, CFTurnstileResponse::class.java)
        logger.info("CFTurnstileResponse: $res")
        if (res?.success == true) {
            return true
        }
        logger.info("CaptchaValidationException: ${res?.errorCodes}")
        throw CaptchaValidationException(Message.CAPTCHA_VALIDATION_FAILED)
    }
}