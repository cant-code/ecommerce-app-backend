package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.dto.UserDTO;
import com.mini.ecommerceapp.services.AuthService;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthzClient authzClient;
    private final KeycloakSpringBootProperties kcProperties;

    @Autowired
    public AuthServiceImpl(AuthzClient authzClient, KeycloakSpringBootProperties kcProperties) {
        this.authzClient = authzClient;
        this.kcProperties = kcProperties;
    }

    @Override
    public AccessTokenResponse authenticateUser(UserDTO userDTO) {
        return authzClient.authorization(userDTO.getUsername(), userDTO.getPassword(), "offline_access").authorize();
    }

    @Override
    public AccessTokenResponse refreshToken(String token) {
        final String url = kcProperties.getAuthServerUrl() + "/realms/" + kcProperties.getRealm() + "/protocol/openid-connect/token";
        final String clientId = kcProperties.getResource();
        final String secret = (String) kcProperties.getCredentials().get("secret");
        Configuration kcConfig = new Configuration(
                kcProperties.getAuthServerUrl(),
                kcProperties.getRealm(),
                kcProperties.getResource(),
                kcProperties.getCredentials(),
                null
        );
        Http http = new Http(kcConfig, (params, headers) -> {});

        return http.<AccessTokenResponse>post(url)
                .authentication().client()
                .form()
                .param("grant_type", "refresh_token")
                .param("refresh_token", token)
                .param("client_id", clientId)
                .param("client_secret", secret)
                .response()
                .json(AccessTokenResponse.class)
                .execute();
    }
}
