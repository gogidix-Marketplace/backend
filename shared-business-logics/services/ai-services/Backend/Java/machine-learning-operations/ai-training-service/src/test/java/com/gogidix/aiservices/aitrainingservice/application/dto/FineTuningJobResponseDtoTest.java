package com.gogidix.aiservices.aitrainingservice.application.dto;

import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FineTuningJobResponseDto Tests")
class FineTuningJobResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @ParameterizedTest
        @EnumSource(FineTuningStatus.class)
        @DisplayName("Should create with all FineTuningStatus values")
        void shouldCreateWithAllStatusValues(FineTuningStatus status) {
            Instant now = Instant.now();
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.15, 0.92, 5);

            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    status,
                    "s3://models/fine-tuned.pkl",
                    metrics,
                    now.minusSeconds(3600),
                    now.plusSeconds(1800)
            );

            assertThat(dto.fineTuningJobId()).isEqualTo("job-123");
            assertThat(dto.baseModel()).isEqualTo("gpt-3.5-turbo");
            assertThat(dto.status()).isEqualTo(status);
        }

        @Test
        @DisplayName("Should create with PENDING status")
        void shouldCreateWithPendingStatus() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(dto.status()).isEqualTo(FineTuningStatus.PENDING);
        }

        @Test
        @DisplayName("Should create with RUNNING status")
        void shouldCreateWithRunningStatus() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "bert-base",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    Instant.now().minusSeconds(300),
                    Instant.now().plusSeconds(3300)
            );

            assertThat(dto.status()).isEqualTo(FineTuningStatus.RUNNING);
        }

        @Test
        @DisplayName("Should create with COMPLETED status and metrics")
        void shouldCreateWithCompletedStatusAndMetrics() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.05, 0.95, 10);

            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    FineTuningStatus.COMPLETED,
                    "s3://models/fine-tuned.pkl",
                    metrics,
                    Instant.now().minusSeconds(3600),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(FineTuningStatus.COMPLETED);
            assertThat(dto.fineTunedModelUrl()).isNotNull();
            assertThat(dto.metrics()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Metrics Tests")
    class MetricsTests {

        @Test
        @DisplayName("Should create metrics with all fields")
        void shouldCreateMetricsWithAllFields() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.15, 0.92, 5);

            assertThat(metrics.loss()).isEqualTo(0.15);
            assertThat(metrics.accuracy()).isEqualTo(0.92);
            assertThat(metrics.epochsCompleted()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle zero loss")
        void shouldHandleZeroLoss() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.0, 1.0, 100);

            assertThat(metrics.loss()).isZero();
        }

        @Test
        @DisplayName("Should handle high accuracy")
        void shouldHandleHighAccuracy() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.01, 0.99, 50);

            assertThat(metrics.accuracy()).isEqualTo(0.99);
        }

        @Test
        @DisplayName("Should handle partial epoch completion")
        void shouldHandlePartialEpochCompletion() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.5, 0.7, 3);

            assertThat(metrics.epochsCompleted()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Model Artifact URL Tests")
    class ModelArtifactUrlTests {

        @Test
        @DisplayName("Should store S3 artifact URL")
        void shouldStoreS3ArtifactUrl() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    FineTuningStatus.COMPLETED,
                    "s3://models/fine-tuned-gpt.pkl",
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.fineTunedModelUrl()).startsWith("s3://");
        }

        @Test
        @DisplayName("Should handle null artifact URL for non-completed jobs")
        void shouldHandleNullArtifactUrl() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            assertThat(dto.fineTunedModelUrl()).isNull();
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should store createdAt timestamp")
        void shouldStoreCreatedAtTimestamp() {
            Instant created = Instant.now().minusSeconds(1800);
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    created,
                    Instant.now().plusSeconds(1800)
            );

            assertThat(dto.createdAt()).isEqualTo(created);
        }

        @Test
        @DisplayName("Should store estimatedCompletion timestamp")
        void shouldStoreEstimatedCompletionTimestamp() {
            Instant completion = Instant.now().plusSeconds(3600);
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "bert-base",
                    FineTuningStatus.RUNNING,
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
            Instant created = Instant.now().minusSeconds(1800);
            Instant estimated = Instant.now().plusSeconds(1800);

            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    created,
                    estimated
            );

            assertThat(dto.createdAt()).isBefore(Instant.now());
            assertThat(dto.estimatedCompletion()).isAfter(Instant.now());
        }
    }

    @Nested
    @DisplayName("ID Tests")
    class IdTests {

        @Test
        @DisplayName("Should store fine-tuning job ID")
        void shouldStoreFineTuningJobId() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "fine-tune-job-abc-123",
                    "gpt-4",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.fineTuningJobId()).isEqualTo("fine-tune-job-abc-123");
        }

        @Test
        @DisplayName("Should store base model name")
        void shouldStoreBaseModelName() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "bert-base-uncased",
                    FineTuningStatus.COMPLETED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.baseModel()).isEqualTo("bert-base-uncased");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Instant now = Instant.now();
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.1, 0.9, 5);

            FineTuningJobResponseDto dto1 = new FineTuningJobResponseDto(
                    "job-1", "model", FineTuningStatus.COMPLETED,
                    "s3://model.pkl", metrics, now, now
            );
            FineTuningJobResponseDto dto2 = new FineTuningJobResponseDto(
                    "job-1", "model", FineTuningStatus.COMPLETED,
                    "s3://model.pkl", metrics, now, now
            );

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when job IDs differ")
        void shouldNotBeEqualWhenJobIdsDiffer() {
            Instant now = Instant.now();

            FineTuningJobResponseDto dto1 = new FineTuningJobResponseDto(
                    "job-1", "model", FineTuningStatus.PENDING, null, null, now, now
            );
            FineTuningJobResponseDto dto2 = new FineTuningJobResponseDto(
                    "job-2", "model", FineTuningStatus.PENDING, null, null, now, now
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when status differs")
        void shouldNotBeEqualWhenStatusDiffers() {
            Instant now = Instant.now();

            FineTuningJobResponseDto dto1 = new FineTuningJobResponseDto(
                    "job-1", "model", FineTuningStatus.RUNNING, null, null, now, now
            );
            FineTuningJobResponseDto dto2 = new FineTuningJobResponseDto(
                    "job-1", "model", FineTuningStatus.COMPLETED, null, null, now, now
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }
    }

    @Nested
    @DisplayName("Metrics Record Tests")
    class MetricsRecordTests {

        @Test
        @DisplayName("Should be equal when all metric fields match")
        void shouldBeEqualWhenAllMetricFieldsMatch() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics1 =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.1, 0.9, 5);
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics2 =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.1, 0.9, 5);

            assertThat(metrics1).isEqualTo(metrics2);
            assertThat(metrics1.hashCode()).isEqualTo(metrics2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when loss differs")
        void shouldNotBeEqualWhenLossDiffers() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics1 =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.1, 0.9, 5);
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics2 =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.2, 0.9, 5);

            assertThat(metrics1).isNotEqualTo(metrics2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in base model name")
        void shouldHandleUnicodeInBaseModelName() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "モデル-3.5",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.baseModel()).isEqualTo("モデル-3.5");
        }

        @Test
        @DisplayName("Should handle very long job ID")
        void shouldHandleVeryLongJobId() {
            String longId = "job-" + "a".repeat(200);
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    longId,
                    "gpt-4",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.fineTuningJobId()).hasSizeGreaterThan(200);
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access fineTuningJobId component")
        void shouldAccessFineTuningJobIdComponent() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-xyz",
                    "model",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.fineTuningJobId()).isEqualTo("job-xyz");
        }

        @Test
        @DisplayName("Should access baseModel component")
        void shouldAccessBaseModelComponent() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "my-model",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.baseModel()).isEqualTo("my-model");
        }

        @Test
        @DisplayName("Should access status component")
        void shouldAccessStatusComponent() {
            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.COMPLETED,
                    null,
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.1, 0.9, 10),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.status()).isEqualTo(FineTuningStatus.COMPLETED);
        }

        @Test
        @DisplayName("Should access metrics component")
        void shouldAccessMetricsComponent() {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.05, 0.95, 10);

            FineTuningJobResponseDto dto = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    FineTuningStatus.COMPLETED,
                    null,
                    metrics,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metrics()).isEqualTo(metrics);
        }
    }
}
