package com.mini.ecommerceapp.exceptions;

public class ValidationDetailsException extends ExceptionDetails {
    private String fields;
    private String fieldsMsg;

    public ValidationDetailsException() { }

    public ValidationDetailsException(String fields, String fieldsMsg) {
        this.fields = fields;
        this.fieldsMsg = fieldsMsg;
    }

    public ValidationDetailsException setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public ValidationDetailsException setFieldsMsg(String fieldsMsg) {
        this.fieldsMsg = fieldsMsg;
        return this;
    }

    public String getFields() {
        return fields;
    }

    public String getFieldsMsg() {
        return fieldsMsg;
    }
}
