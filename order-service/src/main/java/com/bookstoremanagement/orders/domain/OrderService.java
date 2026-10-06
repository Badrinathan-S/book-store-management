package com.bookstoremanagement.orders.domain;

import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.CreateOrderResponse;
import com.bookstoremanagement.orders.domain.models.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest createOrderRequest) {
        orderValidator.validate(createOrderRequest);
        OrderEntity newOrder = OrderMapper.convertToOrderEntity(createOrderRequest);
        newOrder.setUserName(userName);
        OrderEntity saveOrder = this.orderRepository.save(newOrder);
        log.info("Create Order with orderNumber={}", saveOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(saveOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(saveOrder.getOrderNumber());
    }

}
