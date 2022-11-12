package com.waigel.testresultapi.pdf

import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.ColumnText
import com.itextpdf.text.pdf.PdfPageEventHelper
import com.itextpdf.text.pdf.PdfWriter
import com.waigel.testresultapi.entities.Tenant
import java.time.format.DateTimeFormatter

class HeaderFooterPageEvent(
    private val tenant: Tenant
) : PdfPageEventHelper() {

    /**
     * Helper methode to add components to the right position in the footer
     */
    private fun addComponentToFooter(writer: PdfWriter, paragraph: Paragraph, number: Int) {
        ColumnText.showTextAligned(
            writer.directContent, Element.ALIGN_CENTER, Phrase(paragraph), 300f,
            (10 + (number * 20)).toFloat(), 0f
        )
    }

    private fun buildCompanyLine(): Paragraph {
        val company = Paragraph()
        company.alignment = Element.ALIGN_CENTER
        company.add(
            Chunk(
                "Betreiber: ${tenant.company.name} - ${tenant.company.street} ${tenant.company.houseNumber} - ${tenant.company.zipcode} ${tenant.company.city}",
                Font(Font.FontFamily.HELVETICA, 12f, Font.ITALIC)
            )
        )
        return company;
    }

    private fun buildTestCenterLine(): Paragraph {
        val testCenter = Paragraph()
        testCenter.alignment = Element.ALIGN_CENTER
        testCenter.add(
            Chunk(
                "${tenant.location.name} - ${tenant.location.street} ${tenant.location.houseNumber} - ${tenant.location.zipcode} ${tenant.location.city}",
                Font(Font.FontFamily.HELVETICA, 12f, Font.ITALIC)
            )
        )
        return testCenter;
    }


    /**
     * Add the footer to the document
     * Contains company / test center information and the date the document was created
     */
    override fun onEndPage(writer: PdfWriter, document: Document) {
        val font = Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL)
        val fontItalic = Font(font).apply { style = Font.ITALIC }

        //generate current date
        val currentDate = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))


        //document created on
        val createdOn = Paragraph()
        createdOn.spacingBefore = 20f
        createdOn.alignment = Element.ALIGN_CENTER
        createdOn.add(Chunk("Dokument automatisch erstellt am / created on: $currentDate", fontItalic))

        addComponentToFooter(writer, createdOn, 3)
        addComponentToFooter(writer, buildTestCenterLine(), 2)
        addComponentToFooter(writer, buildCompanyLine(), 1)


        /**
         * Add the page number and the amount of total pages to the footer
         */
        ColumnText.showTextAligned(
            writer.directContent,
            Element.ALIGN_CENTER,
            Phrase(document.pageNumber.toString() + "/" + writer.pageNumber),
            580f,
            30f,
            0f
        )
    }
}
