package com.gogidix.globalbusinessmanagement.localization.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizationEntryResponseDto {
    private String id;
    private String tenantId;
    private String key;
    private String value;
    private String language;
    private String region;
    private String module;
    private String isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
