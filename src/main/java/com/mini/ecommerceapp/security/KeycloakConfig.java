package com.mini.ecommerceapp.security;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
public class KeycloakConfig {
    @Bean
    public AuthzClient authzClient(KeycloakSpringBootProperties properties) {
        org.keycloak.authorization.client.Configuration configuration = new org.keycloak.authorization.client.Configuration(
                properties.getAuthServerUrl(),
                properties.getRealm(),
                properties.getResource(),
                properties.getCredentials(),
                null
        );
        return AuthzClient.create(configuration);
    }

    @Bean
    public Keycloak getKeycloak(KeycloakSpringBootProperties kcProperties) {
        return KeycloakBuilder.builder()
                .grantType(CLIENT_CREDENTIALS)
                .serverUrl(kcProperties.getAuthServerUrl())
                .realm(kcProperties.getRealm())
                .clientId(kcProperties.getResource())
                .clientSecret(kcProperties.getCredentials().get("secret").toString())
                .build();
    }

    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}
