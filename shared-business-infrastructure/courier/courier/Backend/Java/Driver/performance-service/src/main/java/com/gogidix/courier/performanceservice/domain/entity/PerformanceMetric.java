package com.gogidix.courier.performanceservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing a specific Performance Metric.
 * Tracks individual metric values over time.
 */
@Document(collection = "performance_metrics")
@CompoundIndex(name = "idx_metric_driver_time", def = "{'tenantId': 1, 'driverId': 1, 'metricDate': -1}")
public class PerformanceMetric {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Indexed
    @Field("metric_date")
    private Instant metricDate;

    @Field("metric_type")
    private MetricType metricType;

    @Field("metric_value")
    private Double metricValue;

    @Field("delivery_id")
    private String deliveryId;

    @Field("context")
    private MetricContext context;

    @Field("created_at")
    private Instant createdAt;

    /**
     * Default constructor for persistence.
     */
    protected PerformanceMetric() {
    }

    /**
     * Create a new PerformanceMetric.
     *
     * @param tenantId   the tenant identifier
     * @param driverId   the driver identifier
     * @param metricType the metric type
     * @param metricValue the metric value
     */
    public PerformanceMetric(String tenantId, String driverId, MetricType metricType, Double metricValue) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.metricType = Objects.requireNonNull(metricType, "metricType is required");
        this.metricValue = Objects.requireNonNull(metricValue, "metricValue is required");
        this.metricDate = Instant.now();
        this.createdAt = Instant.now();
    }

    /**
     * Validate the metric value.
     */
    public void validate() {
        if (metricValue == null) {
            throw new IllegalArgumentException("metricValue is required");
        }
        if (metricType == MetricType.RATING && (metricValue < 1.0 || metricValue > 5.0)) {
            throw new IllegalArgumentException("Rating must be between 1.0 and 5.0");
        }
        if (metricType == MetricType.COMPLETION_RATE && (metricValue < 0.0 || metricValue > 1.0)) {
            throw new IllegalArgumentException("Completion rate must be between 0.0 and 1.0");
        }
        if (metricType == MetricType.ON_TIME_RATE && (metricValue < 0.0 || metricValue > 1.0)) {
            throw new IllegalArgumentException("On-time rate must be between 0.0 and 1.0");
        }
    }

    /**
     * Set delivery context.
     *
     * @param deliveryId the delivery ID
     * @param context    the metric context
     */
    public void setDeliveryContext(String deliveryId, MetricContext context) {
        this.deliveryId = deliveryId;
        this.context = context;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public Instant getMetricDate() {
        return metricDate;
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public MetricContext getContext() {
        return context;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setMetricDate(Instant metricDate) {
        this.metricDate = metricDate;
    }

    protected void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    protected void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    protected void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    protected void setContext(MetricContext context) {
        this.context = context;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Metric type enum.
     */
    public enum MetricType {
        ON_TIME_RATE,
        COMPLETION_RATE,
        RATING,
        ROUTE_EFFICIENCY,
        DELIVERY_TIME,
        CANCELLATION_RATE,
        DISTANCE_TRAVELED
    }

    /**
     * Metric context value object.
     */
    public static class MetricContext {
        @Field("zone_id")
        private String zoneId;

        @Field("vehicle_type")
        private String vehicleType;

        @Field("time_of_day")
        private String timeOfDay;

        @Field("weather_condition")
        private String weatherCondition;

        public MetricContext() {
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getTimeOfDay() {
            return timeOfDay;
        }

        public void setTimeOfDay(String timeOfDay) {
            this.timeOfDay = timeOfDay;
        }

        public String getWeatherCondition() {
            return weatherCondition;
        }

        public void setWeatherCondition(String weatherCondition) {
            this.weatherCondition = weatherCondition;
        }
    }
}
