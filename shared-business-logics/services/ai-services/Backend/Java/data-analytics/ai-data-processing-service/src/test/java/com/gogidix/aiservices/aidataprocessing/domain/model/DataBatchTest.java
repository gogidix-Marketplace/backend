package com.gogidix.aiservices.aidataprocessing.domain.model;

import com.gogidix.aiservices.aidataprocessing.domain.aggregate.DataBatch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Data Batch Domain Model Tests")
class DataBatchTest {

    private static final String VALID_SOURCE = "s3://bucket/data.csv";

    @Nested
    @DisplayName("Batch Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create batch with valid source")
        void shouldCreateWithValidSource() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            assertThat(batch).isNotNull();
            assertThat(batch.getSource()).isEqualTo(VALID_SOURCE);
            assertThat(batch.getFormat()).isEqualTo(DataFormat.CSV);
            assertThat(batch.getStatus()).isEqualTo(ProcessingStatus.PENDING);
            assertThat(batch.getBatchId()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null source")
        void shouldRejectNullSource() {
            assertThatThrownBy(() -> DataBatch.create(null, DataFormat.JSON))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("source cannot be null");
        }

        @Test
        @DisplayName("Should reject empty source")
        void shouldRejectEmptySource() {
            assertThatThrownBy(() -> DataBatch.create("", DataFormat.JSON))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("source cannot be empty");
        }

        @ParameterizedTest
        @EnumSource(DataFormat.class)
        @DisplayName("Should accept all data formats")
        void shouldAcceptAllFormats(DataFormat format) {
            DataBatch batch = DataBatch.create(VALID_SOURCE, format);

            assertThat(batch.getFormat()).isEqualTo(format);
        }
    }

    @Nested
    @DisplayName("Batch Status Tests")
    class StatusTests {

        @Test
        @DisplayName("Should start with PENDING status")
        void shouldStartAsPending() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            assertThat(batch.getStatus()).isEqualTo(ProcessingStatus.PENDING);
        }

        @Test
        @DisplayName("Should transition to PROCESSING")
        void shouldTransitionToProcessing() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            batch.startProcessing();

            assertThat(batch.getStatus()).isEqualTo(ProcessingStatus.PROCESSING);
        }

        @Test
        @DisplayName("Should transition to COMPLETED")
        void shouldTransitionToCompleted() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            batch.startProcessing();

            batch.completeProcessing(1000);

            assertThat(batch.getStatus()).isEqualTo(ProcessingStatus.COMPLETED);
            assertThat(batch.getRecordsProcessed()).isEqualTo(1000);
        }

        @Test
        @DisplayName("Should transition to FAILED")
        void shouldTransitionToFailed() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            batch.startProcessing();

            batch.failProcessing("Validation error");

            assertThat(batch.getStatus()).isEqualTo(ProcessingStatus.FAILED);
            assertThat(batch.getError()).isEqualTo("Validation error");
        }

        @Test
        @DisplayName("Should track progress percentage")
        void shouldTrackProgress() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            batch.startProcessing();

            batch.updateProgress(50, 100);

            assertThat(batch.getProgress()).isEqualTo(50);
        }
    }

    @Nested
    @DisplayName("Transformation Tests")
    class TransformationTests {

        @Test
        @DisplayName("Should add transformation")
        void shouldAddTransformation() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            DataTransformation transformation = DataTransformation.builder()
                    .type(TransformationType.NORMALIZE)
                    .config(Map.of("column", "price"))
                    .build();

            batch.addTransformation(transformation);

            assertThat(batch.getTransformations()).hasSize(1);
        }

        @Test
        @DisplayName("Should reject null transformation")
        void shouldRejectNullTransformation() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            assertThatThrownBy(() -> batch.addTransformation(null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should limit transformations")
        void shouldLimitTransformations() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            for (int i = 0; i < 15; i++) {
                DataTransformation transformation = DataTransformation.builder()
                        .type(TransformationType.FILTER)
                        .config(Map.of("index", i))
                        .build();
                batch.addTransformation(transformation);
            }

            assertThatThrownBy(() -> batch.addTransformation(
                    DataTransformation.builder().type(TransformationType.NORMALIZE).build()))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("maximum");
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should add validation rule")
        void shouldAddValidationRule() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            ValidationRule rule = ValidationRule.builder()
                    .field("email")
                    .type(ValidationType.EMAIL)
                    .build();

            batch.addValidationRule(rule);

            assertThat(batch.getValidationRules()).hasSize(1);
        }

        @Test
        @DisplayName("Should run validation rules")
        void shouldRunValidation() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            ValidationRule rule = ValidationRule.builder()
                    .field("age")
                    .type(ValidationType.RANGE)
                    .config(Map.of("min", 0, "max", 120))
                    .build();
            batch.addValidationRule(rule);

            ValidationResult result = batch.validate();

            assertThat(result).isNotNull();
            assertThat(result.isValid()).isTrue();
        }

        @Test
        @DisplayName("Should detect validation failures")
        void shouldDetectValidationFailures() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            batch.addSampleData(Map.of("age", -5));

            ValidationRule rule = ValidationRule.builder()
                    .field("age")
                    .type(ValidationType.RANGE)
                    .config(Map.of("min", 0, "max", 120))
                    .build();
            batch.addValidationRule(rule);

            ValidationResult result = batch.validate();

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Size Limit Tests")
    class SizeLimitTests {

        @Test
        @DisplayName("Should enforce max records per batch")
        void shouldEnforceMaxRecords() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            for (int i = 0; i < 10_000_001; i++) {
                batch.addRecord(Map.of("id", i));
            }

            assertThat(batch.getRecordCount()).isEqualTo(10_000_000);
        }

        @Test
        @DisplayName("Should track batch size")
        void shouldTrackBatchSize() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            for (int i = 0; i < 100; i++) {
                batch.addRecord(Map.of("id", i, "data", "x".repeat(100)));
            }

            assertThat(batch.getBatchSizeBytes()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Time Tracking Tests")
    class TimeTrackingTests {

        @Test
        @DisplayName("Should track processing time")
        void shouldTrackProcessingTime() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);
            batch.startProcessing();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // ignore
            }

            batch.completeProcessing(100);

            assertThat(batch.getProcessingDurationMs()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should record timestamps")
        void shouldRecordTimestamps() {
            DataBatch batch = DataBatch.create(VALID_SOURCE, DataFormat.CSV);

            assertThat(batch.getCreatedAt()).isNotNull();
            assertThat(batch.getStartedAt()).isNull(); // Not started yet

            batch.startProcessing();
            assertThat(batch.getStartedAt()).isNotNull();

            batch.completeProcessing(0);
            assertThat(batch.getCompletedAt()).isNotNull();
        }
    }
}
