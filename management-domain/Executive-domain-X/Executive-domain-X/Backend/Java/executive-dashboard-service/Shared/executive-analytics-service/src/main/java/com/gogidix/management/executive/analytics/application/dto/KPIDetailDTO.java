package com.gogidix.management.executive.analytics.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
/**
 * Detailed KPI Data Transfer Object
 */
@Data
public class KPIDetailDTO {
    private String id;
    private String name;
    private String category;
    private String executiveLevel;
    private BigDecimal value;
    private String unit;
    private String period;
    private BigDecimal target;
    private BigDecimal previousValue;
    private BigDecimal percentChange;
    private String status;
    private String trend;
    private List<String> dataSources;
    private Map<String, Object> metadata;
    private Boolean visible;
    private Instant lastCalculatedAt;
    private Instant createdAt;
    private Instant updatedAt;
}
