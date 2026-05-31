package com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngestionJobResponseDto {
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
}
