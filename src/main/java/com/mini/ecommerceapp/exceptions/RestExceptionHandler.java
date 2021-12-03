package com.mini.ecommerceapp.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(buildExceptionDetails(status, ex), headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> map = new HashMap<>();
        fieldErrors.forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));

        return new ResponseEntity<>(
                new ValidationDetailsException()
                        .setFieldErrors(map)
                        .setTimeStamp(LocalDateTime.now())
                        .setTitle("Field Validation Error")
                        .setDetail("Check field(s) below")
                        .setStatus(HttpStatus.BAD_REQUEST.value())
                        .setDeveloperMessage(ex.getClass().getName()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.NOT_FOUND, ex),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleDataIntegrityViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.CONFLICT, ex),
                HttpStatus.CONFLICT
        );
    }

    public ExceptionDetails buildExceptionDetails(@NotNull HttpStatus httpStatus, @NotNull Exception ex) {
        return new ExceptionDetails()
                        .setTimeStamp(LocalDateTime.now())
                        .setStatus(httpStatus.value())
                        .setTitle(ex.getCause() != null ? ex.getCause().getMessage() : ex.getClass().getSimpleName())
                        .setDetail(ex.getMessage())
                        .setDeveloperMessage(ex.getClass().getName()
        );
    }
}
