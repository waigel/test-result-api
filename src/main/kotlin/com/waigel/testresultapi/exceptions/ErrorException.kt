package com.waigel.testresultapi.exceptions

import org.springframework.http.HttpStatus
import java.io.Serializable

abstract class ErrorException : RuntimeException {
    private val params: List<Serializable>?
    private val code: String

    constructor(message: Message, params: List<Serializable>?) : super(message.code) {
        this.params = params
        this.code = message.code
    }

    constructor(message: Message) : this(message, null)

    constructor(code: String, params: List<Serializable>? = null) {
        this.code = code
        this.params = params
    }

    val errorResponseBody: ErrorResponseBody
        get() = ErrorResponseBody(this.code, params)
    abstract val httpStatus: HttpStatus
}
