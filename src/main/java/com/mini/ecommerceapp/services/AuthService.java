package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.dto.UserDTO;
import org.keycloak.representations.AccessTokenResponse;

public interface AuthService {
    AccessTokenResponse authenticateUser(UserDTO userDTO);
    AccessTokenResponse refreshToken(String token);
    void registerUser(UserDTO userDTO);
}
