package com.gogidix.aiservices.anomalydetectionservice.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnomalySummary {
    private int totalAnomalies;
    private int highSeverity;
    private int mediumSeverity;
    private int lowSeverity;
    private double averageScore;
}
