package com.mini.ecommerceapp.dto;

import io.swagger.v3.oas.annotations.Parameter;

import javax.validation.constraints.Email;

public class UserDTO extends BaseUserDTO {
    @Parameter
    private String firstName;
    @Parameter
    private String lastName;
    @Parameter
    @Email
    private String email;

    public UserDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
