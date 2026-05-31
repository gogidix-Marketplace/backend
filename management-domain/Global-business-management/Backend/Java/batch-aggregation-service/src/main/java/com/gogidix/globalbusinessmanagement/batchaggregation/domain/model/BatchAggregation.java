package com.gogidix.globalbusinessmanagement.batchaggregation.domain.model;

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
@Document(collection = "batch_aggregations")
public class BatchAggregation {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String aggregationType;
    private String dataSource;
    private String status;
    private String schedule;
    private String region;
    private String country;
    private Instant createdAt;
    private Instant updatedAt;

    public BatchAggregation(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
