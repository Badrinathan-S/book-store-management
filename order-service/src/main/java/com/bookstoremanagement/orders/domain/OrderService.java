package com.bookstoremanagement.orders.domain;

import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest createOrderRequest) {
        OrderEntity newOrder = OrderMapper.convertToOrderEntity(createOrderRequest);
        newOrder.setUserName(userName);
        OrderEntity saveOrder = this.orderRepository.save(newOrder);
        log.info("Create Order with orderNumber={}");
        return new CreateOrderResponse(saveOrder.getOrderNumber());
    }

}
