package com.bookstoremanagement.orders;

import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ConfigurationPropertiesScan
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
