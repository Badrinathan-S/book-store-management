package com.bookstoremanagement.orders.domain;


import com.bookstoremanagement.orders.clients.catalog.Product;
import com.bookstoremanagement.orders.clients.catalog.ProductServiceClient;
import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.InvalidOrderException;
import com.bookstoremanagement.orders.domain.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class OrderValidator {
    private static final Logger log = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient client;

    OrderValidator(ProductServiceClient client) {
        this.client = client;
    }

    void validate(CreateOrderRequest request) {
        Set<OrderItem> item = request.items();
        for (OrderItem orderItem : item) {
            Product product = client.getProductByCode(orderItem.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product code:" + orderItem.code()));
            if (orderItem.price().compareTo(product.price()) != 0) {
                log.error(
                        "Product price not matching. Actual price:{}, received price:{}",
                        product.price(),
                        orderItem.price());
                throw new InvalidOrderException("Product price not matching");
            }
        }
    }
}
