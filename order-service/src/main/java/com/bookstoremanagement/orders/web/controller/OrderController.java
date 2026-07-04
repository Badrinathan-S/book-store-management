package com.bookstoremanagement.orders.web.controller;

import com.bookstoremanagement.orders.domain.OrderService;
import com.bookstoremanagement.orders.domain.SecurityService;
import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        String userName = securityService.getLoginUserName();
        log.info("Create order for user: {}", userName);
        return orderService.createOrder(userName, createOrderRequest);
    }
}
