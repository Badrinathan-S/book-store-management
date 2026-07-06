package com.bookstoremanagement.orders.testdata;

import com.bookstoremanagement.orders.domain.models.Address;
import com.bookstoremanagement.orders.domain.models.CreateOrderRequest;
import com.bookstoremanagement.orders.domain.models.Customer;
import com.bookstoremanagement.orders.domain.models.OrderItem;
import org.instancio.Instancio;

import static org.instancio.Select.field;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class TestDataFactory {
    static final List<String> VALID_COUNTIES = List.of("India", "Germany");
    static final Set<OrderItem> VALID_ORDER_ITEMS = Set.of(new OrderItem("P100", "Product 1", new BigDecimal("25.50"), 1));
    static final Set<OrderItem> INVALID_ORDER_ITEMS = Set.of(new OrderItem("ABCD", "Product 1", new BigDecimal("25.50"), 1));

    public static CreateOrderRequest createValidOrderRequest() {
        return Instancio.of(CreateOrderRequest.class).generate(field(Customer::email), gen -> gen.text().pattern("#a#a#a#a#a#a@gmail.com")).set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS).generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES)).create();

    }

    public static CreateOrderRequest createOrderRequestWithInvalidCustomer() {
        return Instancio.of(CreateOrderRequest.class).generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d@gmail.com")).set(field(Customer::phone), "").generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES)).set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS).create();
    }

    public static CreateOrderRequest createOrderRequestWithInvalidDeliveryAddress() {
        return Instancio.of(CreateOrderRequest.class).generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d@gmail.com")).set(field(Address::country), "").set(field(CreateOrderRequest::items), VALID_ORDER_ITEMS).create();
    }

    public static CreateOrderRequest createOrderRequestWithNoItems() {
        return Instancio.of(CreateOrderRequest.class).generate(field(Customer::email), gen -> gen.text().pattern("#c#c#c#c#d@gmail.com")).set(field(Customer::phone), "").generate(field(Address::country), gen -> gen.oneOf(VALID_COUNTIES)).set(field(CreateOrderRequest::items), Set.of()).create();
    }

}
