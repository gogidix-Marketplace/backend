package com.gogidix.aiservices.aiinferenceservice.infrastructure.persistence;

import com.gogidix.aiservices.aiinferenceservice.domain.model.InferenceResult;
import com.gogidix.aiservices.aiinferenceservice.domain.port.out.InferenceResultRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of InferenceResultRepositoryPort.
 */
@Repository
public class MongoInferenceResultRepository implements InferenceResultRepositoryPort {

    private final SpringDataInferenceResultRepository repository;

    public MongoInferenceResultRepository(SpringDataInferenceResultRepository repository) {
        this.repository = repository;
    }

    @Override
    public InferenceResult save(InferenceResult result) {
        return repository.save(result);
    }

    @Override
    public Optional<InferenceResult> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<InferenceResult> findByTenantId(String tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByTenantId(tenantId, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
