package com.gogidix.aiservices.aifeaturestoreservice.domain.port.out;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureValue;

import java.util.List;
import java.util.Optional;

/**
 * Output port for feature value repository operations.
 */
public interface FeatureValueRepositoryPort {

    FeatureValue save(FeatureValue featureValue);

    List<FeatureValue> saveAll(List<FeatureValue> featureValues);

    List<FeatureValue> findByFeatureNameAndTenantId(String featureName, String tenantId);

    Optional<FeatureValue> findByFeatureNameAndEntityIdAndTenantId(String featureName, String entityId, String tenantId);

    void deleteByFeatureNameAndTenantId(String featureName, String tenantId);

    long countByFeatureNameAndTenantId(String featureName, String tenantId);
}
