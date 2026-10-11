package com.bookstoremanagement.orders.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderDeliveredEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> orderItems,
        Customer customer,
        Address deliveryAddress,
        LocalDateTime createdAt) {}
