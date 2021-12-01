package com.mini.ecommerceapp.utils;

import com.auth0.jwt.algorithms.Algorithm;
import com.mini.ecommerceapp.models.ClientUser;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    public Map<String, String> getJWTTokens(String issuer, @NotNull User user) {
        return buildJWT(issuer, user.getUsername(), user.getAuthorities().stream().findFirst().get().toString());
    }

    public Map<String, String> getJWTTokens(String issuer, @NotNull ClientUser user) {
        return buildJWT(issuer, user.getUsername(), user.getRole().toString());
    }

    public Map<String, String> buildJWT(String issuer, String username, String roles) {
        Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET"));
        String access_token = com.auth0.jwt.JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
        String refresh_token = com.auth0.jwt.JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        return tokens;
    }
}
