package com.alooa.swish.exception;

import com.alooa.swish.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 Not Found: no shot with this id
    @ExceptionHandler(ShotsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShotsNotFoundException(ShotsNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), List.of(ex.getMessage()), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 400 Bad Request: breaks a business rule (e.g. shotsMade > shotsTaken)
    @ExceptionHandler(InvalidShotsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidShotsException(InvalidShotsException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(ex.getMessage()), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 400 Bad Request: failed Bean Validation (@Valid), one error per bad field
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .toList();
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorList, "Invalid input");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 400 Bad Request: body missing, broken JSON, or wrong value types
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of("Request body is missing, sent invalid JSON, or field(s) have wrong value types"), "Malformed JSON request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 400 Bad Request: path variable has the wrong type (e.g. /shots/abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String errorMessage = String.format("Parameter '%s' has invalid value '%s' and must be a number.", ex.getName(), ex.getValue());
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), List.of(errorMessage), "Invalid parameter value");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 404 Not Found: no endpoint at this URL
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), List.of("No endpoint found at this URL"), "Resource not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 405 Method Not Allowed: endpoint exists, but not for this HTTP method (e.g. PUT)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String errorMessage = String.format("Method '%s' is not supported. Supported methods are: %s", ex.getMethod(), Arrays.toString(ex.getSupportedMethods()));
        ErrorResponse error = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), List.of(errorMessage), "HTTP method not allowed");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    // 415 Unsupported Media Type: Content-Type is not JSON (e.g. text/plain)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String errorMessage = String.format("Content type '%s' is not supported. Supported content types are: %s", ex.getContentType(), ex.getSupportedMediaTypes());
        ErrorResponse error = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), List.of(errorMessage), "Unsupported media type");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(error);
    }

    // 500 Internal Server Error: catch-all for anything unexpected (logged)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), List.of("An unexpected error occurred"), "Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
