package com.gogidix.globalbusinessmanagement.kafkaingestion.domain.model;

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
@Document(collection = "ingestion_jobs")
public class IngestionJob {

    @Id
    private String id;
    private String tenantId;
    private String topic;
    private String source;
    private String status;
    private String recordCount;
    private String errorCount;
    private String lastProcessedOffset;
    private Instant createdAt;
    private Instant updatedAt;

    public IngestionJob(String tenantId) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }
}
