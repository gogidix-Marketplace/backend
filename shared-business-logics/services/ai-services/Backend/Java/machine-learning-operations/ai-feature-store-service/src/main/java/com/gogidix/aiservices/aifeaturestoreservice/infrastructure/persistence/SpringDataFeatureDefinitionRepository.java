package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for FeatureDefinition.
 */
public interface SpringDataFeatureDefinitionRepository extends MongoRepository<FeatureDefinition, String> {

    Optional<FeatureDefinition> findByFeatureNameAndTenantId(String featureName, String tenantId);

    List<FeatureDefinition> findByTenantId(String tenantId, Pageable pageable);

    void deleteByFeatureNameAndTenantId(String featureName, String tenantId);

    boolean existsByFeatureNameAndTenantId(String featureName, String tenantId);
}
