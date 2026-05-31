package com.gogidix.aiservices.aitrainingservice.infrastructure.persistence;

import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningJob;
import com.gogidix.aiservices.aitrainingservice.domain.port.out.FineTuningJobRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * MongoDB implementation of FineTuningJobRepositoryPort.
 */
@Repository
public class MongoFineTuningJobRepository implements FineTuningJobRepositoryPort {

    private final SpringDataFineTuningJobRepository repository;

    public MongoFineTuningJobRepository(SpringDataFineTuningJobRepository repository) {
        this.repository = repository;
    }

    @Override
    public FineTuningJob save(FineTuningJob job) {
        return repository.save(job);
    }

    @Override
    public Optional<FineTuningJob> findByJobIdAndTenantId(String jobId, String tenantId) {
        return repository.findByJobIdAndTenantId(jobId, tenantId);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
