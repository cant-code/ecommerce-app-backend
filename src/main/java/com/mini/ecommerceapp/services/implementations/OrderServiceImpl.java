package com.mini.ecommerceapp.services.implementations;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.models.ClientUser;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.OrderRepository;
import com.mini.ecommerceapp.security.securitycontext.IAuthentication;
import com.mini.ecommerceapp.services.ClientUserService;
import com.mini.ecommerceapp.services.OrderService;
import com.mini.ecommerceapp.services.VehicularSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final IAuthentication authentication;
    private final OrderRepository orderRepository;
    private final ClientUserService clientUserService;
    private final VehicularSpaceService vehicularSpaceService;

    @Autowired
    public OrderServiceImpl(IAuthentication authentication, OrderRepository orderRepository, ClientUserService clientUserService, VehicularSpaceService vehicularSpaceService) {
        this.authentication = authentication;
        this.orderRepository = orderRepository;
        this.clientUserService = clientUserService;
        this.vehicularSpaceService = vehicularSpaceService;
    }

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersForUser() {
        return orderRepository.findByUser_Username(authentication.getAuthentication().getName());
    }

    @Override
    public Order getOrder(long id) {
        return orderRepository.getById(id);
    }

    @Override
    public Order addOrder(OrderDTO order) {
        ClientUser clientUser = clientUserService.getUser(authentication.getAuthentication().getName());
        VehicularSpace space = vehicularSpaceService.getVehicularSpace(order.getVehicularSpace().getId());
        order.setVehicularSpace(space);
        return orderRepository.save(new Order(clientUser, order));
    }

    @Override
    public Map<Long, Long> getOrderCount(LocalDateTime dateTime) {
        return orderRepository.countOrdersMap(dateTime);
    }

    @Override
    public void updateStatus(long id) {
        orderRepository.updateStatus(id, Status.EXPIRED);
    }
}
