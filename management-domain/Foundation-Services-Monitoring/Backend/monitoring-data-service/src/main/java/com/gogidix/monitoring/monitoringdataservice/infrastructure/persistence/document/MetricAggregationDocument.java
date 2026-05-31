package com.gogidix.monitoring.monitoringdataservice.infrastructure.persistence.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

/**
 * MongoDB document for metric aggregations.
 * Stores pre-aggregated metrics for faster querying.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "metric_aggregations")
@CompoundIndex(name = "tenant_service_metric_window_time_idx",
        def = "{'tenantId': 1, 'serviceName': 1, 'metricName': 1, 'window': 1, 'windowStart': -1}")
@CompoundIndex(name = "tenant_window_time_idx",
        def = "{'tenantId': 1, 'window': 1, 'windowStart': -1}")
public class MetricAggregationDocument {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String serviceName;

    @Indexed
    private String serviceType;

    @Indexed
    private String metricName;

    @Indexed
    private String window;

    @Indexed
    private Instant windowStart;

    @Indexed
    private Instant windowEnd;

    private long count;

    private Double min;

    private Double max;

    private Double avg;

    private Double sum;

    private Double p50;

    private Double p95;

    private Double p99;

    private Double stdDev;

    private Map<String, String> tags;

    @Indexed
    private Instant computedAt;
}
