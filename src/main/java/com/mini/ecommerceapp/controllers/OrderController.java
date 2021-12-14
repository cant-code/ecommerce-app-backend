package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.services.OrderService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.ORDER;

@RestController
@RequestMapping(ORDER)
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(@Valid @RequestBody OrderDTO order, Principal principal) {
        AccessToken token = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
        return orderService.addOrder(order, token.getPreferredUsername());
    }

    @GetMapping("/{id}")
    public Order getOrder(@Valid @PathVariable long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> getOrders(Principal principal) {
        AccessToken token = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
        return orderService.getOrdersForUser(token.getPreferredUsername());
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<Order> updateStatus(@Valid @PathVariable long id) {
        return ResponseEntity.ok(orderService.updateStatus(id));
    }
}
