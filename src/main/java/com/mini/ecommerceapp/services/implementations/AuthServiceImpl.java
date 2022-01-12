package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.dto.ProfileDTO;
import com.mini.ecommerceapp.dto.UserDTO;
import com.mini.ecommerceapp.exceptions.ResourceNotAvailableException;
import com.mini.ecommerceapp.services.AuthService;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.Http;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthzClient authzClient;
    private final KeycloakSpringBootProperties kcProperties;
    private final Keycloak keycloak;

    @Autowired
    public AuthServiceImpl(AuthzClient authzClient, KeycloakSpringBootProperties kcProperties, Keycloak keycloak) {
        this.authzClient = authzClient;
        this.kcProperties = kcProperties;
        this.keycloak = keycloak;
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
        Configuration kcConfig = authzClient.getConfiguration();
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

    @Override
    public void registerUser(UserDTO userDTO) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setTemporary(false);
        credential.setValue(userDTO.getPassword());
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(userDTO.getUsername());
        userRepresentation.setEmail(userDTO.getEmail());
        userRepresentation.setFirstName(userDTO.getFirstName());
        userRepresentation.setLastName(userDTO.getLastName());
        userRepresentation.setCredentials(List.of(credential));
        userRepresentation.setEnabled(true);
        Response response = keycloak.realm(kcProperties.getRealm()).users().create(userRepresentation);
        if (response.getStatus() != HttpStatus.CREATED.value()) {
            Map<String, String> map = response.readEntity(Map.class);
            throw new ResourceNotAvailableException("Could not create user: " + map.getOrDefault("errorMessage", ""));
        }
    }

    @Override
    public ProfileDTO getUser(AccessToken accessToken) {
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setEmail(accessToken.getEmail());
        profileDTO.setUsername(accessToken.getPreferredUsername());
        profileDTO.setFirstName(accessToken.getGivenName());
        profileDTO.setLastName(accessToken.getFamilyName());
        profileDTO.setRoles(accessToken.getRealmAccess().getRoles());
        return profileDTO;
    }
}
