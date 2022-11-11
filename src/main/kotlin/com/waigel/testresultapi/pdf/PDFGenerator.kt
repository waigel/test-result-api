package com.waigel.testresultapi.pdf

import com.itextpdf.text.Document
import com.itextpdf.text.pdf.PdfWriter
import com.waigel.testresultapi.configurations.PDFConfiguration
import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.models.InternalDocument
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PDFGenerator(
    private val pdfConfiguration: PDFConfiguration,
) {
    /**
     * Create new document and set meta data
     */
    fun createNewDocument(tenant: Tenant): InternalDocument {
        val output = ByteArrayOutputStream()
        val document = Document()
        document.addCreationDate()
        document.addAuthor(pdfConfiguration.creatorName)
        document.addCreator(pdfConfiguration.creatorName)
        document.addTitle("Test Result PDF")
        document.addSubject("Test Result PDF")

        val writer = PdfWriter.getInstance(document, output);
        val event = HeaderFooterPageEvent(tenant)
        writer.pageEvent = event
        document.open();
        return InternalDocument(document, output, tenant)
    }
}