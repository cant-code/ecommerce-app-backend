package com.mini.ecommerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceNotAvailableException extends RuntimeException {
    public ResourceNotAvailableException(String msg) { super(msg);}
}
