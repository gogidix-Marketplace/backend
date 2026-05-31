package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureValue;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureValueRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of FeatureValueRepositoryPort.
 */
@Repository
public class MongoFeatureValueRepository implements FeatureValueRepositoryPort {

    private final SpringDataFeatureValueRepository repository;

    public MongoFeatureValueRepository(SpringDataFeatureValueRepository repository) {
        this.repository = repository;
    }

    @Override
    public FeatureValue save(FeatureValue featureValue) {
        return repository.save(featureValue);
    }

    @Override
    public List<FeatureValue> saveAll(List<FeatureValue> featureValues) {
        return repository.saveAll(featureValues);
    }

    @Override
    public List<FeatureValue> findByFeatureNameAndTenantId(String featureName, String tenantId) {
        return repository.findByFeatureNameAndTenantId(featureName, tenantId);
    }

    @Override
    public Optional<FeatureValue> findByFeatureNameAndEntityIdAndTenantId(String featureName, String entityId, String tenantId) {
        return repository.findByFeatureNameAndEntityIdAndTenantId(featureName, entityId, tenantId);
    }

    @Override
    public void deleteByFeatureNameAndTenantId(String featureName, String tenantId) {
        repository.deleteByFeatureNameAndTenantId(featureName, tenantId);
    }

    @Override
    public long countByFeatureNameAndTenantId(String featureName, String tenantId) {
        return repository.countByFeatureNameAndTenantId(featureName, tenantId);
    }
}
