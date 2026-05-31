package com.gogidix.platform.platform.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Input port: Command to create feature flag.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFeatureFlagCommand {

    @NotBlank(message = "Feature key is required")
    private String featureKey;

    @NotBlank(message = "Feature name is required")
    private String featureName;

    private String description;

    @NotNull(message = "Feature type is required")
    private com.gogidix.platform.platform.domain.model.FeatureFlag.FeatureType featureType;

    @Builder.Default
    private boolean enabled = false;

    private String[] allowedTenants;

    private String[] deniedTenants;

    private List<String> userSegments;

    private java.util.Map<String, Object> rolloutRules;

    @Builder.Default
    private boolean requiresOptIn = false;
}
