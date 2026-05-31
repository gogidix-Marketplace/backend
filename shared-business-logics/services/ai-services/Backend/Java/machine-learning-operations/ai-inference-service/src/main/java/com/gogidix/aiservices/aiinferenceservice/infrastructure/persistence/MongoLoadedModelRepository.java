package com.gogidix.aiservices.aiinferenceservice.infrastructure.persistence;

import com.gogidix.aiservices.aiinferenceservice.domain.model.LoadedModel;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.LoadedModelRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB implementation of LoadedModelRepositoryPort.
 */
@Repository
public class MongoLoadedModelRepository implements LoadedModelRepositoryPort {

    private final SpringDataLoadedModelRepository repository;

    public MongoLoadedModelRepository(SpringDataLoadedModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public LoadedModel save(LoadedModel model) {
        return repository.save(model);
    }

    @Override
    public Optional<LoadedModel> findByModelIdAndTenantId(String modelId, String tenantId) {
        return repository.findByModelIdAndTenantId(modelId, tenantId);
    }

    @Override
    public void deleteByModelIdAndTenantId(String modelId, String tenantId) {
        repository.deleteByModelIdAndTenantId(modelId, tenantId);
    }
}
