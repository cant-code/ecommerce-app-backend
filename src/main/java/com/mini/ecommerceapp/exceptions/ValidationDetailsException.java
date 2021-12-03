package com.mini.ecommerceapp.exceptions;

import java.util.Map;

public class ValidationDetailsException extends ExceptionDetails {
    private Map<String, String> fieldErrors;

    public ValidationDetailsException() { }

    public ValidationDetailsException(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public ValidationDetailsException setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }
}
