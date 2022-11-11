package com.waigel.testresultapi.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN)
data class PermissionDeniedException(val msg: Message? = Message.BIRTHDATE_WRONG) : RuntimeException()
