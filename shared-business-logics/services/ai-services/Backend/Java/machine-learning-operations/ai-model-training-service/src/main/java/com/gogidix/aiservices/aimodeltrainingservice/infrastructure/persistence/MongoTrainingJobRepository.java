package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.persistence;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingJob;
import com.gogidix.aiservices.aimodeltrainingservice.domain.port.out.TrainingJobRepositoryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB implementation of TrainingJobRepositoryPort.
 */
@Repository
public class MongoTrainingJobRepository implements TrainingJobRepositoryPort {

    private final SpringDataTrainingJobRepository repository;

    public MongoTrainingJobRepository(SpringDataTrainingJobRepository repository) {
        this.repository = repository;
    }

    @Override
    public TrainingJob save(TrainingJob job) {
        return repository.save(job);
    }

    @Override
    public Optional<TrainingJob> findByJobIdAndTenantId(String jobId, String tenantId) {
        return repository.findByJobIdAndTenantId(jobId, tenantId);
    }

    @Override
    public List<TrainingJob> findByTenantId(String tenantId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return repository.findByTenantId(tenantId, pageRequest);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
