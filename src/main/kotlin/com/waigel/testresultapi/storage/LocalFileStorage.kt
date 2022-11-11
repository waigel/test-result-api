package com.waigel.testresultapi.storage

import com.waigel.testresultapi.configurations.LocalFileStorageConfig
import com.waigel.testresultapi.exceptions.FileStoreException
import java.io.File
import java.io.FileNotFoundException


class LocalFileStorage(
    config: LocalFileStorageConfig,
) : FileStorage {

    private val localDataPath = config.fsDataPath

    override fun readFile(storageFilePath: String): ByteArray {
        try {
            return getLocalFile(storageFilePath).readBytes()
        } catch (e: Exception) {
            throw FileNotFoundException("Can not obtain file$storageFilePath")
        }
    }

    override fun deleteFile(storageFilePath: String) {
        try {
            getLocalFile(storageFilePath).delete()
        } catch (e: Exception) {
            throw FileStoreException("Can not delete file from local filesystem!", storageFilePath, e)
        }
    }

    override fun storeFile(storageFilePath: String, bytes: ByteArray) {
        val file = getLocalFile(storageFilePath)
        try {
            file.parentFile.mkdirs()
            file.writeBytes(bytes)
        } catch (e: Exception) {
            throw FileStoreException("Can not store file to local filesystem!", storageFilePath, e)
        }
    }

    override fun fileExists(storageFilePath: String): Boolean {
        return getLocalFile(storageFilePath).exists()
    }

    private fun getLocalFile(storageFilePath: String): File {
        return File("$localDataPath/$storageFilePath")
    }
}