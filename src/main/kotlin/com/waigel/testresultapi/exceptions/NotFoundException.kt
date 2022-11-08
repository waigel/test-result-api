package com.waigel.testresultapi.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
data class NotFoundException(val msg: Message? = Message.RESOURCE_NOT_FOUND) : RuntimeException()
