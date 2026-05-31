package com.gogidix.infrastructure.config.application.service;

import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.ConfigurationProperty;
import com.gogidix.infrastructure.config.domain.repository.ConfigVersionRepository;
import com.gogidix.infrastructure.config.domain.repository.ConfigurationPropertyRepository;
import com.gogidix.infrastructure.config.infrastructure.crypto.EncryptionService;
import com.gogidix.infrastructure.config.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing configuration properties.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationPropertyService {

    private final ConfigurationPropertyRepository repository;
    private final ConfigVersionRepository versionRepository;
    private final EncryptionService encryptionService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String CONFIG_CACHE = "config";
    private static final String CONFIG_CHANGED_TOPIC = "infrastructure.config.changed";

    /**
     * Creates a new configuration property.
     */
    @Transactional
    public ConfigurationProperty createConfiguration(CreateConfigurationRequest request, String userId) {
        log.info("Creating configuration: tenantId={}, key={}", request.tenantId(), request.key());

        // Check for existing configuration
        repository.findByTenantIdAndKey(request.tenantId(), request.key())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Configuration with key '" + request.key() + "' already exists");
                });

        ConfigurationProperty config = ConfigurationProperty.builder()
                .tenantId(request.tenantId())
                .environment(request.environment())
                .key(request.key())
                .name(request.name())
                .description(request.description())
                .value(request.value())
                .defaultValue(request.defaultValue())
                .valueType(request.valueType())
                .active(request.isActive() != null ? request.isActive() : true)
                .sensitive(request.isSensitive() != null ? request.isSensitive() : false)
                .required(request.isRequired() != null ? request.isRequired() : false)
                .validationRule(request.validationRule())
                .tags(request.tags())
                .category(request.category())
                .owner(request.owner())
                .createdBy(userId)
                .lastUpdatedBy(userId)
                .metadata(request.metadata())
                .validUntil(request.validUntil())
                .build();

        // Encrypt sensitive value
        if (config.isSensitive() && config.getValue() != null) {
            config.setValue(encryptionService.encrypt(config.getValue()));
        }

        // Validate
        if (!config.validate()) {
            throw new IllegalArgumentException("Configuration value validation failed");
        }

        ConfigurationProperty saved = repository.save(config);

        // Create version record
        createVersionRecord(saved, null, saved.getValue(), ConfigVersion.ChangeType.CREATED, userId, request.changeReason());

        // Publish event
        publishConfigChangedEvent(saved, "CREATED");

        log.info("Configuration created: id={}", saved.getId());
        return saved;
    }

    /**
     * Updates an existing configuration property.
     */
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#tenantId + ':' + #key")
    public ConfigurationProperty updateConfiguration(String id, UpdateConfigurationRequest request,
            String tenantId, String key, String userId) {
        log.info("Updating configuration: id={}", id);

        ConfigurationProperty config = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + id));

        // Verify tenant and key
        if (!config.getTenantId().equals(tenantId) || !config.getKey().equals(key)) {
            throw new IllegalArgumentException("Configuration mismatch");
        }

        String previousValue = config.getValue();

        // Update fields
        if (request.name() != null) {
            config.setName(request.name());
        }
        if (request.description() != null) {
            config.setDescription(request.description());
        }
        if (request.value() != null) {
            String newValue = request.value();
            if (config.isSensitive()) {
                newValue = encryptionService.encrypt(newValue);
            }
            config.setValue(newValue);
        }
        if (request.defaultValue() != null) {
            config.setDefaultValue(request.defaultValue());
        }
        if (request.valueType() != null) {
            config.setValueType(request.valueType());
        }
        if (request.isActive() != null) {
            config.setActive(request.isActive());
        }
        if (request.validationRule() != null) {
            config.setValidationRule(request.validationRule());
        }
        if (request.tags() != null) {
            config.setTags(request.tags());
        }
        if (request.category() != null) {
            config.setCategory(request.category());
        }
        if (request.owner() != null) {
            config.setOwner(request.owner());
        }
        if (request.validUntil() != null) {
            config.setValidUntil(request.validUntil());
        }
        if (request.metadata() != null) {
            config.setMetadata(request.metadata());
        }

        config.setLastUpdatedBy(userId);

        // Validate
        if (!config.validate()) {
            throw new IllegalArgumentException("Configuration value validation failed");
        }

        ConfigurationProperty saved = repository.save(config);

        // Create version record
        createVersionRecord(saved, previousValue, saved.getValue(),
                ConfigVersion.ChangeType.UPDATED, userId, request.changeReason());

        // Publish event
        publishConfigChangedEvent(saved, "UPDATED");

        log.info("Configuration updated: id={}", saved.getId());
        return saved;
    }

    /**
     * Gets a configuration property by tenant and key.
     */
    @Cacheable(value = CONFIG_CACHE, key = "#tenantId + ':' + #key")
    @Transactional(readOnly = true)
    public ConfigurationProperty getConfiguration(String tenantId, String key) {
        log.debug("Getting configuration: tenantId={}, key={}", tenantId, key);
        return repository.findByTenantIdAndKey(tenantId, key)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Configuration not found: " + key));
    }

    /**
     * Gets a configuration property by ID.
     */
    @Transactional(readOnly = true)
    public ConfigurationProperty getConfigurationById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + id));
    }

    /**
     * Gets all configuration properties for a tenant.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationProperty> getConfigurations(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    /**
     * Gets configuration properties for a tenant and environment.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationProperty> getConfigurationsByEnvironment(
            String tenantId, ConfigurationProperty.Environment environment) {
        return repository.findByTenantIdAndEnvironment(tenantId, environment);
    }

    /**
     * Gets active configuration properties for a tenant and environment.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationProperty> getActiveConfigurations(
            String tenantId, ConfigurationProperty.Environment environment) {
        return repository.findByTenantIdAndEnvironmentAndIsActive(tenantId, environment, true)
                .stream()
                .filter(config -> config.isValid())
                .collect(Collectors.toList());
    }

    /**
     * Searches configuration properties by key pattern.
     */
    @Transactional(readOnly = true)
    public List<ConfigurationProperty> searchConfigurations(String tenantId, String pattern) {
        return repository.searchByKey(tenantId, pattern);
    }

    /**
     * Gets configuration properties with pagination.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationProperty> getConfigurations(String tenantId, Pageable pageable) {
        return repository.findByTenantId(tenantId, pageable);
    }

    /**
     * Deletes a configuration property.
     */
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#tenantId + ':' + #key")
    public void deleteConfiguration(String id, String tenantId, String key, String userId) {
        log.info("Deleting configuration: id={}", id);

        ConfigurationProperty config = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + id));

        if (!config.getTenantId().equals(tenantId) || !config.getKey().equals(key)) {
            throw new IllegalArgumentException("Configuration mismatch");
        }

        // Create version record before deletion
        createVersionRecord(config, config.getValue(), null,
                ConfigVersion.ChangeType.DELETED, userId, null);

        repository.delete(config);

        // Publish event
        publishConfigChangedEvent(config, "DELETED");

        log.info("Configuration deleted: id={}", id);
    }

    /**
     * Gets configuration value as a specific type.
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfigurationValue(String tenantId, String key, Class<T> type) {
        ConfigurationProperty config = getConfiguration(tenantId, key);
        String value = config.getEffectiveValue();

        if (value == null) {
            return null;
        }

        // Decrypt if sensitive
        if (config.isSensitive()) {
            value = encryptionService.decrypt(value);
        }

        if (type == String.class) {
            return (T) value;
        } else if (type == Integer.class || type == int.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Long.class || type == long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Boolean.class || type == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (type == Double.class || type == double.class) {
            return (T) Double.valueOf(value);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    /**
     * Gets multiple configuration values.
     */
    @Transactional(readOnly = true)
    public Map<String, String> getConfigurationValues(String tenantId, Set<String> keys) {
        Map<String, String> values = new HashMap<>();
        for (String key : keys) {
            try {
                ConfigurationProperty config = getConfiguration(tenantId, key);
                String value = config.getEffectiveValue();
                if (config.isSensitive() && value != null) {
                    value = encryptionService.decrypt(value);
                }
                values.put(key, value);
            } catch (IllegalArgumentException e) {
                values.put(key, null);
            }
        }
        return values;
    }

    /**
     * Gets version history for a configuration.
     */
    @Transactional(readOnly = true)
    public List<ConfigVersion> getVersionHistory(String configId) {
        return versionRepository.findByConfigIdOrderByVersionDesc(configId);
    }

    /**
     * Rolls back a configuration to a previous version.
     */
    @Transactional
    @CacheEvict(value = CONFIG_CACHE, key = "#config.tenantId + ':' + #config.key")
    public ConfigurationProperty rollbackToVersion(String configId, Integer version, String userId) {
        log.info("Rolling back configuration: configId={}, version={}", configId, version);

        ConfigurationProperty config = repository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));

        ConfigVersion targetVersion = Optional.ofNullable(
                versionRepository.findByConfigIdAndVersion(configId, version))
                .orElseThrow(() -> new IllegalArgumentException("Version not found: " + version));

        if (!targetVersion.isCanRollback()) {
            throw new IllegalArgumentException("This version cannot be rolled back to");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> snapshot = (Map<String, Object>) targetVersion.getSnapshot();

        String previousValue = config.getValue();

        // Restore from snapshot
        config.setName((String) snapshot.get("name"));
        config.setDescription((String) snapshot.get("description"));
        config.setValue((String) snapshot.get("value"));
        config.setDefaultValue((String) snapshot.get("defaultValue"));
        config.setValueType((ConfigurationProperty.ValueType) snapshot.get("valueType"));
        config.setActive((Boolean) snapshot.getOrDefault("active", true));
        config.setSensitive((Boolean) snapshot.getOrDefault("sensitive", false));
        config.setRequired((Boolean) snapshot.getOrDefault("required", false));
        config.setTags((Set<String>) snapshot.get("tags"));
        config.setCategory((String) snapshot.get("category"));
        config.setOwner((String) snapshot.get("owner"));
        config.setLastUpdatedBy(userId);

        config.incrementVersion();

        ConfigurationProperty saved = repository.save(config);

        // Create rollback version record
        createVersionRecord(saved, previousValue, saved.getValue(),
                ConfigVersion.ChangeType.ROLLED_BACK, userId,
                "Rolled back to version " + version);

        // Publish event
        publishConfigChangedEvent(saved, "ROLLED_BACK");

        log.info("Configuration rolled back: id={}, newVersion={}", saved.getId(), saved.getVersion());
        return saved;
    }

    /**
     * Creates a version record for a configuration change.
     */
    private void createVersionRecord(ConfigurationProperty config, String previousValue,
            String newValue, ConfigVersion.ChangeType changeType, String userId, String reason) {

        ConfigVersion version = ConfigVersion.builder()
                .tenantId(config.getTenantId())
                .configId(config.getId())
                .configType(ConfigVersion.ConfigType.CONFIGURATION_PROPERTY)
                .configKey(config.getKey())
                .version(config.getVersion())
                .previousValue(previousValue)
                .newValue(newValue)
                .changeType(changeType)
                .changedBy(userId)
                .changeReason(reason)
                .changedAt(LocalDateTime.now())
                .snapshot(createSnapshot(config))
                .build();

        versionRepository.save(version);
    }

    /**
     * Creates a snapshot map of the configuration.
     */
    private Map<String, Object> createSnapshot(ConfigurationProperty config) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("name", config.getName());
        snapshot.put("description", config.getDescription());
        snapshot.put("value", config.getValue());
        snapshot.put("defaultValue", config.getDefaultValue());
        snapshot.put("valueType", config.getValueType());
        snapshot.put("active", config.isActive());
        snapshot.put("sensitive", config.isSensitive());
        snapshot.put("required", config.isRequired());
        snapshot.put("tags", config.getTags());
        snapshot.put("category", config.getCategory());
        snapshot.put("owner", config.getOwner());
        return snapshot;
    }

    /**
     * Publishes a configuration changed event to Kafka.
     */
    private void publishConfigChangedEvent(ConfigurationProperty config, String action) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("action", action);
            event.put("tenantId", config.getTenantId());
            event.put("key", config.getKey());
            event.put("type", "CONFIGURATION_PROPERTY");
            event.put("timestamp", LocalDateTime.now().toString());

            String eventJson = toJson(event);
            kafkaTemplate.send(CONFIG_CHANGED_TOPIC, config.getTenantId(), eventJson);
        } catch (Exception e) {
            log.warn("Failed to publish config changed event", e);
        }
    }

    private String toJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        map.forEach((k, v) -> sb.append("\"").append(k).append("\":\"").append(v).append("\","));
        if (!map.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }
        sb.append("}");
        return sb.toString();
    }
}
