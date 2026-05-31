package com.gogidix.aiservices.aimodeltrainingservice.application.service;

import com.gogidix.aiservices.aimodeltrainingservice.application.dto.StartTrainingJobRequestDto;
import com.gogidix.aiservices.aimodeltrainingservice.application.dto.TrainingJobResponseDto;
import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingJob;
import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingStatus;
import com.gogidix.aiservices.aimodeltrainingservice.domain.port.out.TrainingJobRepositoryPort;
import com.gogidix.aiservices.aimodeltrainingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Application service for model training operations.
 */
@Service
@Transactional
public class ModelTrainingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(ModelTrainingApplicationService.class);

    private final TrainingJobRepositoryPort trainingJobRepository;

    public ModelTrainingApplicationService(TrainingJobRepositoryPort trainingJobRepository) {
        this.trainingJobRepository = trainingJobRepository;
    }

    public TrainingJobResponseDto startTrainingJob(StartTrainingJobRequestDto request) {
        String tenantId = "tenant-" + UUID.randomUUID().toString().substring(0, 8);

        TrainingJob job = new TrainingJob(
                tenantId,
                request.modelType(),
                request.trainingDataUrl(),
                request.algorithm(),
                request.hyperparameters()
        );

        job.validate();
        job.start();
        job = trainingJobRepository.save(job);

        log.info("Started training job: {} for model type: {}", job.getJobId(), request.modelType());

        return new TrainingJobResponseDto(
                job.getJobId(),
                job.getModelType(),
                job.getStatus(),
                null,
                null,
                job.getStartedAt(),
                Instant.now().plusSeconds(3600)
        );
    }

    @Transactional(readOnly = true)
    public TrainingJobResponseDto getTrainingStatus(String jobId, String tenantId) {
        TrainingJob job = trainingJobRepository.findByJobIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new ValidationException("Training job not found"));

        return new TrainingJobResponseDto(
                job.getJobId(),
                job.getModelType(),
                job.getStatus(),
                job.getModelArtifactUrl(),
                job.getMetrics(),
                job.getStartedAt(),
                job.getCompletedAt() != null ? job.getCompletedAt() : Instant.now().plusSeconds(3600)
        );
    }

    public void cancelTrainingJob(String jobId, String tenantId) {
        TrainingJob job = trainingJobRepository.findByJobIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new ValidationException("Training job not found"));

        job.cancel();
        trainingJobRepository.save(job);

        log.info("Cancelled training job: {}", jobId);
    }

    private Map<String, Double> simulateTraining(String modelType, Map<String, Object> hyperparameters) {
        return Map.of(
                "accuracy", 0.85 + Math.random() * 0.1,
                "precision", 0.80 + Math.random() * 0.1,
                "recall", 0.75 + Math.random() * 0.1,
                "f1_score", 0.82 + Math.random() * 0.1
        );
    }
}
