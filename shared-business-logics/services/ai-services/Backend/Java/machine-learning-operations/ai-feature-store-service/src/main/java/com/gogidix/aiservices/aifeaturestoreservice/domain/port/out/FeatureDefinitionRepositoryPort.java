package com.gogidix.aiservices.aifeaturestoreservice.domain.port.out;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureDefinition;

import java.util.List;
import java.util.Optional;

/**
 * Output port for feature definition repository operations.
 */
public interface FeatureDefinitionRepositoryPort {

    FeatureDefinition save(FeatureDefinition definition);

    Optional<FeatureDefinition> findByFeatureNameAndTenantId(String featureName, String tenantId);

    List<FeatureDefinition> findByTenantId(String tenantId, int page, int size);

    void deleteByFeatureNameAndTenantId(String featureName, String tenantId);

    boolean existsByFeatureNameAndTenantId(String featureName, String tenantId);
}
