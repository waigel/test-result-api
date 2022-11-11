package com.waigel.testresultapi.storage

interface FileStorage {
    fun readFile(storageFilePath: String): ByteArray
    fun deleteFile(storageFilePath: String)
    fun storeFile(storageFilePath: String, bytes: ByteArray)
    fun fileExists(storageFilePath: String): Boolean
}
