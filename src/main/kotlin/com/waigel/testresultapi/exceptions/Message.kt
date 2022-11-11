package com.waigel.testresultapi.exceptions

import java.util.Locale

enum class Message {
    RESOURCE_NOT_FOUND,
    BIRTHDATE_WRONG,
    FILE_NOT_FOUND,
    FILE_STORAGE_NOT_READY,
    PERMISSION_DENIED,
    ACCESS_CODE_INVALID,
    INVALID_REQUEST_PARAM;


    val code: String
        get() = name.uppercase(Locale.getDefault())
}
