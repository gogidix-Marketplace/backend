package com.gogidix.dashboard.performance.adapter.in.web.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Performance Metric
 */
public class PerformanceMetricDTO {
    private String metricId;
    private String metricName;
    private String domain;
    private String service;
    private double value;
    private String unit;
    private LocalDateTime timestamp;
    private String status;

    public PerformanceMetricDTO() {
    }

    public PerformanceMetricDTO(String metricId, String metricName, String domain, 
                                  String service, double value, String unit, 
                                  LocalDateTime timestamp, String status) {
        this.metricId = metricId;
        this.metricName = metricName;
        this.domain = domain;
        this.service = service;
        this.value = value;
        this.unit = unit;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getMetricId() { return metricId; }
    public void setMetricId(String metricId) { this.metricId = metricId; }
    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
