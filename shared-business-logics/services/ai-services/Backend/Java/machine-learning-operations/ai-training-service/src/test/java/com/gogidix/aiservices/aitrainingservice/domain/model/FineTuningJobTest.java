package com.gogidix.aiservices.aitrainingservice.domain.model;

import com.gogidix.aiservices.aitrainingservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FineTuningJob domain model.
 */
@DisplayName("FineTuningJob Domain Model Tests")
class FineTuningJobTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String BASE_MODEL = "gpt-3.5-turbo";
    private static final String DATA_URL = "s3://data/training.jsonl";
    private static final int EPOCHS = 10;
    private static final double LEARNING_RATE = 0.001;

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create fine-tuning job with valid parameters")
        void shouldCreateFineTuningJobWithValidParameters() {
            FineTuningJob job = new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, LEARNING_RATE);

            assertNotNull(job.getId());
            assertNotNull(job.getJobId());
            assertEquals(TENANT_ID, job.getTenantId());
            assertEquals(BASE_MODEL, job.getBaseModel());
            assertEquals(DATA_URL, job.getTrainingDataUrl());
            assertEquals(EPOCHS, job.getEpochs());
            assertEquals(LEARNING_RATE, job.getLearningRate());
            assertEquals(FineTuningStatus.PENDING, job.getStatus());
            assertNotNull(job.getCreatedAt());
        }

        @Test
        @DisplayName("Should throw exception when epochs is out of range")
        void shouldThrowWhenEpochsOutOfRange() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, 0, LEARNING_RATE));
        }

        @Test
        @DisplayName("Should throw exception when learning rate is too low")
        void shouldThrowWhenLearningRateTooLow() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, 0.00001));
        }
    }

    @Nested
    @DisplayName("Status Tests")
    class StatusTests {

        @Test
        @DisplayName("Should start fine-tuning job")
        void shouldStartFineTuningJob() {
            FineTuningJob job = new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, LEARNING_RATE);

            job.start();

            assertEquals(FineTuningStatus.RUNNING, job.getStatus());
        }

        @Test
        @DisplayName("Should complete fine-tuning job")
        void shouldCompleteFineTuningJob() {
            FineTuningJob job = new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, LEARNING_RATE);
            job.start();

            FineTuningJob.TrainingMetrics metrics = new FineTuningJob.TrainingMetrics(0.15, 0.92, 10);
            job.complete("s3://models/fine-tuned.pkl", metrics);

            assertEquals(FineTuningStatus.COMPLETED, job.getStatus());
            assertEquals("s3://models/fine-tuned.pkl", job.getFineTunedModelUrl());
            assertNotNull(job.getCompletedAt());
        }

        @Test
        @DisplayName("Should fail fine-tuning job")
        void shouldFailFineTuningJob() {
            FineTuningJob job = new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, LEARNING_RATE);
            job.start();

            job.fail("Training error");

            assertEquals(FineTuningStatus.FAILED, job.getStatus());
            assertEquals("Training error", job.getErrorMessage());
        }

        @Test
        @DisplayName("Should cancel fine-tuning job")
        void shouldCancelFineTuningJob() {
            FineTuningJob job = new FineTuningJob(TENANT_ID, BASE_MODEL, DATA_URL, EPOCHS, LEARNING_RATE);

            job.cancel();

            assertEquals(FineTuningStatus.CANCELLED, job.getStatus());
        }
    }
}
