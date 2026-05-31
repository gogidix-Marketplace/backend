package com.gogidix.aiservices.aitrainingservice.application.service;

import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuneRequestDto;
import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuningJobResponseDto;
import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuningJobResponseDto.FineTuningJobMetricsDto;
import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningJob;
import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningStatus;
import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningJob.TrainingMetrics;
import com.gogidix.aiservices.aitrainingservice.domain.port.out.FineTuningJobRepositoryPort;
import com.gogidix.aiservices.aitrainingservice.shared.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Application service for training operations.
 */
@Service
@Transactional
public class TrainingApplicationService {

    private static final Logger log = LoggerFactory.getLogger(TrainingApplicationService.class);

    private final FineTuningJobRepositoryPort jobRepository;

    public TrainingApplicationService(FineTuningJobRepositoryPort jobRepository) {
        this.jobRepository = jobRepository;
    }

    public FineTuningJobResponseDto fineTuneModel(FineTuneRequestDto request) {
        String tenantId = "tenant-" + UUID.randomUUID().toString().substring(0, 8);

        FineTuningJob job = new FineTuningJob(
                tenantId,
                request.baseModel(),
                request.trainingDataUrl(),
                request.epochs(),
                request.learningRate()
        );

        job.validate();
        job.start();
        job = jobRepository.save(job);

        log.info("Started fine-tuning job: {} for base model: {}", job.getJobId(), request.baseModel());

        return new FineTuningJobResponseDto(
                job.getJobId(),
                job.getBaseModel(),
                job.getStatus(),
                null,
                null,
                job.getCreatedAt(),
                Instant.now().plusSeconds(request.epochs() * 60L)
        );
    }

    @Transactional(readOnly = true)
    public FineTuningJobResponseDto getFineTuningStatus(String jobId, String tenantId) {
        FineTuningJob job = jobRepository.findByJobIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new ValidationException("Fine-tuning job not found"));

        FineTuningJobMetricsDto metricsDto = null;
        if (job.getMetrics() != null) {
            metricsDto = new FineTuningJobMetricsDto(
                    job.getMetrics().loss(),
                    job.getMetrics().accuracy(),
                    job.getMetrics().epochsCompleted()
            );
        }

        return new FineTuningJobResponseDto(
                job.getJobId(),
                job.getBaseModel(),
                job.getStatus(),
                job.getFineTunedModelUrl(),
                metricsDto,
                job.getCreatedAt(),
                job.getCompletedAt() != null ? job.getCompletedAt() : Instant.now().plusSeconds(3600)
        );
    }

    public void cancelFineTuning(String jobId, String tenantId) {
        FineTuningJob job = jobRepository.findByJobIdAndTenantId(jobId, tenantId)
                .orElseThrow(() -> new ValidationException("Fine-tuning job not found"));

        job.cancel();
        jobRepository.save(job);

        log.info("Cancelled fine-tuning job: {}", jobId);
    }

    public void scheduleRetraining(String baseModel, String cronExpression, String tenantId) {
        log.info("Scheduled retraining for model: {} with cron: {}", baseModel, cronExpression);
        // Implementation would integrate with a scheduler
    }
}
