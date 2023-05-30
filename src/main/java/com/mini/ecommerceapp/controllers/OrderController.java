package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.exceptions.ValidationDetailsException;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.ORDER;

@RestController
@RequestMapping(ORDER)
@SecurityRequirement(name = "Bearer Auth")
@Tag(name = "Orders")
@ApiResponse(responseCode = "401", description = "Unauthorized", content = { @Content(schema = @Schema(hidden = true))})
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            description = "Place new order",
            summary = "Place new order"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully placed order"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationDetailsException.class))})
    })
    @PostMapping
    public Order placeOrder(@Valid @RequestBody OrderDTO order, Principal principal) {
        AccessToken token = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
        return orderService.addOrder(order, token.getPreferredUsername());
    }

    @Operation(
            summary = "Get order with id",
            description = "Get order with id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @GetMapping("/{id}")
    public Order getOrder(@Valid @PathVariable long id) {
        return orderService.getOrder(id);
    }

    @Operation(
            description = "Get all orders for current user",
            summary = "Get all orders"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success")
    })
    @GetMapping
    public List<Order> getOrders(Principal principal) {
        AccessToken token = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext().getToken();
        return orderService.getOrdersForUser(token.getPreferredUsername());
    }

    @Operation(
            description = "Checkout endpoint for order",
            summary = "Checkout"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Checked out"),
            @ApiResponse(responseCode = "400", description = "Validation Exception", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))}),
            @ApiResponse(responseCode = "404", description = "Resource Not Found", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionDetails.class))})
    })
    @PostMapping("/{id}/finish")
    public ResponseEntity<Order> updateStatus(@Valid @PathVariable long id) {
        return ResponseEntity.ok(orderService.updateStatus(id));
    }
}
