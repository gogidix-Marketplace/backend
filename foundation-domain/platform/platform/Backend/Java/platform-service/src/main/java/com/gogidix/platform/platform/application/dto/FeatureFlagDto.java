package com.gogidix.platform.platform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for feature flag responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureFlagDto {

    private String id;
    private String tenantId;
    private String featureKey;
    private String featureName;
    private String description;
    private String featureType;
    private boolean enabled;
    private String[] allowedTenants;
    private String[] deniedTenants;
    private List<String> userSegments;
    private Map<String, Object> rolloutRules;
    private boolean requiresOptIn;
    private Integer rolloutPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
