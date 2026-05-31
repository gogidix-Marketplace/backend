package com.gogidix.centralconfiguration.featureflagservice.application.service;

import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlag;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.FeatureFlagEvaluation;
import com.gogidix.centralconfiguration.featureflagservice.domain.model.RolloutStrategy;
import com.gogidix.centralconfiguration.featureflagservice.domain.port.in.CreateFeatureFlagCommand;
import com.gogidix.centralconfiguration.featureflagservice.domain.port.in.EvaluateFlagQuery;
import com.gogidix.centralconfiguration.featureflagservice.domain.repository.FeatureFlagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

/**
 * Service for Feature Flag operations.
 * Handles CQRS commands and queries for feature flags.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create a new feature flag
     */
    @Transactional
    public FeatureFlag createFeatureFlag(CreateFeatureFlagCommand command) {
        log.info("Creating feature flag: tenantId={}, key={}", command.tenantId(), command.flagKey());

        if (featureFlagRepository.existsByFlagKey(command.flagKey())) {
            throw new IllegalArgumentException("Feature flag already exists: " + command.flagKey());
        }

        FeatureFlag featureFlag = FeatureFlag.builder()
                .tenantId(command.tenantId())
                .flagKey(command.flagKey())
                .name(command.name())
                .description(command.description())
                .isEnabled(command.isEnabled())
                .rolloutPercentage(command.rolloutPercentage())
                .rolloutStrategy(command.rolloutStrategy())
                .whitelistedUsers(command.whitelistedUsers())
                .isSticky(command.isSticky())
                .tags(command.tags())
                .owner(command.owner())
                .createdBy(command.createdBy())
                .build();

        return featureFlagRepository.save(featureFlag);
    }

    /**
     * Evaluate a feature flag for a user
     */
    public FeatureFlagEvaluation evaluateFlag(EvaluateFlagQuery query) {
        String effectiveTenantId = query.tenantId() != null ? query.tenantId() : DEFAULT_TENANT_ID;

        FeatureFlag flag = featureFlagRepository.findByFlagKey(query.flagKey())
                .orElse(null);

        if (flag == null) {
            log.debug("Feature flag not found: {}", query.flagKey());
            return FeatureFlagEvaluation.disabled(query.flagKey(), query.userId(), effectiveTenantId, "Flag not found");
        }

        if (!flag.getTenantId().equals(effectiveTenantId)) {
            return FeatureFlagEvaluation.disabled(query.flagKey(), query.userId(), effectiveTenantId, "Access denied");
        }

        if (!flag.getIsEnabled()) {
            log.debug("Feature flag is disabled: {}", query.flagKey());
            return FeatureFlagEvaluation.disabled(query.flagKey(), query.userId(), effectiveTenantId, "Flag is disabled");
        }

        if (flag.isExpired()) {
            log.debug("Feature flag is expired: {}", query.flagKey());
            return FeatureFlagEvaluation.disabled(query.flagKey(), query.userId(), effectiveTenantId, "Flag is expired");
        }

        // Evaluate based on rollout strategy
        boolean enabled = evaluateFlagForUser(flag, query.userId(), query.context());

        return enabled
                ? FeatureFlagEvaluation.enabled(query.flagKey(), query.userId(), effectiveTenantId)
                : FeatureFlagEvaluation.disabled(query.flagKey(), query.userId(), effectiveTenantId, "User not in rollout");
    }

    /**
     * Evaluate if flag should be enabled for a specific user
     */
    private boolean evaluateFlagForUser(FeatureFlag flag, String userId, java.util.Map<String, Object> context) {
        return switch (flag.getRolloutStrategy()) {
            case ALL_USERS -> true;
            case WHITELIST -> userId != null && flag.isUserWhitelisted(userId);
            case PERCENTAGE -> isUserInPercentage(userId, flag.getRolloutPercentage());
            case GRADUAL -> isUserInPercentage(userId, flag.getRolloutPercentage());
            case BETA_TESTERS -> userId != null && flag.isUserWhitelisted(userId);
            case INTERNAL -> userId != null && flag.isUserWhitelisted(userId);
        };
    }

    /**
     * Check if user falls within rollout percentage using consistent hashing
     */
    private boolean isUserInPercentage(String userId, Integer percentage) {
        if (userId == null || userId.isEmpty()) {
            return false;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(userId.getBytes(StandardCharsets.UTF_8));
            int hashValue = ((hash[0] & 0xFF) << 24) | ((hash[1] & 0xFF) << 16) |
                           ((hash[2] & 0xFF) << 8) | (hash[3] & 0xFF);
            int normalized = Math.abs(hashValue % 100);
            return normalized < percentage;
        } catch (Exception e) {
            log.error("Error hashing user ID for percentage rollout", e);
            return false;
        }
    }

    /**
     * Get all feature flags for a tenant
     */
    public List<FeatureFlag> getFeatureFlags(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return featureFlagRepository.findByTenantId(effectiveTenantId);
    }

    /**
     * Get feature flag by ID
     */
    public FeatureFlag getFeatureFlag(Long id) {
        return featureFlagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Feature flag not found: " + id));
    }

    /**
     * Toggle feature flag
     */
    @Transactional
    public FeatureFlag toggleFlag(Long flagId, boolean enabled, String userId) {
        FeatureFlag flag = getFeatureFlag(flagId);

        if (enabled) {
            flag.enable();
        } else {
            flag.disable();
        }
        flag.setUpdatedBy(userId);

        return featureFlagRepository.save(flag);
    }

    /**
     * Delete feature flag
     */
    @Transactional
    public void deleteFeatureFlag(Long flagId) {
        FeatureFlag flag = getFeatureFlag(flagId);
        featureFlagRepository.delete(flag);
    }
}
