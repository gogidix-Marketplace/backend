package com.gogidix.globalbusinessmanagement.regionalanalytics.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "regional_analytics")
public class RegionalAnalytics {

    @Id
    private String id;
    private String tenantId;
    private String metricName;
    private String metricValue;
    private String region;
    private String country;
    private String period;
    private String category;
    private Instant createdAt;
    private Instant updatedAt;

    public RegionalAnalytics(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
