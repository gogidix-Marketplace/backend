package com.gogidix.transaction.progress.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.progress.domain.entity.ProgressStep;
import com.gogidix.transaction.progress.domain.repository.ProgressStepRepository;
import com.gogidix.transaction.progress.dto.*;
import com.gogidix.transaction.progress.exception.StepNotFoundException;
import com.gogidix.transaction.progress.mapper.ProgressStepMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressStepService {

    private final ProgressStepRepository stepRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public ProgressStepResponse createStep(ProgressStepCreateRequest request) {
        log.info("Creating progress step: {} for transaction: {}", request.getStepName(), request.getTransactionId());

        ProgressStep step = ProgressStepMapper.toEntity(request);
        ProgressStep saved = stepRepository.save(step);

        publishStepEvent("STEP_CREATED", saved);

        return ProgressStepMapper.toResponse(saved);
    }

    @Transactional
    public List<ProgressStepResponse> createSteps(UUID transactionId, List<ProgressStepCreateRequest> requests) {
        log.info("Creating {} steps for transaction: {}", requests.size(), transactionId);

        List<ProgressStep> steps = requests.stream()
            .map(ProgressStepMapper::toEntity)
            .collect(Collectors.toList());

        List<ProgressStep> saved = stepRepository.saveAll(steps);

        saved.forEach(step -> publishStepEvent("STEP_CREATED", step));

        return saved.stream()
            .map(ProgressStepMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public ProgressStepResponse startStep(UUID stepId) {
        log.info("Starting step: {}", stepId);

        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));

        if (step.getStatus() != ProgressStep.StepStatus.PENDING &&
            step.getStatus() != ProgressStep.StepStatus.RETRYING) {
            throw new IllegalStateException("Step cannot be started from status: " + step.getStatus());
        }

        step.setStatus(ProgressStep.StepStatus.IN_PROGRESS);
        step.setStartedAt(LocalDateTime.now());

        ProgressStep saved = stepRepository.save(step);
        publishStepEvent("STEP_STARTED", saved);

        return ProgressStepMapper.toResponse(saved);
    }

    @Transactional
    public ProgressStepResponse completeStep(UUID stepId, Map<String, Object> outputData) {
        log.info("Completing step: {}", stepId);

        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));

        if (step.getStatus() != ProgressStep.StepStatus.IN_PROGRESS) {
            throw new IllegalStateException("Step is not in progress");
        }

        step.setStatus(ProgressStep.StepStatus.COMPLETED);
        step.setCompletedAt(LocalDateTime.now());

        if (step.getStartedAt() != null) {
            step.setDurationMilliseconds(
                java.time.Duration.between(step.getStartedAt(), step.getCompletedAt()).toMillis()
            );
        }

        if (outputData != null && !outputData.isEmpty()) {
            try {
                step.setOutputData(objectMapper.writeValueAsString(outputData));
            } catch (Exception e) {
                log.warn("Failed to serialize output data", e);
            }
        }

        ProgressStep saved = stepRepository.save(step);
        publishStepEvent("STEP_COMPLETED", saved);

        // Trigger next steps if any
        triggerNextSteps(saved);

        return ProgressStepMapper.toResponse(saved);
    }

    @Transactional
    public ProgressStepResponse failStep(UUID stepId, String errorMessage) {
        log.info("Failing step: {} - {}", stepId, errorMessage);

        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));

        step.setStatus(ProgressStep.StepStatus.FAILED);
        step.setErrorMessage(errorMessage);
        step.setCompletedAt(LocalDateTime.now());

        if (step.getStartedAt() != null) {
            step.setDurationMilliseconds(
                java.time.Duration.between(step.getStartedAt(), step.getCompletedAt()).toMillis()
            );
        }

        ProgressStep saved = stepRepository.save(step);
        publishStepEvent("STEP_FAILED", saved);

        // Check if retry is possible
        if (step.getRetryCount() < step.getMaxRetries()) {
            scheduleRetry(saved);
        } else {
            // Execute compensation if configured
            executeCompensation(saved);
        }

        return ProgressStepMapper.toResponse(saved);
    }

    @Transactional
    public ProgressStepResponse skipStep(UUID stepId, String reason) {
        log.info("Skipping step: {} - {}", stepId, reason);

        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));

        step.setStatus(ProgressStep.StepStatus.SKIPPED);
        step.setErrorMessage(reason);
        step.setCompletedAt(LocalDateTime.now());

        ProgressStep saved = stepRepository.save(step);
        publishStepEvent("STEP_SKIPPED", saved);

        triggerNextSteps(saved);

        return ProgressStepMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProgressStepResponse> getStepsByTransactionId(UUID transactionId) {
        List<ProgressStep> steps = stepRepository.findByTransactionIdOrderByStepOrderAsc(transactionId);
        return steps.stream()
            .map(ProgressStepMapper::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProgressStepResponse getStepById(UUID stepId) {
        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));
        return ProgressStepMapper.toResponse(step);
    }

    @Transactional(readOnly = true)
    public StepExecutionSummary getExecutionSummary(UUID transactionId) {
        List<ProgressStep> steps = stepRepository.findByTransactionIdOrderByStepOrderAsc(transactionId);

        long totalSteps = steps.size();
        long completed = steps.stream().filter(s -> s.getStatus() == ProgressStep.StepStatus.COMPLETED).count();
        long failed = steps.stream().filter(s -> s.getStatus() == ProgressStep.StepStatus.FAILED).count();
        long inProgress = steps.stream().filter(s -> s.getStatus() == ProgressStep.StepStatus.IN_PROGRESS).count();
        long pending = steps.stream().filter(s -> s.getStatus() == ProgressStep.StepStatus.PENDING).count();
        long skipped = steps.stream().filter(s -> s.getStatus() == ProgressStep.StepStatus.SKIPPED).count();

        int progressPercentage = totalSteps > 0 ? (int) ((completed * 100) / totalSteps) : 0;

        return StepExecutionSummary.builder()
            .transactionId(transactionId)
            .totalSteps(totalSteps)
            .completedSteps(completed)
            .failedSteps(failed)
            .inProgressSteps(inProgress)
            .pendingSteps(pending)
            .skippedSteps(skipped)
            .progressPercentage(progressPercentage)
            .isComplete(completed + failed + skipped == totalSteps)
            .build();
    }

    @Transactional
    public void executeCompensation(UUID stepId) {
        log.info("Executing compensation for step: {}", stepId);

        ProgressStep step = stepRepository.findById(stepId)
            .orElseThrow(() -> new StepNotFoundException("Step not found: " + stepId));

        if (step.getCompensationAction() != null && !step.getCompensationAction().isBlank()) {
            // Kafka removed for local development - compensation would be triggered here
            log.info("Compensation triggered for step: {}, action: {}", stepId, step.getCompensationAction());
        }
    }

    private void scheduleRetry(ProgressStep step) {
        log.info("Scheduling retry for step: {}, attempt {}", step.getId(), step.getRetryCount() + 1);

        CompletableFuture.delayedExecutor(step.getRetryDelaySeconds(), java.util.concurrent.TimeUnit.SECONDS)
            .execute(() -> {
                try {
                    step.setRetryCount(step.getRetryCount() + 1);
                    step.setStatus(ProgressStep.StepStatus.RETRYING);
                    step.setErrorMessage(null);
                    stepRepository.save(step);

                    publishStepEvent("STEP_RETRYING", step);
                    log.info("Retrying step: {}, attempt {}", step.getId(), step.getRetryCount());
                } catch (Exception e) {
                    log.error("Failed to schedule retry for step: {}", step.getId(), e);
                }
            });
    }

    private void triggerNextSteps(ProgressStep completedStep) {
        log.info("Triggering next steps after: {}", completedStep.getId());

        // Find steps that depend on this step
        List<ProgressStep> allSteps = stepRepository.findByTransactionIdOrderByStepOrderAsc(
            completedStep.getTransactionId());

        allSteps.stream()
            .filter(step -> step.getStatus() == ProgressStep.StepStatus.PENDING)
            .filter(step -> canExecute(step, allSteps))
            .forEach(step -> {
                try {
                    publishStepEvent("STEP_READY", step);
                    log.info("Step {} is ready to execute", step.getId());
                } catch (Exception e) {
                    log.error("Failed to trigger next step: {}", step.getId(), e);
                }
            });
    }

    private boolean canExecute(ProgressStep step, List<ProgressStep> allSteps) {
        // Check if step can execute in parallel or if dependencies are met
        if (step.getCanExecuteParallel()) {
            return true;
        }

        // Check dependencies
        if (step.getDependsOn() != null && !step.getDependsOn().isBlank()) {
            try {
                String[] dependencies = step.getDependsOn().split(",");
                for (String dep : dependencies) {
                    int depOrder = Integer.parseInt(dep.trim());
                    boolean depCompleted = allSteps.stream()
                        .filter(s -> s.getStepOrder() == depOrder)
                        .anyMatch(s -> s.getStatus() == ProgressStep.StepStatus.COMPLETED ||
                                      s.getStatus() == ProgressStep.StepStatus.SKIPPED);
                    if (!depCompleted) {
                        return false;
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to parse dependencies for step: {}", step.getId(), e);
                return false;
            }
        }

        return true;
    }

    private void executeCompensation(ProgressStep step) {
        executeCompensation(step.getId());
    }

    @Scheduled(fixedDelay = 60000)
    public void checkTimeoutSteps() {
        log.debug("Checking for timed out steps");

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(5);
        List<ProgressStep> timedOutSteps = stepRepository.findByStatusAndStartedAtBefore(
            ProgressStep.StepStatus.IN_PROGRESS, threshold);

        timedOutSteps.forEach(step -> {
            if (step.getExecutionTimeoutSeconds() != null) {
                LocalDateTime timeout = step.getStartedAt().plusSeconds(step.getExecutionTimeoutSeconds());
                if (LocalDateTime.now().isAfter(timeout)) {
                    log.warn("Step {} has timed out", step.getId());
                    failStep(step.getId(), "Step execution timeout");
                }
            }
        });
    }

    private void publishStepEvent(String eventType, ProgressStep step) {
        // Kafka removed for local development - just logging the event
        log.debug("Step event: {} for step: {}, transaction: {}, status: {}",
            eventType, step.getId(), step.getTransactionId(), step.getStatus());
    }
}
