package com.bookstoremanagement.orders.domain;

import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.OrderItem;
import com.bookstoremanagement.orders.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class OrderMapper {

    static OrderEntity convertToOrderEntity(CreateOrderRequest createOrderRequest) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(createOrderRequest.customer());
        newOrder.setDeliveryAddress(createOrderRequest.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : createOrderRequest.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }

}
