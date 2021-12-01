package com.mini.ecommerceapp.scheduler;

import com.mini.ecommerceapp.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private final OrderService orderService;

    @Autowired
    public ScheduledTasks(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    @Async
    public void releaseResource() {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        log.info("Release Resources started at {}", dateTime);
        orderService.releaseResources(dateTime);
        log.info("Finished releasing resources at {}", LocalDateTime.now());
    }
}
