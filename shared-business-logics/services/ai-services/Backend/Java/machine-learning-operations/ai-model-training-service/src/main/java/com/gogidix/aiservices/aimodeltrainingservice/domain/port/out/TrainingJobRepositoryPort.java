package com.gogidix.aiservices.aimodeltrainingservice.domain.port.out;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingJob;

import java.util.List;
import java.util.Optional;

/**
 * Output port for training job repository operations.
 */
public interface TrainingJobRepositoryPort {

    TrainingJob save(TrainingJob job);

    Optional<TrainingJob> findByJobIdAndTenantId(String jobId, String tenantId);

    List<TrainingJob> findByTenantId(String tenantId, int page, int size);

    void deleteById(String id);
}
