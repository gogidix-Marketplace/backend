package com.gogidix.centralconfiguration.configserver.domain.port.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateConfigCommand(
@NotBlank(message = "Tenant ID is required")
String tenantId,

@NotBlank(message = "Application name is required")
@Size(max = 255, message = "Application name must not exceed 255 characters")
String applicationName,

@NotBlank(message = "Profile is required")
@Size(max = 100, message = "Profile must not exceed 100 characters")
String profile,

@NotBlank(message = "Configuration key is required")
@Size(max = 500, message = "Configuration key must not exceed 500 characters")
String configKey,

String configValue,

Boolean isEncrypted,

String description,

String createdBy
) {
public CreateConfigCommand {
if (isEncrypted == null) {
isEncrypted = false;
}
}

public static CreateConfigCommandBuilder builder() {
return new CreateConfigCommandBuilder();
}

public static class CreateConfigCommandBuilder {
private String tenantId;
private String applicationName;
private String profile;
private String configKey;
private String configValue;
private Boolean isEncrypted = false;
private String description;
private String createdBy;

public CreateConfigCommandBuilder tenantId(String tenantId) {
this.tenantId = tenantId;
return this;
}

public CreateConfigCommandBuilder applicationName(String applicationName) {
this.applicationName = applicationName;
return this;
}

public CreateConfigCommandBuilder profile(String profile) {
this.profile = profile;
return this;
}

public CreateConfigCommandBuilder configKey(String configKey) {
this.configKey = configKey;
return this;
}

public CreateConfigCommandBuilder configValue(String configValue) {
this.configValue = configValue;
return this;
}

public CreateConfigCommandBuilder isEncrypted(Boolean isEncrypted) {
this.isEncrypted = isEncrypted;
return this;
}

public CreateConfigCommandBuilder description(String description) {
this.description = description;
return this;
}

public CreateConfigCommandBuilder createdBy(String createdBy) {
this.createdBy = createdBy;
return this;
}

public CreateConfigCommand build() {
return new CreateConfigCommand(tenantId, applicationName, profile, configKey, configValue, isEncrypted, description, createdBy);
}
}
}
