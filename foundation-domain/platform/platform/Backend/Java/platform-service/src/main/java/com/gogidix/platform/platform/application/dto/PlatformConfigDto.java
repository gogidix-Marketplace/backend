package com.gogidix.platform.platform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for platform configuration responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformConfigDto {

    private String id;
    private String tenantId;
    private String configKey;
    private String configValue;
    private String configType;
    private String description;
    private boolean isSensitive;
    private boolean isEncrypted;
    private String environment;
    private Integer version;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveUntil;
    private String[] tags;
    private Map<String, Object> metadata;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
