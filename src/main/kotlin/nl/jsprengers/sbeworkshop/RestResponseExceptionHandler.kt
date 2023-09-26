package nl.jsprengers.sbeworkshop

import nl.jsprengers.sbeworkshop.model.RestError
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class RestResponseExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [(Exception::class)])
    fun handleGenericException(ex: Exception, request: WebRequest): ResponseEntity<RestError> {
        return ResponseEntity.badRequest().body(RestError(ex.message ?: "unknown error"))
    }
}
