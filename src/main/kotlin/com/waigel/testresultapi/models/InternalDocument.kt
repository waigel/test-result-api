package com.waigel.testresultapi.models

import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.draw.DottedLineSeparator
import com.waigel.testresultapi.entities.Tenant
import com.waigel.testresultapi.entities.TestResult
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.time.format.DateTimeFormatter

class InternalDocument(
    private val document: Document,
    private val output: ByteArrayOutputStream,
    private val tenant: Tenant
) {
    private val logger = LoggerFactory.getLogger(InternalDocument::class.java)

    /**
     * Static font sizes
     */
    private val font = Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL)
    private val fontBold = Font(font).apply { style = Font.BOLD }
    private val fontItalic = Font(font).apply { style = Font.ITALIC }
    private val h1 = Font(Font.FontFamily.HELVETICA, 28f, Font.BOLD)


    /**
     * Close the document and return the output stream
     */
    fun close(): ByteArrayOutputStream {
        document.close()
        return output
    }

    fun buildDocumentAsCertificate(testResultEntity: TestResult): InternalDocument {
        val personalData = testResultEntity.personalData
        val testId = testResultEntity.testId
        val testResult = testResultEntity.testResult
        val testPerformedAt = testResultEntity.testPerformedAt

        try {

            /**
             * Add logo, if url ist set on tenant location
             */
            try {
                if (tenant.location.logoUrl != null) {
                    document.add(Image.getInstance(tenant.location.logoUrl).apply {
                        scaleToFit(150f, 150f)
                        alignment = Element.ALIGN_CENTER
                    })
                }
            } catch (e: Exception) {
                logger.info("Could not add logo to pdf, skip build pdf without logo")
            }


            /**
             * Title with the test result
             */
            document.add(Paragraph(Chunk(testResult.displayText, h1.apply {
                color = testResult.color
            }).setBackground(BaseColor(223, 223, 223), 5f, 2f, 5f, 5f)).apply {
                alignment = Element.ALIGN_CENTER
            })

            /**
             * Patient address block
             */
            document.add(Paragraph().apply {
                add(Chunk("${personalData.firstName} ${personalData.lastName}\n", font))
                add(Chunk("${personalData.street} ${personalData.houseNumber}\n", font))
                add(Chunk("${personalData.zipcode} ${personalData.city}\n", font))
                add(Chunk(personalData.country, font))
                alignment = Element.ALIGN_LEFT
                spacingAfter = 20f
                spacingBefore = 20f
            })

            /**
             * Information about test result
             */
            document.add(
                Paragraph(
                    "Untersuchung auf SARS-CoV-2-Virus / Examination for SARS-CoV-2-Virus",
                    fontBold
                ).apply {
                    alignment = Element.ALIGN_CENTER
                })

            /**
             * Table with certificate data
             */
            document.add(Paragraph("Zertifikat / Certificate", Font(fontBold).apply {
                size = 16f
            }).apply {
                alignment = Element.ALIGN_LEFT
                spacingBefore = 40f
                spacingAfter = 5f
            })

            /**
             * separator line
             */
            document.add(DottedLineSeparator().apply {
                gap = 4f
                lineColor = BaseColor.GRAY
            })


            val table = com.itextpdf.text.pdf.PdfPTable(2)
            table.spacingBefore = 10f
            table.widthPercentage = 100f
            table.defaultCell.paddingBottom = 8f
            table.defaultCell.paddingTop = 8f
            table.defaultCell.borderColor = BaseColor(255, 255, 255)
            //add Date of Test as first row
            table.addCell(Paragraph("Testzeitpunk / Date of test", fontBold))
            table.addCell(Paragraph(testPerformedAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")), font))
            //test result
            table.addCell(Paragraph("Testergebnis / test result*", fontBold))
            table.addCell(Paragraph(testResult.displayText, font))
            //test type
            table.addCell(Paragraph("Testart / test type", fontBold))
            table.addCell(Paragraph("Covid Antigentest 140000", font))
            //testId
            table.addCell(Paragraph("Test-ID / test id", fontBold))
            table.addCell(Paragraph(testId, font))
            //patient full name
            table.addCell(Paragraph("Name / name", fontBold))
            table.addCell(Paragraph("${personalData.firstName} ${personalData.lastName}", font))
            //patient address
            table.addCell(Paragraph("Anschrift / address", fontBold))
            table.addCell(Paragraph("${personalData.street} ${personalData.houseNumber}", font))
            //patient zip code
            table.addCell(Paragraph("Postleitzahl / post code", fontBold))
            table.addCell(Paragraph(personalData.zipcode, font))
            //patient city
            table.addCell(Paragraph("Ort / city", fontBold))
            table.addCell(Paragraph(personalData.city, font))
            //patient birthdate
            table.addCell(Paragraph("Geburtsdatum / Date of birth", fontBold))
            table.addCell(Paragraph(personalData.birthDate, font))
            document.add(table)


            //legal test required information
            val diagnosticFooter = Paragraph()
            diagnosticFooter.spacingBefore = 30f
            diagnosticFooter.add(
                Chunk(
                    "* Diagnostische Sensitivität: 97,5% Diagnostische Spezifität: 99,4% / Diagnostic sensitivity: 97,5% Diagnostic specificity: 99,4%",
                    font
                )
            )
            document.add(diagnosticFooter)

            val interpretation = Paragraph()
            interpretation.spacingBefore = 20f
            interpretation.add(Chunk("Interpretation / Interpretation:", fontBold))
            interpretation.add(
                Chunk(
                    "Es können keine spezifischen Nukleoproteine des SARS-CoV-2 Virus in der oropharyngealen Probe nachgewiesen werden.",
                    font
                )
            )
            interpretation.add(Chunk.NEWLINE)
            interpretation.add(
                Chunk(
                    "No specific nucleoproteins of the SARS-CoV-2 virus can be detected in the oropharyngeal sample.",
                    font
                )
            )
            document.add(interpretation)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this

    }


}