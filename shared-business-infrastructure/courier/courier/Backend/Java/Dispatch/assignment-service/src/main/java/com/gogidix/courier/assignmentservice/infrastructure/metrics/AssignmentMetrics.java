package com.gogidix.courier.assignmentservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class AssignmentMetrics {

    private final Counter assignmentsCreatedCounter;
    private final Counter assignmentsCompletedCounter;
    private final Counter assignmentsCancelledCounter;
    private final Counter assignmentsFailedCounter;
    private final Timer assignmentCreationTimer;
    private final Timer assignmentCompletionTimer;

    public AssignmentMetrics(MeterRegistry registry) {
        this.assignmentsCreatedCounter = Counter.builder("assignment.created.total")
                .description("Total number of assignments created")
                .register(registry);

        this.assignmentsCompletedCounter = Counter.builder("assignment.completed.total")
                .description("Total number of assignments completed")
                .register(registry);

        this.assignmentsCancelledCounter = Counter.builder("assignment.cancelled.total")
                .description("Total number of assignments cancelled")
                .register(registry);

        this.assignmentsFailedCounter = Counter.builder("assignment.failed.total")
                .description("Total number of assignments failed")
                .register(registry);

        this.assignmentCreationTimer = Timer.builder("assignment.creation.duration")
                .description("Time taken to create an assignment")
                .register(registry);

        this.assignmentCompletionTimer = Timer.builder("assignment.completion.duration")
                .description("Time taken to complete an assignment")
                .register(registry);
    }

    public void recordAssignmentCreated() {
        assignmentsCreatedCounter.increment();
    }

    public void recordAssignmentCompleted() {
        assignmentsCompletedCounter.increment();
    }

    public void recordAssignmentCancelled() {
        assignmentsCancelledCounter.increment();
    }

    public void recordAssignmentFailed() {
        assignmentsFailedCounter.increment();
    }

    public Timer getAssignmentCreationTimer() {
        return assignmentCreationTimer;
    }

    public Timer getAssignmentCompletionTimer() {
        return assignmentCompletionTimer;
    }
}
