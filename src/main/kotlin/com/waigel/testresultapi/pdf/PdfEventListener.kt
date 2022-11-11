package com.waigel.testresultapi.pdf

import com.waigel.testresultapi.events.OnTestSubmitted
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.FileOutputStream

@Component
class PdfEventListener(
    private val pdfGenerator: PDFGenerator
) {

    private val logger = LoggerFactory.getLogger(PdfEventListener::class.java)

    @EventListener(OnTestSubmitted::class)
    fun handleTestSubmittedEvent(event: OnTestSubmitted) {
        logger.info("Received event: ${event.javaClass.simpleName}")
        val certificate = pdfGenerator.createNewDocument(event.tenant)
            .buildDocumentAsCertificate(event.testResult)
            .close()
        logger.info("Certificate created for test ${event.testResult.testId}")

        certificate.writeTo(FileOutputStream("test.pdf"))
    }

}