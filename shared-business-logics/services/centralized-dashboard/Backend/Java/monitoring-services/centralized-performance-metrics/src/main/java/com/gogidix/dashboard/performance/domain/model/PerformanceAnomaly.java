package com.gogidix.dashboard.performance.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Performance Anomaly Value Object
 */
public class PerformanceAnomaly {
    private final String description;
    private final AnomalySeverity severity;
    private final LocalDateTime detectedAt;
    
    public PerformanceAnomaly(String description, AnomalySeverity severity, LocalDateTime detectedAt) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
        this.severity = Objects.requireNonNull(severity, "Severity cannot be null");
        this.detectedAt = Objects.requireNonNull(detectedAt, "Detection time cannot be null");
    }
    
    public String getDescription() { return description; }
    public AnomalySeverity getSeverity() { return severity; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceAnomaly that = (PerformanceAnomaly) o;
        return Objects.equals(description, that.description) &&
               severity == that.severity &&
               Objects.equals(detectedAt, that.detectedAt);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(description, severity, detectedAt);
    }
}

// Enums are defined in separate files: AnomalySeverity.java, PerformanceTrend.java, AlertPriority.java