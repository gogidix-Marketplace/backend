package com.gogidix.transaction.monitoring.dto;

import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionMetricsResponse {

    private UUID id;
    private UUID transactionId;
    private TransactionMetrics.MetricType metricType;
    private String metricName;
    private BigDecimal metricValue;
    private String metricUnit;
    private BigDecimal thresholdWarning;
    private BigDecimal thresholdCritical;
    private TransactionMetrics.MetricSeverity severity;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
    private Long version;
}
