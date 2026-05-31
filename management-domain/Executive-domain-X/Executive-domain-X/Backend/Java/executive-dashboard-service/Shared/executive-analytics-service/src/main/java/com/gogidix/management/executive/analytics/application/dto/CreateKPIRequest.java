package com.gogidix.management.executive.analytics.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateKPIRequest {
    @NotNull(message = "Name is required")
    private String name;
    @NotNull(message = "Category is required")
    private String category;
    private String executiveLevel;
    @NotNull(message = "Value is required")
    private BigDecimal value;
    private String unit;
    private String period;
    private BigDecimal target;
    private BigDecimal previousValue;
    private List<String> dataSources;
    private Map<String, Object> metadata;
    private Boolean visible;
    private Boolean isCalculated;
}
