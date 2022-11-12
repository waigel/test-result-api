package com.waigel.testresultapi.webhooks

import com.fasterxml.jackson.databind.ObjectMapper
import com.waigel.testresultapi.configurations.WebhookConfiguration
import com.waigel.testresultapi.events.OnUserAcceptTestExecution
import com.waigel.testresultapi.models.WebhookResponse
import com.waigel.testresultapi.models.WebhookUserAcceptTestExecutionRequest
import com.waigel.testresultapi.utils.CryptoHelper
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class WebhookService(
    private val restTemplate: RestTemplate,
    private val config: WebhookConfiguration
) {
    private val logger = LoggerFactory.getLogger(WebhookService::class.java)
    val signatureHeader = "X-Signature-SHA256"

    private val failedWebhookCalls = mutableListOf<OnUserAcceptTestExecution>()


    private fun buildHmacSignature(data: WebhookUserAcceptTestExecutionRequest): String {
        val payload = ObjectMapper().writeValueAsString(data)
        return CryptoHelper.hmacSha256(config.secret ?: "secret-not-set", payload)
    }

    /**
     * Run a check every minute to see if there are any failed webhook calls
     * that are older than 5 minutes and try to send them again
     */
    @Scheduled(fixedDelay = 1 * 60 * 1000)
    fun retryFailedWebhookCalls() {
        if (failedWebhookCalls.isNotEmpty()) {
            logger.info("Retrying ${failedWebhookCalls.size} failed webhook calls")
            failedWebhookCalls.forEach {
                if (it.date.isBefore(LocalDateTime.now().minusMinutes(5))) {
                    logger.info("Retrying webhook call for ${it.testResult.id}")
                    sendNotificationToTCA(it)
                    failedWebhookCalls.remove(it)
                }

            }
        }
    }

    private fun sendNotificationToTCA(event: OnUserAcceptTestExecution, retry: Boolean = false) {
        logger.info("Sending notification to TCA")
        val body = WebhookUserAcceptTestExecutionRequest.fromEvent(event)

        val headers = HttpHeaders()
        headers.add(signatureHeader, buildHmacSignature(body))
        val entity = HttpEntity(body, headers)
        try {
            val res = restTemplate.postForEntity(config.url, entity, WebhookResponse::class.java)
            if (res.statusCode.is2xxSuccessful) {
                logger.info("Notification to TCA successful")
            } else {
                logger.error("Notification to TCA failed with status ${res.statusCode}")
                if (!retry) {
                    logger.error("Retrying notification to TCA in 5 minutes")
                    failedWebhookCalls.add(event)
                } else {
                    logger.error("Failed to send webhook call again")
                }
            }
        } catch (e: RestClientException) {
            logger.error("Error sending webhook to ${config.url}", e)
        }
    }

    @Async
    @EventListener(OnUserAcceptTestExecution::class)
    fun onUserAcceptTestExecution(event: OnUserAcceptTestExecution) {
        logger.info("WebhookService.onUserAcceptTestExecution")
        this.sendNotificationToTCA(event)
    }
}