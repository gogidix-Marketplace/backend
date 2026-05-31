package com.gogidix.aiservices.aifeaturestoreservice.infrastructure.persistence;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureDefinition;
import com.gogidix.aiservices.aifeaturestoreservice.domain.port.out.FeatureDefinitionRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of FeatureDefinitionRepositoryPort.
 */
@Repository
public class MongoFeatureDefinitionRepository implements FeatureDefinitionRepositoryPort {

    private final SpringDataFeatureDefinitionRepository repository;

    public MongoFeatureDefinitionRepository(SpringDataFeatureDefinitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public FeatureDefinition save(FeatureDefinition definition) {
        return repository.save(definition);
    }

    @Override
    public Optional<FeatureDefinition> findByFeatureNameAndTenantId(String featureName, String tenantId) {
        return repository.findByFeatureNameAndTenantId(featureName, tenantId);
    }

    @Override
    public List<FeatureDefinition> findByTenantId(String tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByTenantId(tenantId, pageRequest);
    }

    @Override
    public void deleteByFeatureNameAndTenantId(String featureName, String tenantId) {
        repository.deleteByFeatureNameAndTenantId(featureName, tenantId);
    }

    @Override
    public boolean existsByFeatureNameAndTenantId(String featureName, String tenantId) {
        return repository.existsByFeatureNameAndTenantId(featureName, tenantId);
    }
}
