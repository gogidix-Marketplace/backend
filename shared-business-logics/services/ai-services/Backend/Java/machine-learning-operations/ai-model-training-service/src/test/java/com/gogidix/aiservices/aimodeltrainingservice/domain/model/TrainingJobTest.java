package com.gogidix.aiservices.aimodeltrainingservice.domain.model;

import com.gogidix.aiservices.aimodeltrainingservice.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for TrainingJob domain model.
 */
@DisplayName("TrainingJob Domain Model Tests")
class TrainingJobTest {

    private static final String TENANT_ID = "tenant-123";
    private static final String MODEL_TYPE = "recommendation-model";
    private static final String DATA_URL = "s3://data/training.csv";
    private static final TrainingAlgorithm ALGORITHM = TrainingAlgorithm.RANDOM_FOREST;

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create training job with valid parameters")
        void shouldCreateTrainingJobWithValidParameters() {
            TrainingJob job = new TrainingJob(TENANT_ID, MODEL_TYPE, DATA_URL, ALGORITHM, Map.of());

            assertNotNull(job.getId());
            assertNotNull(job.getJobId());
            assertEquals(TENANT_ID, job.getTenantId());
            assertEquals(MODEL_TYPE, job.getModelType());
            assertEquals(DATA_URL, job.getTrainingDataUrl());
            assertEquals(ALGORITHM, job.getAlgorithm());
            assertEquals(TrainingStatus.QUEUED, job.getStatus());
            assertNotNull(job.getStartedAt());
        }
    }

    @Nested
    @DisplayName("Status Tests")
    class StatusTests {

        @Test
        @DisplayName("Should start training job")
        void shouldStartTrainingJob() {
            TrainingJob job = new TrainingJob(TENANT_ID, MODEL_TYPE, DATA_URL, ALGORITHM, Map.of());

            job.start();

            assertEquals(TrainingStatus.RUNNING, job.getStatus());
        }

        @Test
        @DisplayName("Should complete training job")
        void shouldCompleteTrainingJob() {
            TrainingJob job = new TrainingJob(TENANT_ID, MODEL_TYPE, DATA_URL, ALGORITHM, Map.of());
            job.start();

            job.complete("s3://models/model.pkl", Map.of("accuracy", 0.85));

            assertEquals(TrainingStatus.COMPLETED, job.getStatus());
            assertEquals("s3://models/model.pkl", job.getModelArtifactUrl());
            assertNotNull(job.getCompletedAt());
        }

        @Test
        @DisplayName("Should fail training job")
        void shouldFailTrainingJob() {
            TrainingJob job = new TrainingJob(TENANT_ID, MODEL_TYPE, DATA_URL, ALGORITHM, Map.of());
            job.start();

            job.fail("Training error");

            assertEquals(TrainingStatus.FAILED, job.getStatus());
            assertEquals("Training error", job.getErrorMessage());
        }

        @Test
        @DisplayName("Should cancel training job")
        void shouldCancelTrainingJob() {
            TrainingJob job = new TrainingJob(TENANT_ID, MODEL_TYPE, DATA_URL, ALGORITHM, Map.of());

            job.cancel();

            assertEquals(TrainingStatus.CANCELLED, job.getStatus());
        }
    }
}
