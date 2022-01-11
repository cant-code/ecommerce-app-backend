package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.BaseUserDTO;
import com.mini.ecommerceapp.dto.UserDTO;
import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.exceptions.ValidationDetailsException;
import com.mini.ecommerceapp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@Tag(name = "Auth")
public class AppController {
    private final AuthService authService;

    @Autowired
    public AppController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/user")
    public AccessToken getUser(Principal principal) {
        return ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
    }

    @Operation(
            description = "Login",
            summary = "Login"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationDetailsException.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> loginUser(
            @Parameter(schema = @Schema(implementation = BaseUserDTO.class))
            @RequestBody @Valid UserDTO userDTO
    ) {
        return ResponseEntity.ok(authService.authenticateUser(userDTO));
    }

    @Operation(
            description = "Register new user",
            summary = "Register"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationDetailsException.class))}),
            @ApiResponse(responseCode = "409", description = "Resource Not Available", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO) {
        authService.registerUser(userDTO);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())).build();
    }

    @Operation(
            description = "Refresh Token",
            summary = "Refresh Token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = { @Content(schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "401", description = "Validation Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationDetailsException.class))}),
    })
    @GetMapping("/token/refresh")
    public AccessTokenResponse refreshToken(@RequestParam String token) {
        return authService.refreshToken(token);
    }
}
