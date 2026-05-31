package com.gogidix.digitalmarketing.globalmarketingdashboard.infrastructure.metrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class CustomMetrics {


    private final AtomicLong operationCount = new AtomicLong(0);

    public void incrementOperation() {
        operationCount.incrementAndGet();
    }

    public long getOperationCount() {
        return operationCount.get();
    }
}
