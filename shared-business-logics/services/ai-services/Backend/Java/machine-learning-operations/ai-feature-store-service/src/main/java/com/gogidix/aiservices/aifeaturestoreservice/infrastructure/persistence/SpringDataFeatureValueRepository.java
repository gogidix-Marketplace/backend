package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureValue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for FeatureValue.
 */
public interface SpringDataFeatureValueRepository extends MongoRepository<FeatureValue, String> {

    List<FeatureValue> findByFeatureNameAndTenantId(String featureName, String tenantId);

    Optional<FeatureValue> findByFeatureNameAndEntityIdAndTenantId(String featureName, String entityId, String tenantId);

    void deleteByFeatureNameAndTenantId(String featureName, String tenantId);

    long countByFeatureNameAndTenantId(String featureName, String tenantId);
}
