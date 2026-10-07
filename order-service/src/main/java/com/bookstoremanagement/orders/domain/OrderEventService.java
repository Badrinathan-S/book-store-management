package com.bookstoremanagement.orders.domain;

import com.bookstoremanagement.orders.domain.models.*;
import com.bookstoremanagement.orders.domain.models.OrderEventType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderEventService {
    private static final Logger log = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final ObjectMapper objectMapper;
    private final OrderEventPublisher orderEventPublisher;

    OrderEventService(
            OrderEventRepository orderEventRepository,
            ObjectMapper objectMapper,
            OrderEventPublisher orderEventPublisher) {
        this.orderEventRepository = orderEventRepository;
        this.objectMapper = objectMapper;
        this.orderEventPublisher = orderEventPublisher;
    }

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        //        this.orderEventRepository.save(orderEvent);
        orderEvent.setPayload(toJsonPayload(event));

        OrderEventEntity savedEvent = orderEventRepository.save(orderEvent);

        log.info(
                "OrderEvent saved: id={}, eventId={}, orderNumber={}",
                savedEvent.getId(),
                savedEvent.getEventId(),
                savedEvent.getOrderNumber());
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(toJsonPayload(event));
        this.orderEventRepository.save(orderEvent);
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = this.orderEventRepository.findAll(sort);
        log.info("Found {} Order Events to be published", events.size());
        for (OrderEventEntity orderEvent : events) {
            this.publishEvent(orderEvent);
            orderEventRepository.delete(orderEvent);
        }
    }

    private void publishEvent(OrderEventEntity orderEventEntity) {
        OrderEventType eventType = orderEventEntity.getEventType();
        switch (eventType) {
            case ORDER_CREATED:
                OrderCreatedEvent orderCreatedEvent =
                        (OrderCreatedEvent) fromJsonPayload(orderEventEntity.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderCreatedEvent);
                break;
            case ORDER_DELIVERED:
                OrderDeliveredEvent orderDeliveredEvent =
                        (OrderDeliveredEvent) fromJsonPayload(orderEventEntity.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderDeliveredEvent);
                break;
            case ORDER_CANCELLED:
                OrderCancelledEvent orderCancelledEvent =
                        (OrderCancelledEvent) fromJsonPayload(orderEventEntity.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderCancelledEvent);
                break;
            case ORDER_PROCESSING_FAILED:
                OrderErrorEvent OrderErrorEvent =
                        (OrderErrorEvent) fromJsonPayload(orderEventEntity.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(OrderErrorEvent);
                break;
            default:
                log.warn("Unhandled Order Event Type: {}", eventType);
        }
    }

    private String toJsonPayload(Object object) {
        try {
            return objectMapper.writeValueAsString(object);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> T fromJsonPayload(String jsonPayload, Class<T> type) {
        try {
            return objectMapper.readValue(jsonPayload, type);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
