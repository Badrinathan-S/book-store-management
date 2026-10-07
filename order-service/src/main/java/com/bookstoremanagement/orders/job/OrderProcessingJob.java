package com.bookstoremanagement.orders.job;

import com.bookstoremanagement.orders.domain.OrderService;
import java.time.Instant;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingJob {
    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingJob.class.getName());

    private final OrderService orderService;

    public OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${order.new-orders-job-cron}")
    @SchedulerLock(name = "processNewOrders")
    public void processNewOrder() {
        LockAssert.assertLocked();
        logger.info("Processing New Order at {}", Instant.now());
        orderService.processNewOrder();
    }
}
