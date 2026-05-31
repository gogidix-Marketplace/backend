package com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngestionJobRequestDto {
    private String tenantId;
    private String topic;
    private String source;
    private String status;
    private String recordCount;
    private String errorCount;
    private String lastProcessedOffset;
}
