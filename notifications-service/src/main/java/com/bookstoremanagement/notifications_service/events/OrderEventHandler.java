package com.bookstoremanagement.notifications_service.events;

import com.bookstoremanagement.notifications_service.domain.NotificationService;
import com.bookstoremanagement.notifications_service.domain.OrderEventEntity;
import com.bookstoremanagement.notifications_service.domain.OrderEventRepository;
import com.bookstoremanagement.notifications_service.domain.models.OrderCancelledEvent;
import com.bookstoremanagement.notifications_service.domain.models.OrderCreatedEvent;
import com.bookstoremanagement.notifications_service.domain.models.OrderDeliveredEvent;
import com.bookstoremanagement.notifications_service.domain.models.OrderErrorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    public OrderEventHandler(NotificationService notificationService, OrderEventRepository orderEventRepository) {
        this.notificationService = notificationService;
        this.orderEventRepository = orderEventRepository;
    }

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Order Created Event{}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order Created Event already exists with eventId {}", event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event) {
        log.info("Order Delivered Event{}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order delivered Event already exists with eventId {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event) {
        log.info("Order Cancelled Event{}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order cancelled Event already exists with eventId {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event) {
        log.info("Order Error Event{}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Order error Event already exists with eventId {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity orderEventEntity = new OrderEventEntity(event.eventId());
        orderEventRepository.save(orderEventEntity);
    }
}
