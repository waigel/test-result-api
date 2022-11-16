package com.waigel.testresultapi.exceptions

import java.util.Locale

enum class Message {
    RESOURCE_NOT_FOUND,
    BIRTHDATE_WRONG,
    FILE_NOT_FOUND,
    FILE_STORAGE_NOT_READY,
    PERMISSION_DENIED,
    ACCESS_CODE_INVALID,
    ACCESS_CODE_EXPIRED,
    INVALID_REQUEST_PARAM,
    MISSING_REQUEST_HEADER,
    CAPTCHA_VALIDATION_FAILED;


    val code: String
        get() = name.uppercase(Locale.getDefault())
}
