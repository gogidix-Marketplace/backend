package com.gogidix.globalbusinessmanagement.localization.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizationEntryRequestDto {
    private String tenantId;
    private String key;
    private String value;
    private String language;
    private String region;
    private String module;
    private String isActive;
}
