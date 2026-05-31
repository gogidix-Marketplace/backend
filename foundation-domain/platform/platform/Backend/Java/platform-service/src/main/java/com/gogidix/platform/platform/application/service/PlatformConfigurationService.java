package com.gogidix.platform.platform.application.service;

import com.gogidix.platform.platform.application.dto.PlatformConfigDto;
import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import com.gogidix.platform.platform.domain.port.in.CreatePlatformConfigCommand;
import com.gogidix.platform.platform.domain.port.in.UpdatePlatformConfigCommand;
import com.gogidix.platform.platform.domain.repository.PlatformConfigurationRepository;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for platform configuration management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlatformConfigurationService {

    private final PlatformConfigurationRepository configurationRepository;
    private final AuditService auditService;

    /**
     * Create a new platform configuration
     */
    @Transactional
    @CacheEvict(value = "platformConfigs", allEntries = true)
    public PlatformConfigDto createConfiguration(CreatePlatformConfigCommand command) {
        log.info("Creating platform configuration: key={}", command.getConfigKey());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        if (configurationRepository.findByConfigKey(command.getConfigKey()).isPresent()) {
            throw new ConflictException("Configuration key already exists: " + command.getConfigKey());
        }

        PlatformConfiguration config = PlatformConfiguration.builder()
            .tenantId(tenantId)
            .configKey(command.getConfigKey())
            .configValue(command.getConfigValue())
            .configType(command.getConfigType())
            .description(command.getDescription())
            .isSensitive(command.isSensitive())
            .isEncrypted(command.isEncrypted())
            .environment(command.getEnvironment())
            .tags(command.getTags())
            .metadata(command.getMetadata() != null ? command.getMetadata() : new java.util.HashMap<>())
            .status(com.gogidix.platform.platform.domain.model.PlatformConfiguration.ConfigStatus.ACTIVE)
            .build();

        if (command.getEnvironment() == null) {
            config.setEnvironment(com.gogidix.platform.platform.domain.model.PlatformConfiguration.Environment.ALL);
        }

        PlatformConfiguration saved = configurationRepository.save(config);

        auditService.logEvent("system", "CONFIG_CREATED", "PlatformConfiguration", saved.getId());

        return toDto(saved);
    }

    /**
     * Update platform configuration
     */
    @Transactional
    @CacheEvict(value = "platformConfigs", allEntries = true)
    public PlatformConfigDto updateConfiguration(String configId, UpdatePlatformConfigCommand command) {
        log.info("Updating platform configuration: id={}", configId);

        PlatformConfiguration config = configurationRepository.findById(configId)
            .orElseThrow(() -> new NotFoundException("Configuration not found: " + configId));

        config.setConfigValue(command.getConfigValue());
        config.incrementVersion();

        if (command.getDescription() != null) {
            config.setDescription(command.getDescription());
        }
        if (command.getTags() != null) {
            config.setTags(command.getTags());
        }
        if (command.getMetadata() != null) {
            config.setMetadata(command.getMetadata());
        }

        PlatformConfiguration saved = configurationRepository.save(config);

        auditService.logEvent("system", "CONFIG_UPDATED", "PlatformConfiguration", saved.getId());

        return toDto(saved);
    }

    /**
     * Get configuration by key
     */
    @Cacheable(value = "platformConfigs", key = "#configKey")
    public PlatformConfigDto getConfigurationByKey(String configKey) {
        log.info("Getting platform configuration: key={}", configKey);

        PlatformConfiguration config = configurationRepository.findByConfigKey(configKey)
            .orElseThrow(() -> new NotFoundException("Configuration not found: " + configKey));

        return toDto(config);
    }

    /**
     * Get all configurations for tenant
     */
    public List<PlatformConfigDto> getAllConfigurations() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return configurationRepository.findByTenantId(tenantId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Delete configuration
     */
    @Transactional
    @CacheEvict(value = "platformConfigs", allEntries = true)
    public void deleteConfiguration(String configId) {
        log.info("Deleting platform configuration: id={}", configId);

        PlatformConfiguration config = configurationRepository.findById(configId)
            .orElseThrow(() -> new NotFoundException("Configuration not found: " + configId));

        configurationRepository.delete(config);

        auditService.logEvent("system", "CONFIG_DELETED", "PlatformConfiguration", configId);
    }

    private PlatformConfigDto toDto(PlatformConfiguration config) {
        return PlatformConfigDto.builder()
            .id(config.getId())
            .tenantId(config.getTenantId())
            .configKey(config.getConfigKey())
            .configValue(config.isSensitive() ? "***HIDDEN***" : config.getConfigValue())
            .configType(config.getConfigType().name())
            .description(config.getDescription())
            .isSensitive(config.isSensitive())
            .isEncrypted(config.isEncrypted())
            .environment(config.getEnvironment() != null ? config.getEnvironment().name() : null)
            .version(config.getVersion())
            .effectiveFrom(config.getEffectiveFrom())
            .effectiveUntil(config.getEffectiveUntil())
            .tags(config.getTags())
            .metadata(config.getMetadata())
            .status(config.getStatus().name())
            .createdAt(config.getCreatedAt())
            .updatedAt(config.getUpdatedAt())
            .build();
    }
}
