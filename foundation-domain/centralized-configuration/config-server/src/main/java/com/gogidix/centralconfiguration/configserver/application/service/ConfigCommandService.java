package com.gogidix.centralconfiguration.configserver.application.service;

import com.gogidix.centralconfiguration.configserver.application.dto.request.CreateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.request.UpdateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.mapper.ConfigMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.port.in.CreateConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.port.in.DeleteConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.port.in.UpdateConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationHistoryRepository;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationRepository;
import com.gogidix.centralconfiguration.configserver.infrastructure.messaging.kafka.ConfigEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * CQRS Command Handler for Configuration operations.
 * Handles all write operations for configuration entries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigCommandService {

    private final ConfigurationRepository configRepository;
    private final ConfigurationHistoryRepository historyRepository;
    private final ConfigMapper configMapper;
    private final ConfigEventPublisher eventPublisher;

    private static final String DEFAULT_TENANT_ID = "default";
    private static final String DEFAULT_USER = "system";

    /**
     * Create a new configuration entry
     */
    @Transactional
    public ConfigurationResponseDto createConfig(CreateConfigRequestDto request, String tenantId, String userId) {
        log.info("Creating config: tenantId={}, application={}, key={}",
                tenantId, request.getApplicationName(), request.getConfigKey());

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        String effectiveUserId = userId != null ? userId : DEFAULT_USER;

        // Check if config already exists
        if (configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                effectiveTenantId, request.getApplicationName(), request.getProfile(), request.getConfigKey())) {
            throw new IllegalArgumentException("Configuration already exists for the given key");
        }

        CreateConfigCommand command = configMapper.toCreateCommand(request, effectiveTenantId, effectiveUserId);

        Configuration config = Configuration.builder()
                .tenantId(command.tenantId())
                .applicationName(command.applicationName())
                .profile(command.profile())
                .configKey(command.configKey())
                .configValue(command.configValue())
                .isEncrypted(command.isEncrypted())
                .description(command.description())
                .createdBy(command.createdBy())
                .build();

        Configuration savedConfig = configRepository.save(config);

        // Create history entry
        ConfigurationHistory history = ConfigurationHistory.builder()
                .configurationId(savedConfig.getId())
                .tenantId(savedConfig.getTenantId())
                .applicationName(savedConfig.getApplicationName())
                .profile(savedConfig.getProfile())
                .configKey(savedConfig.getConfigKey())
                .oldValue(null)
                .newValue(savedConfig.getConfigValue())
                .version(savedConfig.getVersion())
                .changeType("CREATE")
                .changedBy(savedConfig.getCreatedBy())
                .build();
        historyRepository.save(history);

        // Publish event
        eventPublisher.publishConfigCreated(savedConfig);

        log.info("Configuration created: id={}, key={}", savedConfig.getId(), savedConfig.getConfigKey());

        return configMapper.toResponseDto(savedConfig);
    }

    /**
     * Update an existing configuration entry
     */
    @Transactional
    public ConfigurationResponseDto updateConfig(Long configId, UpdateConfigRequestDto request,
                                                 String tenantId, String userId) {
        log.info("Updating config: id={}, tenantId={}", configId, tenantId);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        String effectiveUserId = userId != null ? userId : DEFAULT_USER;

        Configuration config = configRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));

        // Verify tenant access
        if (!config.getTenantId().equals(effectiveTenantId)) {
            throw new IllegalArgumentException("Access denied: Configuration belongs to different tenant");
        }

        String oldValue = config.getConfigValue();

        UpdateConfigCommand command = configMapper.toUpdateCommand(
                configId, effectiveTenantId, request.getConfigValue(),
                request.getIsEncrypted(), request.getDescription(), effectiveUserId, request.getChangeReason());

        if (command.configValue() != null) {
            config.setConfigValue(command.configValue());
        }
        if (command.isEncrypted() != null) {
            config.setIsEncrypted(command.isEncrypted());
        }
        if (command.description() != null) {
            config.setDescription(command.description());
        }
        config.setUpdatedBy(command.updatedBy());

        Configuration savedConfig = configRepository.save(config);

        // Create history entry
        ConfigurationHistory history = ConfigurationHistory.builder()
                .configurationId(savedConfig.getId())
                .tenantId(savedConfig.getTenantId())
                .applicationName(savedConfig.getApplicationName())
                .profile(savedConfig.getProfile())
                .configKey(savedConfig.getConfigKey())
                .oldValue(oldValue)
                .newValue(savedConfig.getConfigValue())
                .version(savedConfig.getVersion())
                .changeType("UPDATE")
                .changedBy(savedConfig.getUpdatedBy())
                .changeReason(command.changeReason())
                .build();
        historyRepository.save(history);

        // Publish event
        eventPublisher.publishConfigUpdated(savedConfig, oldValue);

        log.info("Configuration updated: id={}, version={}", savedConfig.getId(), savedConfig.getVersion());

        return configMapper.toResponseDto(savedConfig);
    }

    /**
     * Delete a configuration entry
     */
    @Transactional
    public void deleteConfig(Long configId, String tenantId, String userId) {
        log.info("Deleting config: id={}, tenantId={}", configId, tenantId);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        String effectiveUserId = userId != null ? userId : DEFAULT_USER;

        Configuration config = configRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));

        // Verify tenant access
        if (!config.getTenantId().equals(effectiveTenantId)) {
            throw new IllegalArgumentException("Access denied: Configuration belongs to different tenant");
        }

        // Create history entry before deletion
        ConfigurationHistory history = ConfigurationHistory.builder()
                .configurationId(config.getId())
                .tenantId(config.getTenantId())
                .applicationName(config.getApplicationName())
                .profile(config.getProfile())
                .configKey(config.getConfigKey())
                .oldValue(config.getConfigValue())
                .newValue(null)
                .version(config.getVersion() + 1)
                .changeType("DELETE")
                .changedBy(effectiveUserId)
                .changeReason("Configuration deleted")
                .build();
        historyRepository.save(history);

        configRepository.delete(config);

        // Publish event
        eventPublisher.publishConfigDeleted(config);

        log.info("Configuration deleted: id={}", configId);
    }
}
