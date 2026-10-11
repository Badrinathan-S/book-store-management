package com.bookstoremanagement.orders.domain.models;

public record OrderErrorEvent(
        String eventId,
        String orderNumber,
        java.util.Set<OrderItem> orderItems,
        Customer customer,
        Address deliveryAddress,
        String reason,
        java.time.LocalDateTime createdAt) {}
