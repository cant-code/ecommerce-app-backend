package com.mini.ecommerceapp.exceptions;

import java.util.Map;

public class ValidationDetails extends ExceptionDetails {
    private Map<String, String> fieldErrors;

    public ValidationDetails() { }

    public ValidationDetails(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public ValidationDetails setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
        return this;
    }
}
