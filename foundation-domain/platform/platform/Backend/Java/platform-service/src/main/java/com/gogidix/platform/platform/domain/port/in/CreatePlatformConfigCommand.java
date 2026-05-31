package com.gogidix.platform.platform.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Input port: Command to create platform configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlatformConfigCommand {

    @NotBlank(message = "Configuration key is required")
    private String configKey;

    @NotBlank(message = "Configuration value is required")
    private String configValue;

    @NotNull(message = "Configuration type is required")
    private com.gogidix.platform.platform.domain.model.PlatformConfiguration.ConfigType configType;

    private String description;

    @Builder.Default
    private boolean isSensitive = false;

    @Builder.Default
    private boolean isEncrypted = false;

    private com.gogidix.platform.platform.domain.model.PlatformConfiguration.Environment environment;

    private String[] tags;

    private java.util.Map<String, Object> metadata;
}
