package com.waigel.testresultapi.exceptions

import java.io.Serializable

class ErrorResponseBody(var code: String, var params: List<Serializable>?)
