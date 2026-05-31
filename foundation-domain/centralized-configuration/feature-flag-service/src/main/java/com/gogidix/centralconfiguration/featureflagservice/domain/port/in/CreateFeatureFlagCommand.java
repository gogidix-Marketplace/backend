package com.gogidix.centralconfiguration.featureflagservice.domain.port.in;

import com.gogidix.centralconfiguration.featureflagservice.domain.model.RolloutStrategy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFeatureFlagCommand(
@NotBlank(message = "Tenant ID is required")
String tenantId,

@NotBlank(message = "Flag key is required")
@Size(max = 255, message = "Flag key must not exceed 255 characters")
String flagKey,

@NotBlank(message = "Flag name is required")
@Size(max = 255, message = "Flag name must not exceed 255 characters")
String name,

String description,

@NotNull(message = "Enabled status is required")
Boolean isEnabled,

@NotNull(message = "Rollout percentage is required")
Integer rolloutPercentage,

@NotNull(message = "Rollout strategy is required")
RolloutStrategy rolloutStrategy,

String whitelistedUsers,

Boolean isSticky,

String tags,

String owner,

String createdBy
) {
public CreateFeatureFlagCommand {
if (isEnabled == null) isEnabled = false;
if (rolloutPercentage == null) rolloutPercentage = 100;
if (rolloutStrategy == null) rolloutStrategy = RolloutStrategy.ALL_USERS;
if (isSticky == null) isSticky = false;
}

public static CreateFeatureFlagCommandBuilder builder() {
return new CreateFeatureFlagCommandBuilder();
}

public static class CreateFeatureFlagCommandBuilder {
private String tenantId;
private String flagKey;
private String name;
private String description;
private Boolean isEnabled = false;
private Integer rolloutPercentage = 100;
private RolloutStrategy rolloutStrategy = RolloutStrategy.ALL_USERS;
private String whitelistedUsers;
private Boolean isSticky = false;
private String tags;
private String owner;
private String createdBy;

public CreateFeatureFlagCommandBuilder tenantId(String tenantId) { this.tenantId = tenantId; return this; }
public CreateFeatureFlagCommandBuilder flagKey(String flagKey) { this.flagKey = flagKey; return this; }
public CreateFeatureFlagCommandBuilder name(String name) { this.name = name; return this; }
public CreateFeatureFlagCommandBuilder description(String description) { this.description = description; return this; }
public CreateFeatureFlagCommandBuilder isEnabled(Boolean isEnabled) { this.isEnabled = isEnabled; return this; }
public CreateFeatureFlagCommandBuilder rolloutPercentage(Integer rolloutPercentage) { this.rolloutPercentage = rolloutPercentage; return this; }
public CreateFeatureFlagCommandBuilder rolloutStrategy(RolloutStrategy rolloutStrategy) { this.rolloutStrategy = rolloutStrategy; return this; }
public CreateFeatureFlagCommandBuilder whitelistedUsers(String whitelistedUsers) { this.whitelistedUsers = whitelistedUsers; return this; }
public CreateFeatureFlagCommandBuilder isSticky(Boolean isSticky) { this.isSticky = isSticky; return this; }
public CreateFeatureFlagCommandBuilder tags(String tags) { this.tags = tags; return this; }
public CreateFeatureFlagCommandBuilder owner(String owner) { this.owner = owner; return this; }
public CreateFeatureFlagCommandBuilder createdBy(String createdBy) { this.createdBy = createdBy; return this; }

public CreateFeatureFlagCommand build() {
return new CreateFeatureFlagCommand(tenantId, flagKey, name, description, isEnabled, rolloutPercentage, rolloutStrategy, whitelistedUsers, isSticky, tags, owner, createdBy);
}
}
}
