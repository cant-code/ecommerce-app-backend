package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.controllers.RequestFormat.OrderRequest;
import com.mini.ecommerceapp.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    List<Order> getOrdersForUser();
    Order getOrder(long id);
    Order addOrder(OrderRequest order);
    void releaseResources(LocalDateTime dateTime);
}
