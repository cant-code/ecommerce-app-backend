package com.mini.ecommerceapp.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.models.ClientUser;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Roles;
import com.mini.ecommerceapp.security.securitycontext.IAuthentication;
import com.mini.ecommerceapp.services.ClientUserService;
import com.mini.ecommerceapp.services.OrderService;
import com.mini.ecommerceapp.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AppController {
    private final ClientUserService clientUserService;
    private final IAuthentication authentication;
    private final OrderService orderService;
    private final JWTUtil jwtUtil;

    @Autowired
    public AppController(ClientUserService clientUserService, IAuthentication authentication, OrderService orderService, JWTUtil jwtUtil) {
        this.clientUserService = clientUserService;
        this.authentication = authentication;
        this.orderService = orderService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody ClientUser user) {
        user.setRole(Roles.ROLE_USER);
        clientUserService.saveUser(user);
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString())).build();
    }

    @PostMapping("/order")
    public Order placeOrder(@Valid @RequestBody OrderDTO order) {
        return orderService.addOrder(order);
    }

    @GetMapping("/order/{id}")
    public Order getOrder(@Valid @PathVariable long id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.getOrdersForUser();
    }

    @GetMapping("/user")
    public Object getUser() {
        return authentication.getAuthentication();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(System.getenv("SECRET"));
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(token);
                String username = decodedJWT.getSubject();
                ClientUser user = clientUserService.getUser(username);
                Map<String, String> tokens = jwtUtil.getJWTTokens(request.getRequestURL().toString(), user);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> tokens = Map.of(
                        "time_stamp", LocalDateTime.now().toString(),
                        "status", HttpStatus.FORBIDDEN.toString(),
                        "title", "JWT Error",
                        "detail", e.getMessage(),
                        "developerMessage", e.getClass().getName()
                );
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
