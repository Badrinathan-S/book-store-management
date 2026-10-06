package com.bookstoremanagement.orders.job;

import com.bookstoremanagement.orders.domain.OrderEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OrderEventPublishingJob {
    private static final Logger log = LoggerFactory.getLogger(OrderEventPublishingJob.class);

    private OrderEventService orderEventService;

    OrderEventPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "${orders-publish-order-events-job-cron}")
    public void publishOrderEvent() {
        log.info("Publishing Order Event at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
