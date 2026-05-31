package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.persistence;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for TrainingJob.
 */
public interface SpringDataTrainingJobRepository extends MongoRepository<TrainingJob, String> {

    Optional<TrainingJob> findByJobIdAndTenantId(String jobId, String tenantId);

    List<TrainingJob> findByTenantId(String tenantId, org.springframework.data.domain.Pageable pageable);
}
