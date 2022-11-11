package com.waigel.testresultapi.storage

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.waigel.testresultapi.configurations.S3FileStorageConfig
import com.waigel.testresultapi.exceptions.FileStoreException
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream

class S3FileStorage(
    config: S3FileStorageConfig,
    private val s3: AmazonS3,
) : FileStorage {

    private val logger = LoggerFactory.getLogger(S3FileStorage::class.java)

    private val bucketName = config.bucketName

    override fun readFile(storageFilePath: String): ByteArray {
        try {
            return s3.getObject(bucketName, storageFilePath).objectContent.readAllBytes()
        } catch (e: Exception) {
            logger.error("Error while reading file from S3", e)
            throw FileStoreException("Can not obtain file", storageFilePath, e)
        }
    }

    override fun deleteFile(storageFilePath: String) {
        try {
            s3.deleteObject(bucketName, storageFilePath)
            return
        } catch (e: Exception) {
            logger.error("Error while deleting file from S3", e)
            throw FileStoreException("Can not delete file using s3 bucket!", storageFilePath, e)
        }
    }

    override fun storeFile(storageFilePath: String, bytes: ByteArray) {
        val byteArrayInputStream = ByteArrayInputStream(bytes)
        val meta = ObjectMetadata()
        meta.contentLength = bytes.size.toLong()
        val putObjectRequest = PutObjectRequest(
            bucketName,
            storageFilePath,
            byteArrayInputStream, meta
        )
        try {
            s3.putObject(putObjectRequest)
        } catch (e: Exception) {
            logger.error("Error while storing file to S3", e)
            throw FileStoreException("Can not store file using s3 bucket!", storageFilePath, e)
        }
        return
    }

    override fun fileExists(storageFilePath: String): Boolean {
        return s3.doesObjectExist(bucketName, storageFilePath)
    }
}
