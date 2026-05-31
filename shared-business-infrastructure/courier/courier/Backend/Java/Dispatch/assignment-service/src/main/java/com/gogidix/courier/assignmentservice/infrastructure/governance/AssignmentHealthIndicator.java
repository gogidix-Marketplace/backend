package com.gogidix.courier.assignmentservice.infrastructure.governance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AssignmentHealthIndicator {

    private static final Logger log = LoggerFactory.getLogger(AssignmentHealthIndicator.class);

    private final AtomicLong totalAssignmentsCreated = new AtomicLong(0);
    private final AtomicLong totalAssignmentsCompleted = new AtomicLong(0);
    private final AtomicLong totalAssignmentsCancelled = new AtomicLong(0);
    private final AtomicLong totalEventsPublished = new AtomicLong(0);
    private final Instant startTime = Instant.now();

    public void recordAssignmentCreated() {
        totalAssignmentsCreated.incrementAndGet();
    }

    public void recordAssignmentCompleted() {
        totalAssignmentsCompleted.incrementAndGet();
    }

    public void recordAssignmentCancelled() {
        totalAssignmentsCancelled.incrementAndGet();
    }

    public void recordEventPublished() {
        totalEventsPublished.incrementAndGet();
    }

    @Scheduled(fixedRate = 60000)
    public void logHealthStats() {
        log.info("Assignment Service Health - Uptime: {}, Created: {}, Completed: {}, Cancelled: {}, Events: {}",
                startTime, totalAssignmentsCreated.get(), totalAssignmentsCompleted.get(),
                totalAssignmentsCancelled.get(), totalEventsPublished.get());
    }

    public AssignmentHealthStatus getHealthStatus() {
        return new AssignmentHealthStatus(
                startTime,
                totalAssignmentsCreated.get(),
                totalAssignmentsCompleted.get(),
                totalAssignmentsCancelled.get(),
                totalEventsPublished.get()
        );
    }

    public record AssignmentHealthStatus(
            Instant startTime,
            long totalCreated,
            long totalCompleted,
            long totalCancelled,
            long totalEventsPublished
    ) {}
}
