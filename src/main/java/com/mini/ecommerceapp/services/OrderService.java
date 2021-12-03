package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.models.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderService {
    List<Order> getOrders();
    List<Order> getOrdersForUser();
    Order getOrder(long id);
    Order addOrder(OrderDTO order);
    Map<Long, Long> getOrderCount(LocalDateTime dateTime);
    void updateStatus(long id);
}
