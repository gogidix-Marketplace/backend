package com.gogidix.aiservices.aimodeltrainingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Model Training Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ModelTrainingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter trainingTotalCounter;
    private final Counter trainingSuccessCounter;
    private final Counter trainingFailureCounter;
    private final Counter modelsTrainedCounter;
    private final Counter trainingJobsCompletedCounter;
    private final Counter trainingJobsFailedCounter;
    private final Counter hyperparameterTuningCounter;

    // Timers
    private final Timer trainingTimer;
    private final Timer modelEvaluationTimer;
    private final Timer modelSaveTimer;

    public ModelTrainingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.trainingTotalCounter = Counter.builder("ai.model.training.total")
                .description("Total number of model training requests")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.trainingSuccessCounter = Counter.builder("ai.model.training.success")
                .description("Number of successful model training requests")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.trainingFailureCounter = Counter.builder("ai.model.training.failure")
                .description("Number of failed model training requests")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.modelsTrainedCounter = Counter.builder("ai.model.trained")
                .description("Number of models trained")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.trainingJobsCompletedCounter = Counter.builder("ai.model.training.jobs.completed")
                .description("Number of training jobs completed")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.trainingJobsFailedCounter = Counter.builder("ai.model.training.jobs.failed")
                .description("Number of training jobs failed")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        this.hyperparameterTuningCounter = Counter.builder("ai.model.training.hyperparameter.tuning")
                .description("Number of hyperparameter tuning operations")
                .tag("service", "ai-model-training")
                .register(meterRegistry);

        // Initialize timers
        this.trainingTimer = Timer.builder("ai.model.training.duration")
                .description("Model training processing time")
                .tag("service", "ai-model-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.modelEvaluationTimer = Timer.builder("ai.model.training.evaluation.duration")
                .description("Model evaluation time")
                .tag("service", "ai-model-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.modelSaveTimer = Timer.builder("ai.model.training.save.duration")
                .description("Model save operation time")
                .tag("service", "ai-model-training")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementTrainingTotal() {
        trainingTotalCounter.increment();
    }

    public void incrementTrainingSuccess() {
        trainingSuccessCounter.increment();
    }

    public void incrementTrainingFailure() {
        trainingFailureCounter.increment();
    }

    public void incrementModelsTrained() {
        modelsTrainedCounter.increment();
    }

    public void incrementTrainingJobsCompleted() {
        trainingJobsCompletedCounter.increment();
    }

    public void incrementTrainingJobsFailed() {
        trainingJobsFailedCounter.increment();
    }

    public void incrementHyperparameterTuning() {
        hyperparameterTuningCounter.increment();
    }

    // Timer methods
    public void recordTrainingTime(long durationMs) {
        trainingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startTrainingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTrainingTimer(Timer.Sample sample) {
        sample.stop(trainingTimer);
    }

    public Timer.Sample startModelEvaluationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelEvaluationTimer(Timer.Sample sample) {
        sample.stop(modelEvaluationTimer);
    }

    public void recordModelSaveTime(long durationMs) {
        modelSaveTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startModelSaveTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopModelSaveTimer(Timer.Sample sample) {
        sample.stop(modelSaveTimer);
    }

    // SLO compliance methods
    public double getTrainingLatencyP95() {
        return trainingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getTrainingLatencyP99() {
        return trainingTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) trainingTotalCounter.count();
        long failures = (long) trainingFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
