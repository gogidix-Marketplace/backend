package com.gogidix.monitoring.performance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Domain entity representing a performance alert.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "performance_alerts")
public class PerformanceAlert {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String serviceId;

    private String alertName;

    private String condition;

    private Double threshold;

    private Double actualValue;

    private AlertStatus status;

    private String message;

    @Indexed
    private Instant triggeredAt;

    @Indexed
    private Instant resolvedAt;

    /**
     * Create a new alert.
     */
    public static PerformanceAlert create(String tenantId, String serviceId, String alertName,
                                       String condition, Double threshold, Double actualValue, String message) {
        return PerformanceAlert.builder()
                .tenantId(tenantId)
                .serviceId(serviceId)
                .alertName(alertName)
                .condition(condition)
                .threshold(threshold)
                .actualValue(actualValue)
                .status(AlertStatus.OPEN)
                .message(message)
                .triggeredAt(Instant.now())
                .build();
    }

    /**
     * Resolve the alert.
     */
    public void resolve() {
        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = Instant.now();
    }
}
