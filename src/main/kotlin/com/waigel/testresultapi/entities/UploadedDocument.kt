package com.waigel.testresultapi.entities

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["filename"], name = "uploaded_document_filename")])
class UploadedDocument(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID = UUID.randomUUID(),

    var filename: String,

    @OneToOne
    var testResult: TestResult
) : AuditModel() {

    var extension: String = "pdf"

    val filenameWithExtension
        get() = "$filename.$extension"

    constructor(filename: String, testResult: TestResult) : this(UUID.randomUUID(), filename, testResult)
}
