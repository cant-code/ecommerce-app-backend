package com.mini.ecommerceapp.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.exception.ConstraintViolationException;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.io.IOException;
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
        return buildValidationDetails(ex);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildValidationDetails(ex);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.NOT_FOUND, ex),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionDetails> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.FORBIDDEN, ex),
                HttpStatus.FORBIDDEN
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleDataIntegrityViolationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.CONFLICT, ex),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(ResourceNotAvailableException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotAvailableException(ResourceNotAvailableException ex) {
        return new ResponseEntity<>(
                buildExceptionDetails(HttpStatus.CONFLICT, ex),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<ExceptionDetails> handleHttpResponseException(HttpResponseException ex) throws IOException {
        return new ResponseEntity<>(
                new ExceptionDetails()
                        .setTimeStamp(LocalDateTime.now())
                        .setStatus(ex.getStatusCode())
                        .setTitle(ex.getMessage())
                        .setDetail(new ObjectMapper().readValue(ex.getBytes(), Map.class).get("error_description").toString())
                        .setDeveloperMessage(ex.getClass().getName())
                ,
                HttpStatus.valueOf(ex.getStatusCode())
        );
    }

    public ResponseEntity<Object> buildValidationDetails(Exception ex) {
        Map<String, String> map = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
            fieldErrors.forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        } else if (ex instanceof BindException) {
            BindException e = (BindException) ex;
            List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
            fieldErrors.forEach(fieldError -> map.put(fieldError.getField(), fieldError.getDefaultMessage()));
        }

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
