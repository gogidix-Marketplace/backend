package com.gogidix.aiservices.aimodeltrainingservice.application.dto;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TrainingJobResponseDto Tests")
class TrainingJobResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @ParameterizedTest
        @EnumSource(TrainingStatus.class)
        @DisplayName("Should create with all TrainingStatus values")
        void shouldCreateWithAllStatusValues(TrainingStatus status) {
            Instant now = Instant.now();
            Map<String, Double> metrics = Map.of("accuracy", 0.95, "loss", 0.05);

            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    status,
                    "s3://models/model.pkl",
                    metrics,
                    now.minusSeconds(3600),
                    now.plusSeconds(1800)
            );

            assertThat(dto.trainingJobId()).isEqualTo("job-123");
            assertThat(dto.modelType()).isEqualTo("classifier");
            assertThat(dto.status()).isEqualTo(status);
            assertThat(dto.metrics()).hasSize(2);
        }

        @Test
        @DisplayName("Should create with QUEUED status")
        void shouldCreateWithQueuedStatus() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(dto.status()).isEqualTo(TrainingStatus.QUEUED);
        }

        @Test
        @DisplayName("Should create with RUNNING status")
        void shouldCreateWithRunningStatus() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "regressor",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    Instant.now().minusSeconds(300),
                    Instant.now().plusSeconds(2700)
            );

            assertThat(dto.status()).isEqualTo(TrainingStatus.RUNNING);
        }

        @Test
        @DisplayName("Should create with COMPLETED status")
        void shouldCreateWithCompletedStatus() {
            Map<String, Double> metrics = Map.of("accuracy", 0.92, "f1_score", 0.91);
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.COMPLETED,
                    "s3://models/model.pkl",
                    metrics,
                    Instant.now().minusSeconds(3600),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(TrainingStatus.COMPLETED);
            assertThat(dto.modelArtifactUrl()).isNotNull();
            assertThat(dto.metrics()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Metrics Tests")
    class MetricsTests {

        @Test
        @DisplayName("Should store accuracy metric")
        void shouldStoreAccuracyMetric() {
            Map<String, Double> metrics = Map.of("accuracy", 0.95);

            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.COMPLETED,
                    "s3://models/model.pkl",
                    metrics,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metrics().get("accuracy")).isEqualTo(0.95);
        }

        @Test
        @DisplayName("Should store multiple metrics")
        void shouldStoreMultipleMetrics() {
            Map<String, Double> metrics = Map.of(
                    "accuracy", 0.95,
                    "precision", 0.93,
                    "recall", 0.91,
                    "f1_score", 0.92
            );

            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.COMPLETED,
                    "s3://models/model.pkl",
                    metrics,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metrics()).hasSize(4);
        }

        @Test
        @DisplayName("Should handle null metrics")
        void shouldHandleNullMetrics() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metrics()).isNull();
        }

        @Test
        @DisplayName("Should handle empty metrics")
        void shouldHandleEmptyMetrics() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.FAILED,
                    null,
                    Map.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metrics()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Model Artifact URL Tests")
    class ModelArtifactUrlTests {

        @Test
        @DisplayName("Should store S3 artifact URL")
        void shouldStoreS3ArtifactUrl() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.COMPLETED,
                    "s3://models/classifier-v1.pkl",
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelArtifactUrl()).startsWith("s3://");
        }

        @Test
        @DisplayName("Should handle null artifact URL for non-completed jobs")
        void shouldHandleNullArtifactUrl() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(dto.modelArtifactUrl()).isNull();
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should store startedAt timestamp")
        void shouldStoreStartedAtTimestamp() {
            Instant started = Instant.now().minusSeconds(1800);
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    started,
                    Instant.now().plusSeconds(1800)
            );

            assertThat(dto.startedAt()).isEqualTo(started);
        }

        @Test
        @DisplayName("Should store estimatedCompletion timestamp")
        void shouldStoreEstimatedCompletionTimestamp() {
            Instant completion = Instant.now().plusSeconds(3600);
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    Instant.now(),
                    completion
            );

            assertThat(dto.estimatedCompletion()).isEqualTo(completion);
        }

        @Test
        @DisplayName("Should represent 1 hour training duration estimate")
        void shouldRepresent1HourTrainingDuration() {
            Instant started = Instant.now().minusSeconds(1800);
            Instant estimated = Instant.now().plusSeconds(1800);

            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "classifier",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    started,
                    estimated
            );

            assertThat(dto.startedAt()).isBefore(Instant.now());
            assertThat(dto.estimatedCompletion()).isAfter(Instant.now());
        }
    }

    @Nested
    @DisplayName("ID Tests")
    class IdTests {

        @Test
        @DisplayName("Should store training job ID")
        void shouldStoreTrainingJobId() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "training-job-abc-123",
                    "classifier",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.trainingJobId()).isEqualTo("training-job-abc-123");
        }

        @Test
        @DisplayName("Should store model type")
        void shouldStoreModelType() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "sentiment_classifier_v2",
                    TrainingStatus.COMPLETED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelType()).isEqualTo("sentiment_classifier_v2");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Instant now = Instant.now();
            Map<String, Double> metrics = Map.of("acc", 0.9);

            TrainingJobResponseDto dto1 = new TrainingJobResponseDto(
                    "job-1", "model", TrainingStatus.COMPLETED,
                    "s3://model.pkl", metrics, now, now
            );
            TrainingJobResponseDto dto2 = new TrainingJobResponseDto(
                    "job-1", "model", TrainingStatus.COMPLETED,
                    "s3://model.pkl", metrics, now, now
            );

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when job IDs differ")
        void shouldNotBeEqualWhenJobIdsDiffer() {
            Instant now = Instant.now();

            TrainingJobResponseDto dto1 = new TrainingJobResponseDto(
                    "job-1", "model", TrainingStatus.QUEUED, null, null, now, now
            );
            TrainingJobResponseDto dto2 = new TrainingJobResponseDto(
                    "job-2", "model", TrainingStatus.QUEUED, null, null, now, now
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when status differs")
        void shouldNotBeEqualWhenStatusDiffers() {
            Instant now = Instant.now();

            TrainingJobResponseDto dto1 = new TrainingJobResponseDto(
                    "job-1", "model", TrainingStatus.RUNNING, null, null, now, now
            );
            TrainingJobResponseDto dto2 = new TrainingJobResponseDto(
                    "job-1", "model", TrainingStatus.COMPLETED, null, null, now, now
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in model type")
        void shouldHandleUnicodeInModelType() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "分類器",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelType()).isEqualTo("分類器");
        }

        @Test
        @DisplayName("Should handle very long job ID")
        void shouldHandleVeryLongJobId() {
            String longId = "job-" + "a".repeat(200);
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    longId,
                    "model",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.trainingJobId()).hasSizeGreaterThan(200);
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access trainingJobId component")
        void shouldAccessTrainingJobIdComponent() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-xyz",
                    "model",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.trainingJobId()).isEqualTo("job-xyz");
        }

        @Test
        @DisplayName("Should access modelType component")
        void shouldAccessModelTypeComponent() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "my-model",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.modelType()).isEqualTo("my-model");
        }

        @Test
        @DisplayName("Should access status component")
        void shouldAccessStatusComponent() {
            TrainingJobResponseDto dto = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.COMPLETED,
                    null,
                    Map.of("acc", 0.9),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(TrainingStatus.COMPLETED);
        }
    }
}
