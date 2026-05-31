package com.gogidix.infrastructure.config.application.service;

import com.gogidix.infrastructure.config.domain.model.ConfigVersion;
import com.gogidix.infrastructure.config.domain.model.FeatureFlag;
import com.gogidix.infrastructure.config.domain.model.FeatureFlagCondition;
import com.gogidix.infrastructure.config.domain.repository.ConfigVersionRepository;
import com.gogidix.infrastructure.config.domain.repository.FeatureFlagRepository;
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
 * Service for managing feature flags.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository repository;
    private final ConfigVersionRepository versionRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String FEATURE_FLAG_CACHE = "featureFlag";
    private static final String CONFIG_CHANGED_TOPIC = "infrastructure.config.changed";

    /**
     * Creates a new feature flag.
     */
    @Transactional
    public FeatureFlag createFeatureFlag(CreateFeatureFlagRequest request, String userId) {
        log.info("Creating feature flag: tenantId={}, key={}", request.tenantId(), request.flagKey());

        // Check for existing flag
        repository.findByTenantIdAndFlagKey(request.tenantId(), request.flagKey())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Feature flag with key '" + request.flagKey() + "' already exists");
                });

        FeatureFlag flag = FeatureFlag.builder()
                .tenantId(request.tenantId())
                .flagKey(request.flagKey())
                .name(request.name())
                .description(request.description())
                .isEnabled(request.isEnabled() != null ? request.isEnabled() : false)
                .rolloutStrategy(request.rolloutStrategy())
                .rolloutPercentage(request.rolloutPercentage() != null ? request.rolloutPercentage() : 0)
                .whitelistedUsers(request.whitelistedUsers())
                .conditions(request.conditions())
                .isSticky(request.isSticky() != null ? request.isSticky() : true)
                .priority(request.priority() != null ? request.priority() : 0)
                .tags(request.tags())
                .owner(request.owner())
                .createdBy(userId)
                .lastUpdatedBy(userId)
                .metadata(request.metadata())
                .expiresAt(request.expiresAt())
                .build();

        FeatureFlag saved = repository.save(flag);

        // Create version record
        createVersionRecord(saved, null, String.valueOf(saved.getIsEnabled()),
                ConfigVersion.ChangeType.CREATED, userId, request.changeReason());

        // Publish event
        publishFeatureFlagChangedEvent(saved, "CREATED");

        log.info("Feature flag created: id={}", saved.getId());
        return saved;
    }

    /**
     * Updates an existing feature flag.
     */
    @Transactional
    @CacheEvict(value = FEATURE_FLAG_CACHE, key = "#tenantId + ':' + #flagKey")
    public FeatureFlag updateFeatureFlag(String id, UpdateFeatureFlagRequest request,
            String tenantId, String flagKey, String userId) {
        log.info("Updating feature flag: id={}", id);

        FeatureFlag flag = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + id));

        // Verify tenant and key
        if (!flag.getTenantId().equals(tenantId) || !flag.getFlagKey().equals(flagKey)) {
            throw new IllegalArgumentException("Feature flag mismatch");
        }

        String previousValue = String.valueOf(flag.getIsEnabled());

        // Update fields
        if (request.name() != null) {
            flag.setName(request.name());
        }
        if (request.description() != null) {
            flag.setDescription(request.description());
        }
        if (request.isEnabled() != null) {
            flag.setIsEnabled(request.isEnabled());
        }
        if (request.rolloutStrategy() != null) {
            flag.setRolloutStrategy(request.rolloutStrategy());
        }
        if (request.rolloutPercentage() != null) {
            flag.setRolloutPercentage(request.rolloutPercentage());
        }
        if (request.whitelistedUsers() != null) {
            flag.setWhitelistedUsers(request.whitelistedUsers());
        }
        if (request.conditions() != null) {
            flag.setConditions(request.conditions());
        }
        if (request.isSticky() != null) {
            flag.setSticky(request.isSticky());
        }
        if (request.priority() != null) {
            flag.setPriority(request.priority());
        }
        if (request.tags() != null) {
            flag.setTags(request.tags());
        }
        if (request.owner() != null) {
            flag.setOwner(request.owner());
        }
        if (request.expiresAt() != null) {
            flag.setExpiresAt(request.expiresAt());
        }
        if (request.metadata() != null) {
            flag.setMetadata(request.metadata());
        }

        flag.setLastUpdatedBy(userId);

        FeatureFlag saved = repository.save(flag);

        // Create version record
        createVersionRecord(saved, previousValue, String.valueOf(saved.getIsEnabled()),
                ConfigVersion.ChangeType.UPDATED, userId, request.changeReason());

        // Publish event
        publishFeatureFlagChangedEvent(saved, "UPDATED");

        log.info("Feature flag updated: id={}", saved.getId());
        return saved;
    }

    /**
     * Evaluates a feature flag for a specific user.
     */
    @Cacheable(value = FEATURE_FLAG_CACHE,
            key = "#tenantId + ':' + #flagKey + ':' + #userId + ':' + #context.hashCode()")
    @Transactional(readOnly = true)
    public FeatureFlagEvaluation evaluateFlag(String tenantId, String flagKey,
            String userId, Map<String, Object> context) {
        log.debug("Evaluating feature flag: tenantId={}, key={}, userId={}", tenantId, flagKey, userId);

        FeatureFlag flag = repository.findByTenantIdAndFlagKey(tenantId, flagKey)
                .orElse(null);

        if (flag == null) {
            return FeatureFlagEvaluation.builder()
                    .flagKey(flagKey)
                    .enabled(false)
                    .reason("FLAG_NOT_FOUND")
                    .evaluatedAt(LocalDateTime.now())
                    .build();
        }

        boolean enabled = flag.isEnabledForUser(userId, context != null ? context : Map.of());

        return FeatureFlagEvaluation.builder()
                .flagKey(flagKey)
                .enabled(enabled)
                .reason(enabled ? "FLAG_ENABLED" : "FLAG_DISABLED")
                .rolloutStrategy(flag.getRolloutStrategy())
                .isSticky(flag.isSticky())
                .userId(userId)
                .evaluatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Evaluates multiple feature flags at once.
     */
    @Transactional(readOnly = true)
    public Map<String, Boolean> evaluateFlags(String tenantId, Set<String> flagKeys,
            String userId, Map<String, Object> context) {
        Map<String, Boolean> results = new HashMap<>();

        for (String flagKey : flagKeys) {
            try {
                FeatureFlagEvaluation evaluation = evaluateFlag(tenantId, flagKey, userId, context);
                results.put(flagKey, evaluation.isEnabled());
            } catch (Exception e) {
                log.warn("Failed to evaluate flag: {}", flagKey, e);
                results.put(flagKey, false);
            }
        }

        return results;
    }

    /**
     * Gets all feature flags for a tenant.
     */
    @Transactional(readOnly = true)
    public List<FeatureFlag> getFeatureFlags(String tenantId) {
        return repository.findByTenantId(tenantId);
    }

    /**
     * Gets active feature flags for a tenant.
     */
    @Transactional(readOnly = true)
    public List<FeatureFlag> getActiveFeatureFlags(String tenantId) {
        return repository.findActiveFlags(tenantId, LocalDateTime.now());
    }

    /**
     * Gets a feature flag by ID.
     */
    @Transactional(readOnly = true)
    public FeatureFlag getFeatureFlag(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + id));
    }

    /**
     * Toggles a feature flag on/off.
     */
    @Transactional
    @CacheEvict(value = FEATURE_FLAG_CACHE, key = "#tenantId + ':' + #flagKey")
    public FeatureFlag toggleFlag(String id, boolean enabled, String tenantId,
            String flagKey, String userId) {
        log.info("Toggling feature flag: id={}, enabled={}", id, enabled);

        FeatureFlag flag = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + id));

        if (!flag.getTenantId().equals(tenantId) || !flag.getFlagKey().equals(flagKey)) {
            throw new IllegalArgumentException("Feature flag mismatch");
        }

        boolean previousState = flag.getIsEnabled();
        flag.setIsEnabled(enabled);
        flag.setLastToggledAt(LocalDateTime.now());
        flag.setLastToggledBy(userId);
        flag.setLastUpdatedBy(userId);

        FeatureFlag saved = repository.save(flag);

        // Create version record
        createVersionRecord(saved, String.valueOf(previousState), String.valueOf(enabled),
                enabled ? ConfigVersion.ChangeType.ENABLED : ConfigVersion.ChangeType.DISABLED,
                userId, null);

        // Publish event
        publishFeatureFlagChangedEvent(saved, enabled ? "ENABLED" : "DISABLED");

        log.info("Feature flag toggled: id={}, enabled={}", saved.getId(), enabled);
        return saved;
    }

    /**
     * Deletes a feature flag.
     */
    @Transactional
    @CacheEvict(value = FEATURE_FLAG_CACHE, key = "#tenantId + ':' + #flagKey")
    public void deleteFeatureFlag(String id, String tenantId, String flagKey, String userId) {
        log.info("Deleting feature flag: id={}", id);

        FeatureFlag flag = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + id));

        if (!flag.getTenantId().equals(tenantId) || !flag.getFlagKey().equals(flagKey)) {
            throw new IllegalArgumentException("Feature flag mismatch");
        }

        // Create version record
        createVersionRecord(flag, String.valueOf(flag.getIsEnabled()), null,
                ConfigVersion.ChangeType.DELETED, userId, null);

        repository.delete(flag);

        // Publish event
        publishFeatureFlagChangedEvent(flag, "DELETED");

        log.info("Feature flag deleted: id={}", id);
    }

    /**
     * Searches feature flags by key or name pattern.
     */
    @Transactional(readOnly = true)
    public List<FeatureFlag> searchFeatureFlags(String tenantId, String pattern) {
        return repository.searchByKeyOrName(tenantId, pattern);
    }

    /**
     * Gets feature flags with pagination.
     */
    @Transactional(readOnly = true)
    public Page<FeatureFlag> getFeatureFlags(String tenantId, Pageable pageable) {
        return repository.findByTenantId(tenantId, pageable);
    }

    /**
     * Gets version history for a feature flag.
     */
    @Transactional(readOnly = true)
    public List<ConfigVersion> getVersionHistory(String configId) {
        return versionRepository.findByConfigIdOrderByVersionDesc(configId);
    }

    /**
     * Creates a version record for a feature flag change.
     */
    private void createVersionRecord(FeatureFlag flag, String previousValue,
            String newValue, ConfigVersion.ChangeType changeType, String userId, String reason) {

        ConfigVersion version = ConfigVersion.builder()
                .tenantId(flag.getTenantId())
                .configId(flag.getId())
                .configType(ConfigVersion.ConfigType.FEATURE_FLAG)
                .configKey(flag.getFlagKey())
                .version(flag.getVersion())
                .previousValue(previousValue)
                .newValue(newValue)
                .changeType(changeType)
                .changedBy(userId)
                .changeReason(reason)
                .changedAt(LocalDateTime.now())
                .snapshot(createSnapshot(flag))
                .build();

        versionRepository.save(version);
    }

    /**
     * Creates a snapshot map of the feature flag.
     */
    private Map<String, Object> createSnapshot(FeatureFlag flag) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("name", flag.getName());
        snapshot.put("description", flag.getDescription());
        snapshot.put("isEnabled", flag.getIsEnabled());
        snapshot.put("rolloutStrategy", flag.getRolloutStrategy());
        snapshot.put("rolloutPercentage", flag.getRolloutPercentage());
        snapshot.put("whitelistedUsers", flag.getWhitelistedUsers());
        snapshot.put("conditions", flag.getConditions());
        snapshot.put("isSticky", flag.isSticky());
        snapshot.put("priority", flag.getPriority());
        snapshot.put("tags", flag.getTags());
        snapshot.put("owner", flag.getOwner());
        return snapshot;
    }

    /**
     * Publishes a feature flag changed event to Kafka.
     */
    private void publishFeatureFlagChangedEvent(FeatureFlag flag, String action) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("action", action);
            event.put("tenantId", flag.getTenantId());
            event.put("key", flag.getFlagKey());
            event.put("type", "FEATURE_FLAG");
            event.put("timestamp", LocalDateTime.now().toString());

            String eventJson = toJson(event);
            kafkaTemplate.send(CONFIG_CHANGED_TOPIC, flag.getTenantId(), eventJson);
        } catch (Exception e) {
            log.warn("Failed to publish feature flag changed event", e);
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
