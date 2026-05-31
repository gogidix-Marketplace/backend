package com.gogidix.sales.leadmanagement.infrastructure.messaging.kafka;

import com.gogidix.sales.leadmanagement.domain.port.out.LeadAssigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round Robin Lead Assigner Implementation
 */
@Component
@Slf4j
public class RoundRobinLeadAssigner implements LeadAssigner {

    private final Map<String, AtomicInteger> tenantCounters = new ConcurrentHashMap<>();

    @Value("${lead-management-service.assignment.default-strategy:ROUND_ROBIN}")
    private AssignmentStrategy defaultStrategy;

    // In a real implementation, this would fetch from a user/rep service
    // For now, we'll use a simple map that would be populated from configuration
    private final Map<String, List<String>> tenantSalesReps = new ConcurrentHashMap<>();

    @Override
    public Optional<String> assignLead(String tenantId, String territory, String segment) {
        return assignLeadWithStrategy(tenantId, defaultStrategy, territory, segment);
    }

    @Override
    public Optional<String> assignLeadWithStrategy(String tenantId, AssignmentStrategy strategy,
                                                   String territory, String segment) {
        List<String> reps = tenantSalesReps.get(tenantId);

        if (reps == null || reps.isEmpty()) {
            log.warn("No sales reps available for tenant: {}", tenantId);
            return Optional.empty();
        }

        switch (strategy) {
            case ROUND_ROBIN:
                return assignRoundRobin(tenantId, reps);
            case TERRITORY_BASED:
                return assignByTerritory(tenantId, territory, reps);
            case WORKLOAD_BASED:
                return assignByWorkload(tenantId, reps);
            case MANUAL:
            default:
                return Optional.empty();
        }
    }

    @Override
    public AssignmentStrategy getCurrentStrategy(String tenantId) {
        return defaultStrategy;
    }

    private Optional<String> assignRoundRobin(String tenantId, List<String> reps) {
        AtomicInteger counter = tenantCounters.computeIfAbsent(tenantId, k -> new AtomicInteger(0));
        int index = counter.getAndIncrement() % reps.size();
        return Optional.of(reps.get(index));
    }

    private Optional<String> assignByTerritory(String tenantId, String territory, List<String> reps) {
        // In a real implementation, this would map territories to reps
        // For now, fall back to round robin
        log.debug("Territory-based assignment not fully implemented, falling back to round-robin");
        return assignRoundRobin(tenantId, reps);
    }

    private Optional<String> assignByWorkload(String tenantId, List<String> reps) {
        // In a real implementation, this would query for rep with least active leads
        // For now, fall back to round robin
        log.debug("Workload-based assignment not fully implemented, falling back to round-robin");
        return assignRoundRobin(tenantId, reps);
    }

    /**
     * Initialize sales reps for a tenant (would be called from configuration or admin)
     */
    public void initializeTenantReps(String tenantId, List<String> repIds) {
        tenantSalesReps.put(tenantId, repIds);
        log.info("Initialized {} sales reps for tenant: {}", repIds.size(), tenantId);
    }
}
