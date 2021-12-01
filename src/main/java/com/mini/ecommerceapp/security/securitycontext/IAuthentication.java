package com.mini.ecommerceapp.security.securitycontext;

import org.springframework.security.core.Authentication;

public interface IAuthentication {
    Authentication getAuthentication();
}
