package com.gogidix.aiservices.anomalydetectionservice.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Anomaly {
    private String timestamp;
    private double score;
    private String description;
    private String affectedMetric;
    private Map<String, Object> metadata;
}
