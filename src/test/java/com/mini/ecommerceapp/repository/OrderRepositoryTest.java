package com.mini.ecommerceapp.repository;

import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.ParkingSpace;
import com.mini.ecommerceapp.models.Status;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicularSpaceRepository vehicularSpaceRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Test
    @DisplayName("Find Orders using userId")
    void findOrdersUsingUserId() {
        Order order = saveDummyOrder();

        List<Order> orders = orderRepository.findByUserId(order.getUserId());

        assertEquals(1, orders.size());
        assertEquals(order, orders.get(0));
    }

    @Test
    @DisplayName("Dont find Orders using userId")
    void dontFindOrdersUsingUserId() {
        List<Order> orders = orderRepository.findByUserId(RandomStringUtils.random(20));

        assertEquals(0, orders.size());
    }

    @Test
    @DisplayName("Count orders using item, start time, end time and status")
    void countOrders() {
        Order order = saveDummyOrder();

        long count = orderRepository.countOrdersByItems_IdAndStartLessThanEqualAndStartLessThanAndStatus(order.getItems().getId(),
                order.getStart(), order.getStart().plusHours(1), order.getStatus());
        
        assertEquals(1, count);
    }

    @Test
    @DisplayName("Count 0 orders using item, start time, end time and status")
    void count0Orders() {
        long count = orderRepository.countOrdersByItems_IdAndStartLessThanEqualAndStartLessThanAndStatus(
                RandomUtils.nextLong(), LocalDateTime.now(), LocalDateTime.now(), Status.CONFIRMED);

        assertEquals(0, count);
    }

    @Test
    @DisplayName("Count Orders without id")
    void countOrdersWithoutId() {
        Order order = saveDummyOrder();

        Map<Long, Long> map = orderRepository.countOrdersMap(LocalDateTime.now(), LocalDateTime.now().plusHours(1), -1);

        assertTrue(map.containsKey(order.getItems().getId()));
        assertEquals(1, map.get(order.getItems().getId()));
    }

    @Test
    @DisplayName("Count 0 Orders without id")
    void count0OrdersWithoutId() {
        Map<Long, Long> map = orderRepository.countOrdersMap(LocalDateTime.now().plusHours(20), LocalDateTime.now().plusHours(30), -1);

        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Count 0 Orders with id")
    void count0OrdersWithId() {
        Map<Long, Long> map = orderRepository.countOrdersMap(LocalDateTime.now().plusHours(20), LocalDateTime.now().plusHours(30),
                999999);

        assertTrue(map.isEmpty());
    }

    private Order saveDummyOrder() {
        Order order = MockData.buildOrder();

        ParkingSpace parkingSpace = MockData.buildParkingSpace();
        parkingSpaceRepository.save(parkingSpace);
        order.getItems().setParkingSpace(parkingSpace);

        vehicularSpaceRepository.save(order.getItems());
        return orderRepository.save(order);
    }
}