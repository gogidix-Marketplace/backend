package com.gogidix.digitalmarketing.integration.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntegrationRequestDto {
    private String tenantId;
    private String name;
     private String type;
     private String provider;
     private String status;
     private String webhookUrl;
     private String apiKey;
     private String configuration;

}