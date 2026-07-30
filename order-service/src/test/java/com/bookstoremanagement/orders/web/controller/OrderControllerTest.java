package com.bookstoremanagement.orders.web.controller;

import com.bookstoremanagement.orders.AbstractIT;
import com.bookstoremanagement.orders.testdata.TestDataFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class OrderControllerTest extends AbstractIT {

    @Nested
    class CreateOrderTests {

        @Test
        void shouldCreateOrderSuccessfully() throws Exception {
            mockGetProductByCode("P100", "Product 1", new BigDecimal("25.50"));
            var payload = """
                    {
                        "customer": {
                            "name": "Badrinathan",
                            "email": "badrinathan@google.com",
                            "phone": "9876543210"
                        },
                        "deliveryAddress": {
                            "addressLine1": "Koomapatti",
                            "addressLine2": "London",
                            "city": "Tirupur",
                            "state": "Tamil Nadu",
                            "zipCode": "636002",
                            "country": "India"
                        },
                        "items": [
                            {
                                "code": "P100",
                                "name": "Product 1",
                                "price": 25.50,
                                "quantity": 1
                            }
                        ]
                    }
                    """;
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("orderNumber", notNullValue());
        }

        @Test
        void shouldReturnBadRequestWhenDataIsMissing() {
            var payload = TestDataFactory.createOrderRequestWithInvalidCustomer();
            given().contentType(ContentType.JSON)
                    .body(payload)
                    .when()
                    .post("/api/orders")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}