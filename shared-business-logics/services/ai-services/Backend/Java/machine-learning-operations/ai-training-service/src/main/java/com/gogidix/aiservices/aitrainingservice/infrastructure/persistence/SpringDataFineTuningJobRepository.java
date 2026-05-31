package com.gogidix.aiservices.aitrainingservice.infrastructure.persistence;

import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningJob;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Spring Data MongoDB repository for FineTuningJob.
 */
public interface SpringDataFineTuningJobRepository extends MongoRepository<FineTuningJob, String> {

    Optional<FineTuningJob> findByJobIdAndTenantId(String jobId, String tenantId);
}
