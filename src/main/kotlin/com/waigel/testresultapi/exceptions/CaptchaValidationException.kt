package com.waigel.testresultapi.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN)
data class CaptchaValidationException(val msg: Message? = Message.RESOURCE_NOT_FOUND) : RuntimeException()
