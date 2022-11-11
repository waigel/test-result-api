package com.waigel.testresultapi.exceptions

import com.auth0.jwt.exceptions.JWTDecodeException
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.io.FileNotFoundException
import java.util.Collections
import java.util.function.Consumer
import javax.persistence.EntityNotFoundException


@RestControllerAdvice
class ExceptionHandlers {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<Map<String, Map<String, String>>> {
        val errors: MutableMap<String, String> = HashMap()
        ex.bindingResult.allErrors.forEach(
            Consumer { error: ObjectError ->
                val fieldName = (error as FieldError).field
                val errorMessage = error.getDefaultMessage()
                errors[fieldName] = errorMessage ?: ""
            }
        )
        return ResponseEntity(
            Collections.singletonMap<String, Map<String, String>>(ValidationErrorType.STANDARD_VALIDATION.name, errors),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentTypeMismatchException
    ): ResponseEntity<ErrorResponseBody> {
        val errors = Collections.singletonMap(ex.parameter, ex.message)
        return ResponseEntity(ErrorResponseBody(Message.INVALID_REQUEST_PARAM.code, null), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(FileNotFoundException::class)
    fun handleFileNotFoundExceptions(
        ex: FileNotFoundException
    ): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.FILE_NOT_FOUND.code, null), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(PermissionDeniedException::class)
    fun handlePermissionDeniedException(
        ex: PermissionDeniedException
    ): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.PERMISSION_DENIED.code, null), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(JWTDecodeException::class)
    fun handleJWTDecodeException(
        ex: JWTDecodeException
    ): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.ACCESS_CODE_INVALID.code, null), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(FileStoreException::class)
    fun handleFileStorageExceptions(
        ex: FileStoreException
    ): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.FILE_STORAGE_NOT_READY.code, null), HttpStatus.BAD_GATEWAY)
    }


    @ExceptionHandler(ValidationException::class)
    fun handleCustomValidationExceptions(
        ex: ValidationException
    ): ResponseEntity<Map<String, Map<String, List<String>>>> {
        val errors: MutableMap<String, List<String>> = HashMap()
        for (validationError in ex.validationErrors) {
            errors[validationError.message.code] = listOf(*validationError.parameters)
        }
        return ResponseEntity(
            Collections.singletonMap<String, Map<String, List<String>>>(
                ValidationErrorType.CUSTOM_VALIDATION.name,
                errors
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleCMissingServletRequestParameterException(
        ex: MissingServletRequestParameterException
    ): ResponseEntity<Map<String, Map<String, String?>>> {
        val errors = Collections.singletonMap(ex.parameterName, ex.message)
        return ResponseEntity(
            Collections.singletonMap(ValidationErrorType.STANDARD_VALIDATION.name, errors), HttpStatus.BAD_REQUEST
        )
    }

    @ApiResponse(
        responseCode = "400",
        content = [
            Content(
                schema = Schema(
                    example = """{"code": "you_did_something_wrong", "params": ["something", "wrong"]}"""
                )
            )
        ]
    )
    @ExceptionHandler(ErrorException::class)
    fun handleServerError(ex: ErrorException): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ex.errorResponseBody, ex.httpStatus)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleServerError(ex: EntityNotFoundException?): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(Message.RESOURCE_NOT_FOUND.code, null), HttpStatus.NOT_FOUND)
    }


    @ApiResponse(
        responseCode = "404",
        content = [
            Content(
                schema = Schema(
                    example = """{"code": "resource_not_found", "params": null}"""
                )
            )
        ]
    )
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException): ResponseEntity<ErrorResponseBody> {
        return ResponseEntity(ErrorResponseBody(ex.msg!!.code, null), HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleFileSizeLimitExceeded(ex: HttpRequestMethodNotSupportedException): ResponseEntity<Void> {
        return ResponseEntity(HttpStatus.METHOD_NOT_ALLOWED)
    }

    @ExceptionHandler(Throwable::class)
    fun handleOtherExceptions(ex: Throwable): ResponseEntity<ErrorResponseBody> {
        logger.error(ex.stackTraceToString())
        return ResponseEntity(
            ErrorResponseBody(
                "unexpected_error_occurred",
                listOf(ex::class.java.name)
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
