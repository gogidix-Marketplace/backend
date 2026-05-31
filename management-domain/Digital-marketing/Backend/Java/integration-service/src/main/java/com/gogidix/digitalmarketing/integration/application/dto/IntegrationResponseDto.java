package com.gogidix.digitalmarketing.integration.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationResponseDto {
    private String id;
    private String tenantId;
    private String name;
     private String type;
     private String provider;
     private String status;
     private String webhookUrl;
     private String apiKey;
     private String configuration;

    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
}