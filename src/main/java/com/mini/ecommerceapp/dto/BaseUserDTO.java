package com.mini.ecommerceapp.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

public class BaseUserDTO {
    @NotBlank
    private String username;
    @NotBlank
    @Length(min = 8, max = 20)
    private String password;

    public BaseUserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
