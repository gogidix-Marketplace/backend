package com.gogidix.aiservices.aigatewayservice.domain.event;

/**
 * Event published when circuit breaker trips.
 */
public class CircuitBreakerTrippedEvent extends RouteEvent {

    private final Long failureCount;

    public CircuitBreakerTrippedEvent(String routeId, String tenantId, Long failureCount) {
        super(routeId, tenantId, "CIRCUIT_BREAKER_TRIPPED");
        this.failureCount = failureCount;
    }

    public Long getFailureCount() {
        return failureCount;
    }
}
