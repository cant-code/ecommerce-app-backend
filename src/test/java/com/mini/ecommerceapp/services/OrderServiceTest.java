package com.mini.ecommerceapp.services;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.exceptions.ResourceNotAvailableException;
import com.mini.ecommerceapp.exceptions.ResourceNotFoundException;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.OrderRepository;
import com.mini.ecommerceapp.services.implementations.OrderServiceImpl;
import com.mini.ecommerceapp.utils.MockData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.mini.ecommerceapp.utils.Constants.ORDER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private VehicularSpaceService vehicularSpaceService;

    @BeforeEach
    public void init() {
        orderService = new OrderServiceImpl(orderRepository, vehicularSpaceService);
    }

    @Test
    @DisplayName("Find all orders")
    void findAllOrders() {
        List<Order> orders = List.of(MockData.buildOrder());

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> orderList = orderService.getOrders();

        assertEquals(orders, orderList);
    }

    @Test
    @DisplayName("Get orders for user")
    void getOrdersForUser() {
        List<Order> orders = List.of(MockData.buildOrder());

        when(orderRepository.findByUserId(anyString())).thenReturn(orders);

        List<Order> orderList = orderService.getOrdersForUser(RandomStringUtils.random(20));

        assertEquals(orders, orderList);
    }

    @Test
    @DisplayName("Get order using id")
    void getOrderUsingId() {
        Order mockOrder = MockData.buildOrder();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        Order order = orderService.getOrder(RandomUtils.nextLong());

        assertEquals(mockOrder, order);
    }

    @Test
    @DisplayName("Dont Get order using id")
    void dontGetOrderUsingId() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrder(1));

        assertEquals(ORDER_NOT_FOUND, e.getMessage());
    }

    @Test
    @DisplayName("Add order for user")
    void addOrderForUser() {
        Order mockOrder = MockData.buildOrder();
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();
        vehicularSpace.setTotalSlots(3);

        when(vehicularSpaceService.getVehicularSpace(anyLong())).thenReturn(vehicularSpace);
        when(orderRepository.countOrdersByItems_IdAndStartLessThanEqualAndStartLessThanAndStatus(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Status.class)))
                .thenReturn(2L);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order order = orderService.addOrder(new OrderDTO(LocalDateTime.now(), LocalDateTime.now(), vehicularSpace), "username");

        assertEquals(mockOrder, order);
    }

    @Test
    @DisplayName("Dont add order for user when no space left")
    void dontAddOrder() {
        VehicularSpace vehicularSpace = MockData.buildVehicularSpace();
        vehicularSpace.setTotalSlots(2);

        when(vehicularSpaceService.getVehicularSpace(anyLong())).thenReturn(vehicularSpace);
        when(orderRepository.countOrdersByItems_IdAndStartLessThanEqualAndStartLessThanAndStatus(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), any(Status.class)))
                .thenReturn(2L);

        OrderDTO order = new OrderDTO(LocalDateTime.now(), LocalDateTime.now(), vehicularSpace);
        ResourceNotAvailableException e = assertThrows(ResourceNotAvailableException.class, () ->
                orderService.addOrder(order, "username"));

        assertEquals("Space Booked full", e.getMessage());
    }

    @Test
    @DisplayName("Get Order count")
    void getOrderCount() {
        when(orderRepository.countOrdersMap(any(LocalDateTime.class), any(LocalDateTime.class), anyLong()))
                .thenReturn(Map.of(1L, 1L));

        Map<Long, Long> map = orderService.getOrderCount(LocalDateTime.now(), LocalDateTime.now());

        assertEquals(Map.of(1L, 1L), map);
    }

    @Test
    @DisplayName("Get Order count for parking space")
    void getOrderCountForParkingSpace() {
        when(orderRepository.countOrdersMap(any(LocalDateTime.class), any(LocalDateTime.class), anyLong()))
                .thenReturn(Map.of(1L, 1L));

        Map<Long, Long> map = orderService.getOrderCountForParkingSpace(LocalDateTime.now(), LocalDateTime.now(), 1L);

        assertEquals(Map.of(1L, 1L), map);
    }

    @Test
    @DisplayName("Update order status")
    void updateOrderStatus() {
        Order mockOrder = MockData.buildOrder();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any())).thenReturn(mockOrder);

        Order order = orderService.updateStatus(RandomUtils.nextLong());

        assertEquals(mockOrder, order);
    }

    @Test
    @DisplayName("Dont update order status when order not found")
    void dontUpdateOrderStatus() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> orderService.updateStatus(1L));

        assertEquals(ORDER_NOT_FOUND, e.getMessage());
    }
}