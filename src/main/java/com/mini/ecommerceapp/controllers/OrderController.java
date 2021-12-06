package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public Order placeOrder(@Valid @RequestBody OrderDTO order) {
        return orderService.addOrder(order);
    }

    @GetMapping("/{id}")
    public Order getOrder(@Valid @PathVariable long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getOrdersForUser();
    }

    @PostMapping("/{id}/finish")
    public ResponseEntity<?> updateStatus(@Valid @PathVariable long id) {
        orderService.updateStatus(id);
        return ResponseEntity.ok().build();
    }
}
