package com.waigel.testresultapi.exceptions

class FileStoreException(
    message: String,
    val storageFilePath: String,
    val e: Exception? = null
) : RuntimeException(message, e)
