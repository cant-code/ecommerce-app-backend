package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.UserDTO;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO: Registration
@RestController
public class AppController {
    private final AuthzClient authzClient;
    private final KeycloakSpringBootProperties kcProperties;

    @Autowired
    public AppController(AuthzClient authzClient, KeycloakSpringBootProperties kcProperties) {
        this.authzClient = authzClient;
        this.kcProperties = kcProperties;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        AccessTokenResponse response = authzClient.obtainAccessToken(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody ClientUser user) {
//        user.setRole(Roles.ROLE_USER);
//        clientUserService.saveUser(user);
//        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())).build();
//    }

    @GetMapping("/token/refresh")
    public AccessTokenResponse refreshToken(@RequestParam String token) {
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
