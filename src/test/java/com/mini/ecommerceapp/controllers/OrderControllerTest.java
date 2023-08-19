package com.mini.ecommerceapp.controllers;

import com.mini.ecommerceapp.dto.OrderDTO;
import com.mini.ecommerceapp.exceptions.ExceptionDetails;
import com.mini.ecommerceapp.models.Order;
import com.mini.ecommerceapp.models.Status;
import com.mini.ecommerceapp.models.VehicularSpace;
import com.mini.ecommerceapp.repository.OrderRepository;
import com.mini.ecommerceapp.repository.VehicularSpaceRepository;
import com.mini.ecommerceapp.utils.MockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static com.mini.ecommerceapp.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    private WebTestClient webTestClient;

    @Mock
    private MockHttpServletRequest mockRequest;

    @Autowired
    private OrderRepository repository;

    @Autowired
    private VehicularSpaceRepository vehicularSpaceRepository;

    private VehicularSpace globalVS;
    private Order globalOrder;

    private final String USER_ID = "test";

    @Autowired
    public void create(WebApplicationContext context) {
        webTestClient = MockMvcWebTestClient.bindToApplicationContext(context)
                .apply(springSecurity())
                .defaultRequest(MockMvcRequestBuilders.get("/")
                        .with(SecurityMockMvcRequestPostProcessors
                                .jwt()
                                .jwt(builder -> builder.claim(PREFERRED_USERNAME, USER_ID))
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                        ))
                .configureClient()
                .build();
    }

    @BeforeEach
    void init() {
        mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes attrs = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(attrs);

        VehicularSpace entity = MockData.buildVehicularSpace();
        entity.setId(1);
        entity.setParkingSpace(MockData.buildParkingSpace());
        globalVS = vehicularSpaceRepository.save(entity);

        Order order = MockData.buildOrder();
        order.setItems(globalVS);
        order.setUserId(USER_ID);
        globalOrder = repository.save(order);
    }

    @AfterEach
    void afterEach() {
        vehicularSpaceRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    @DisplayName("Place order")
    void placeOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setVehicularSpace(globalVS);
        orderDTO.setStartTimeStamp(LocalDateTime.now().plusMinutes(5));
        orderDTO.setEndTimeStamp(LocalDateTime.now().plusHours(1));

        Order order = webTestClient
                .post().uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(order);
        assertEquals(Status.CONFIRMED, order.getStatus());
        assertEquals(USER_ID, order.getUserId());
    }

    @Test
    @DisplayName("Dont place order when space full")
    void dontPlaceOrder() {
        globalVS.setTotalSlots(0);
        vehicularSpaceRepository.save(globalVS);
        repository.deleteAll();
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setVehicularSpace(globalVS);
        orderDTO.setStartTimeStamp(LocalDateTime.now().plusMinutes(5));
        orderDTO.setEndTimeStamp(LocalDateTime.now().plusHours(1));

        ExceptionDetails details = webTestClient
                .post().uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(details);
        assertEquals(409, details.getStatus());
        assertEquals("Space Booked full", details.getDetail());
    }

    @Test
    @DisplayName("Dont place order when space not found")
    void dontPlaceOrderWhenSpaceNotFound() {
        VehicularSpace vehicularSpace = globalVS;
        vehicularSpace.setId(100);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setVehicularSpace(vehicularSpace);
        orderDTO.setStartTimeStamp(LocalDateTime.now().plusMinutes(5));
        orderDTO.setEndTimeStamp(LocalDateTime.now().plusHours(1));

        ExceptionDetails details = webTestClient
                .post().uri("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(orderDTO), OrderDTO.class)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(details);
        assertEquals(404, details.getStatus());
        assertEquals(VEHICLE_SPACE_NOT_FOUND, details.getDetail());
    }

    @Test
    @DisplayName("Get Order by Id")
    void getOrderById() {

        Order order = webTestClient
                .get().uri("/orders/" + globalOrder.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(order);
        assertEquals(globalOrder.getId(), order.getId());
        assertEquals(globalOrder.getStatus(), order.getStatus());
    }

    @Test
    @DisplayName("Dont Get Order by Id")
    void dontGetOrderById() {

        ExceptionDetails details = webTestClient
                .get().uri("/orders/100")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(details);
        assertEquals(ORDER_NOT_FOUND, details.getDetail());
    }

    @Test
    @DisplayName("Find all orders for user")
    void findAllOrders() {

        List<Order> orders = webTestClient
                .get().uri("/orders")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Order>>() {
                })
                .returnResult()
                .getResponseBody();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(USER_ID, orders.get(0).getUserId());
    }

    @Test
    @DisplayName("Checkout order")
    void checkoutOrder() {

        Order order = webTestClient
                .post().uri(uriBuilder -> uriBuilder
                        .path("/orders/{id}/finish")
                        .build(globalOrder.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Order.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(order);
        assertEquals(USER_ID, order.getUserId());
        assertEquals(Status.EXPIRED, order.getStatus());
    }

    @Test
    @DisplayName("Dont Checkout order when order not found")
    void dontCheckoutOrder() {

        ExceptionDetails exceptionDetails = webTestClient
                .post().uri(uriBuilder -> uriBuilder
                        .path("/orders/{id}/finish")
                        .build(100))
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ExceptionDetails.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(exceptionDetails);
        assertEquals(ORDER_NOT_FOUND, exceptionDetails.getDetail());
    }

}