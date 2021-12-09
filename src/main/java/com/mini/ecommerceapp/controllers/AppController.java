package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.UserDTO;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private final AuthzClient authzClient;

    @Autowired
    public AppController(AuthzClient authzClient) {
        this.authzClient = authzClient;
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

//    @GetMapping("/token/refresh")
//    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String authHeader = request.getHeader(AUTHORIZATION);
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            try {
//                String token = authHeader.substring("Bearer ".length());
//                Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET"));
//                JWTVerifier verifier = JWT.require(algorithm).build();
//                DecodedJWT decodedJWT = verifier.verify(token);
//                String username = decodedJWT.getSubject();
//                ClientUser user = clientUserService.getUser(username);
//                Map<String, String> tokens = jwtUtil.getJWTTokens(request.getRequestURL().toString(), user);
//                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                Map<String, String> tokens = Map.of(
//                        "time_stamp", LocalDateTime.now().toString(),
//                        "status", HttpStatus.FORBIDDEN.toString(),
//                        "title", "JWT Error",
//                        "detail", e.getMessage(),
//                        "developerMessage", e.getClass().getName()
//                );
//                response.setContentType(APPLICATION_JSON_VALUE);
//                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//            }
//        } else {
//            throw new RuntimeException("Refresh token is missing");
//        }
//    }
}
