package com.gogidix.platform.platform.interfaces.rest;

import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import com.gogidix.platform.platform.domain.repository.PlatformConfigurationRepository;
import com.gogidix.shared.exceptions.domain.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for platform configuration management.
 */
@RestController
@RequestMapping("/api/v1/platform/configurations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Platform Configuration", description = "APIs for managing platform configurations")
public class PlatformConfigurationController {

    private final PlatformConfigurationRepository repository;

    @PostMapping
    @Operation(summary = "Create configuration", description = "Create a new platform configuration")
    public ResponseEntity<ConfigurationResponse> createConfiguration(
            @Valid @RequestBody CreateConfigurationRequest request) {

        log.info("Creating configuration: {}", request.getConfigKey());

        if (repository.existsByConfigKey(request.getConfigKey())) {
            throw new BusinessException("Configuration key already exists");
        }

        PlatformConfiguration config = PlatformConfiguration.builder()
                .tenantId(request.getTenantId())
                .configKey(request.getConfigKey())
                .configValue(request.getConfigValue())
                .configType(PlatformConfiguration.ConfigType.valueOf(request.getConfigType()))
                .description(request.getDescription())
                .isSensitive(request.isSensitive())
                .environment(PlatformConfiguration.Environment.valueOf(request.getEnvironment()))
                .tags(request.getTags())
                .build();

        config = repository.save(config);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConfigurationResponse.fromEntity(config));
    }

    @GetMapping
    @Operation(summary = "List configurations", description = "Get all configurations for tenant")
    public ResponseEntity<List<ConfigurationResponse>> getConfigurations(
            @RequestParam String tenantId) {

        List<PlatformConfiguration> configs = repository.findByTenantId(tenantId);
        List<ConfigurationResponse> response = configs.stream()
                .map(ConfigurationResponse::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    @Operation(summary = "Get configuration by key", description = "Get configuration by key")
    public ResponseEntity<ConfigurationResponse> getConfiguration(
            @Parameter(description = "Configuration key", required = true)
            @PathVariable String key) {

        PlatformConfiguration config = repository.findByConfigKey(key)
                .orElseThrow(() -> new BusinessException("Configuration not found"));

        return ResponseEntity.ok(ConfigurationResponse.fromEntity(config));
    }

    @Data
    public static class CreateConfigurationRequest {
        @NotBlank
        private String tenantId;

        @NotBlank
        @Size(min = 3, max = 100)
        private String configKey;

        @NotBlank
        private String configValue;

        @NotBlank
        private String configType;

        private String description;

        private boolean isSensitive;

        @NotBlank
        private String environment;

        private String[] tags;
    }

    @Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ConfigurationResponse {
        private String id;
        private String tenantId;
        private String configKey;
        private String configValue;
        private String configType;
        private String description;
        private boolean isSensitive;
        private String environment;
        private String[] tags;
        private Integer version;
        private String status;

        public static ConfigurationResponse fromEntity(PlatformConfiguration config) {
            return ConfigurationResponse.builder()
                    .id(config.getId())
                    .tenantId(config.getTenantId())
                    .configKey(config.getConfigKey())
                    .configValue(config.getConfigValue())
                    .configType(config.getConfigType().name())
                    .description(config.getDescription())
                    .isSensitive(config.isSensitive())
                    .environment(config.getEnvironment() != null ? config.getEnvironment().name() : null)
                    .tags(config.getTags())
                    .version(config.getVersion())
                    .status(config.getStatus().name())
                    .build();
        }
    }
}
