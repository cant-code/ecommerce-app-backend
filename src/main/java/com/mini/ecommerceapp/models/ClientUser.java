package com.mini.ecommerceapp.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class ClientUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String username;
    @NotEmpty
    @NotNull
    @Length(min = 7)
    private String password;
    private Roles role;

    public ClientUser(@NotNull @NotEmpty String name, @NotNull @NotEmpty String username, @NotEmpty @NotNull @Length(min = 7) String password, Roles role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public ClientUser() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
