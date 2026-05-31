package com.gogidix.platform.platform.application.service;

import com.gogidix.platform.platform.application.dto.FeatureFlagDto;
import com.gogidix.platform.platform.domain.model.FeatureFlag;
import com.gogidix.platform.platform.domain.port.in.CreateFeatureFlagCommand;
import com.gogidix.platform.platform.domain.repository.FeatureFlagRepository;
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
 * Service for feature flag management.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureFlagService {

    private final FeatureFlagRepository featureFlagRepository;
    private final AuditService auditService;

    /**
     * Create a new feature flag
     */
    @Transactional
    @CacheEvict(value = "featureFlags", allEntries = true)
    public FeatureFlagDto createFeatureFlag(CreateFeatureFlagCommand command) {
        log.info("Creating feature flag: key={}", command.getFeatureKey());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        if (featureFlagRepository.findByFeatureKey(command.getFeatureKey()).isPresent()) {
            throw new ConflictException("Feature flag key already exists: " + command.getFeatureKey());
        }

        FeatureFlag flag = FeatureFlag.builder()
            .tenantId(tenantId)
            .featureKey(command.getFeatureKey())
            .featureName(command.getFeatureName())
            .description(command.getDescription())
            .featureType(command.getFeatureType())
            .isEnabled(command.isEnabled())
            .allowedTenants(command.getAllowedTenants())
            .deniedTenants(command.getDeniedTenants())
            .userSegments(command.getUserSegments())
            .rolloutRules(command.getRolloutRules() != null ? command.getRolloutRules() : new java.util.HashMap<>())
            .requiresOptIn(command.isRequiresOptIn())
            .rolloutPercentage(100)
            .build();

        FeatureFlag saved = featureFlagRepository.save(flag);

        auditService.logEvent("system", "FEATURE_FLAG_CREATED", "FeatureFlag", saved.getId());

        return toDto(saved);
    }

    /**
     * Get feature flag by key
     */
    @Cacheable(value = "featureFlags", key = "#featureKey")
    public FeatureFlagDto getFeatureFlag(String featureKey) {
        log.info("Getting feature flag: key={}", featureKey);

        FeatureFlag flag = featureFlagRepository.findByFeatureKey(featureKey)
            .orElseThrow(() -> new NotFoundException("Feature flag not found: " + featureKey));

        return toDto(flag);
    }

    /**
     * Check if feature is enabled
     */
    public boolean isFeatureEnabled(String featureKey, String userId) {
        FeatureFlag flag = featureFlagRepository.findByFeatureKey(featureKey)
            .orElseThrow(() -> new NotFoundException("Feature flag not found: " + featureKey));

        if (!flag.isEnabled()) {
            return false;
        }

        String tenantId = RequestContext.getTenantIdFromThreadLocal();

        if (flag.getDeniedTenants() != null && tenantId != null) {
            for (String denied : flag.getDeniedTenants()) {
                if (denied.equals(tenantId)) {
                    return false;
                }
            }
        }

        if (flag.getAllowedTenants() != null && tenantId != null) {
            boolean allowed = false;
            for (String allowedTenant : flag.getAllowedTenants()) {
                if (allowedTenant.equals(tenantId)) {
                    allowed = true;
                    break;
                }
            }
            if (!allowed && flag.getAllowedTenants().length > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get all feature flags
     */
    public List<FeatureFlagDto> getAllFeatureFlags() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new com.gogidix.shared.exceptions.ValidationException("Tenant ID not found");
        }

        return featureFlagRepository.findByTenantId(tenantId).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Toggle feature flag
     */
    @Transactional
    @CacheEvict(value = "featureFlags", allEntries = true)
    public FeatureFlagDto toggleFeatureFlag(String featureFlagId, boolean enabled) {
        log.info("Toggling feature flag: id={}, enabled={}", featureFlagId, enabled);

        FeatureFlag flag = featureFlagRepository.findById(featureFlagId)
            .orElseThrow(() -> new NotFoundException("Feature flag not found: " + featureFlagId));

        flag.setEnabled(enabled);
        FeatureFlag saved = featureFlagRepository.save(flag);

        auditService.logEvent("system", "FEATURE_FLAG_TOGGLED", "FeatureFlag", featureFlagId);

        return toDto(saved);
    }

    /**
     * Delete feature flag
     */
    @Transactional
    @CacheEvict(value = "featureFlags", allEntries = true)
    public void deleteFeatureFlag(String featureFlagId) {
        log.info("Deleting feature flag: id={}", featureFlagId);

        FeatureFlag flag = featureFlagRepository.findById(featureFlagId)
            .orElseThrow(() -> new NotFoundException("Feature flag not found: " + featureFlagId));

        featureFlagRepository.delete(flag);

        auditService.logEvent("system", "FEATURE_FLAG_DELETED", "FeatureFlag", featureFlagId);
    }

    private FeatureFlagDto toDto(FeatureFlag flag) {
        return FeatureFlagDto.builder()
            .id(flag.getId())
            .tenantId(flag.getTenantId())
            .featureKey(flag.getFeatureKey())
            .featureName(flag.getFeatureName())
            .description(flag.getDescription())
            .featureType(flag.getFeatureType().name())
            .enabled(flag.isEnabled())
            .allowedTenants(flag.getAllowedTenants())
            .deniedTenants(flag.getDeniedTenants())
            .userSegments(flag.getUserSegments())
            .rolloutRules(flag.getRolloutRules())
            .requiresOptIn(flag.isRequiresOptIn())
            .rolloutPercentage(flag.getRolloutPercentage())
            .createdAt(flag.getCreatedAt())
            .updatedAt(flag.getUpdatedAt())
            .build();
    }
}
