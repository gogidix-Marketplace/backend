package com.gogidix.aiservices.aitrainingservice.domain.port.out;

import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningJob;

import java.util.Optional;

/**
 * Output port for fine-tuning job repository operations.
 */
public interface FineTuningJobRepositoryPort {

    FineTuningJob save(FineTuningJob job);

    Optional<FineTuningJob> findByJobIdAndTenantId(String jobId, String tenantId);

    void deleteById(String id);
}
