package com.gogidix.universal.tracking.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Query to retrieve tracking metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMetricsQuery {

    private String tenantId;

    private String metricName;

    private String metricType;

    private String eventType;

    private String source;

    private LocalDate metricDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<String> metricNames;

    private Boolean includeHourly;

    private Integer page;

    private Integer size;

    private String sortBy;

    private String sortDirection;
}
