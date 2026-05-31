package com.gogidix.aiservices.aimanagementservice.infrastructure.persistence;

import com.gogidix.aiservices.aimanagementservice.domain.model.MLModel;
import com.gogidix.aiservices.aimanagementservice.domain.port.out.ModelRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of ModelRepositoryPort.
 */
@Repository
public class MongoModelRepository implements ModelRepositoryPort {

    private final SpringDataModelRepository repository;

    public MongoModelRepository(SpringDataModelRepository repository) {
        this.repository = repository;
    }

    @Override
    public MLModel save(MLModel model) {
        return repository.save(model);
    }

    @Override
    public Optional<MLModel> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<MLModel> findByIdAndTenantId(String id, String tenantId) {
        return repository.findByIdAndTenantId(id, tenantId);
    }

    @Override
    public List<MLModel> findByTenantId(String tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByTenantId(tenantId, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        return repository.existsByIdAndTenantId(id, tenantId);
    }
}
