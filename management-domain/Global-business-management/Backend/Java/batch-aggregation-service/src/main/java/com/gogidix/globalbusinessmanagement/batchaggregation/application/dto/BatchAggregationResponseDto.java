package com.gogidix.globalbusinessmanagement.batchaggregation.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchAggregationResponseDto {
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
}
