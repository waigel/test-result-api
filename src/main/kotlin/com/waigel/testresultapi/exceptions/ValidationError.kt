package com.waigel.testresultapi.exceptions

class ValidationError(val type: ValidationErrorType, val message: Message, vararg val parameters: String)
