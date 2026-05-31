package com.gogidix.centralconfiguration.environmentservice.application.service;

import com.gogidix.centralconfiguration.environmentservice.domain.model.Environment;
import com.gogidix.centralconfiguration.environmentservice.domain.repository.EnvironmentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for Environment operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Create a new environment
     */
    @Transactional
    public Environment createEnvironment(String tenantId, String environmentName, String displayName,
                                        String description, String type, String createdBy) {
        log.info("Creating environment: tenantId={}, name={}", tenantId, environmentName);

        if (environmentRepository.existsByEnvironmentName(environmentName)) {
            throw new IllegalArgumentException("Environment already exists: " + environmentName);
        }

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        Environment environment = Environment.builder()
                .tenantId(effectiveTenantId)
                .environmentName(environmentName)
                .displayName(displayName)
                .description(description)
                .environmentType(com.gogidix.centralconfiguration.environmentservice.domain.model.EnvironmentType.valueOf(type.toUpperCase()))
                .isActive(true)
                .createdBy(createdBy)
                .build();

        return environmentRepository.save(environment);
    }

    /**
     * Get all environments for tenant
     */
    public List<Environment> getEnvironments(String tenantId) {
        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;
        return environmentRepository.findByTenantId(effectiveTenantId);
    }

    /**
     * Get environment by name
     */
    public Environment getEnvironment(String environmentName) {
        return environmentRepository.findByEnvironmentName(environmentName)
                .orElseThrow(() -> new IllegalArgumentException("Environment not found: " + environmentName));
    }

    /**
     * Update environment
     */
    @Transactional
    public Environment updateEnvironment(Long environmentId, String displayName, String description, Boolean isActive) {
        Environment environment = environmentRepository.findById(environmentId)
                .orElseThrow(() -> new IllegalArgumentException("Environment not found: " + environmentId));

        if (displayName != null) {
            environment.setDisplayName(displayName);
        }
        if (description != null) {
            environment.setDescription(description);
        }
        if (isActive != null) {
            if (isActive) {
                environment.activate();
            } else {
                environment.deactivate();
            }
        }

        return environmentRepository.save(environment);
    }

    /**
     * Delete environment
     */
    @Transactional
    public void deleteEnvironment(Long environmentId) {
        Environment environment = environmentRepository.findById(environmentId)
                .orElseThrow(() -> new IllegalArgumentException("Environment not found: " + environmentId));
        environmentRepository.delete(environment);
    }
}
