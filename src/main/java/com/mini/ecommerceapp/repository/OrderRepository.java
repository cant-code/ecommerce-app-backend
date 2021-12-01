package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndExpiry(String s, LocalDateTime dateTime);
    List<Order> findByUser_Username(String username);
}
