package com.waigel.testresultapi.services

import com.waigel.testresultapi.entities.TestResult
import com.waigel.testresultapi.entities.UploadedDocument
import com.waigel.testresultapi.repositories.UploadedDocumentRepository
import com.waigel.testresultapi.storage.FileStorage
import com.waigel.testresultapi.utils.CryptoHelper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.concurrent.ThreadLocalRandom
import javax.crypto.SecretKey
import javax.transaction.Transactional
import kotlin.streams.asSequence

@Service
class DocumentUploadService(
    private val fileStorage: FileStorage,
    private val uploadedDocumentRepository: UploadedDocumentRepository
) {
    private val logger = LoggerFactory.getLogger(DocumentUploadService::class.java)

    companion object {
        const val UPLOADED_CERTIFICATES_STORAGE_FOLDER_NAME = "certificates"
    }

    @Transactional
    fun store(document: ByteArrayOutputStream, testResult: TestResult, encryptionKey: SecretKey): UploadedDocument {
        val uploadedDocumentEntity = UploadedDocument(generateFilename(), testResult)
            .apply { extension = "pdf" }
        logger.info("Storing document for test ${testResult.testId} in ${uploadedDocumentEntity.filePath}")
        save(uploadedDocumentEntity)
        fileStorage.storeFile(
            uploadedDocumentEntity.filePath,
            CryptoHelper.encryptFile(document, encryptionKey).toByteArray()
        )
        return uploadedDocumentEntity
    }

    @Transactional
    fun delete(uploadedImage: UploadedDocument) {
        fileStorage.deleteFile(uploadedImage.filePath)
        uploadedDocumentRepository.delete(uploadedImage)
    }


    private fun generateFilename(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return ThreadLocalRandom.current()
            .ints(100L, 0, charPool.size)
            .asSequence()
            .map(charPool::get)
            .joinToString("")
    }

    fun save(document: UploadedDocument): UploadedDocument {
        return uploadedDocumentRepository.save(document)
    }

    fun retrieve(uploadedDocument: UploadedDocument, secretKey: SecretKey): ByteArray {
        logger.info("Retrieving document for test ${uploadedDocument.testResult.testId} from ${uploadedDocument.filePath}")
        val document = fileStorage.readFile(uploadedDocument.filePath)
        return CryptoHelper.decryptFile(document, secretKey)

    }

    val UploadedDocument.filePath
        get() = "$UPLOADED_CERTIFICATES_STORAGE_FOLDER_NAME/" + this.filenameWithExtension
}
