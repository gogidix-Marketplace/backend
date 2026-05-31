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
 * MongoDB document for metric data points.
 * Optimized for time-series queries with appropriate indexes.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "metric_data_points")
@CompoundIndex(name = "tenant_service_metric_time_idx", def = "{'tenantId': 1, 'serviceName': 1, 'metricName': 1, 'timestamp': -1}")
@CompoundIndex(name = "tenant_service_time_idx", def = "{'tenantId': 1, 'serviceName': 1, 'timestamp': -1}")
@CompoundIndex(name = "tenant_time_idx", def = "{'tenantId': 1, 'timestamp': -1}")
public class MetricDataPointDocument {

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

    private double value;

    private String unit;

    @Indexed
    private String metricType;

    private Map<String, String> tags;

    @Indexed
    private Instant timestamp;

    private String host;

    @Indexed
    private String instanceId;

    private String correlationId;

    @Indexed
    private String category;

    @Indexed
    private Instant createdAt;
}
