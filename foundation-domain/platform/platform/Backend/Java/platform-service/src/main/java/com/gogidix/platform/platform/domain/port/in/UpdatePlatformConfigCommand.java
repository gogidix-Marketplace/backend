package com.gogidix.platform.platform.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * Input port: Command to update platform configuration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePlatformConfigCommand {

    @NotBlank(message = "Configuration value is required")
    private String configValue;

    private String description;

    private String[] tags;

    private java.util.Map<String, Object> metadata;
}
