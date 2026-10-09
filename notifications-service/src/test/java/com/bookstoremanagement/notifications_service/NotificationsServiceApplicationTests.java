package com.bookstoremanagement.notifications_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
class NotificationsServiceApplicationTests extends AbstractIT {

    @Test
    void contextLoads() {
    }

}
