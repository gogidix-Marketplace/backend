package com.gogidix.sales.countrydashboard.application.dto.response;

import com.gogidix.sales.countrydashboard.domain.valueobject.MetricType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * KPI Response DTO
 * Used for API responses with KPI data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KpiResponseDto {

    private String kpiId;
    private String name;
    private MetricType type;
    private Object value;
    private BigDecimal monetaryValue;
    private String currency;
    private BigDecimal percentageValue;
    private String target;
    private BigDecimal achievement;
    private String trend;
    private BigDecimal trendValue;
    private Integer weight;
    private Boolean isCritical;
    private Instant lastUpdated;
}
