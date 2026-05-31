package com.gogidix.aiservices.aitrainingservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FineTuneRequestDto Tests")
class FineTuneRequestDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request with all fields")
        void shouldCreateWithAllFields() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo",
                    "s3://data/training.jsonl",
                    10,
                    0.001
            );

            assertThat(request.baseModel()).isEqualTo("gpt-3.5-turbo");
            assertThat(request.trainingDataUrl()).isEqualTo("s3://data/training.jsonl");
            assertThat(request.epochs()).isEqualTo(10);
            assertThat(request.learningRate()).isEqualTo(0.001);
        }

        @Test
        @DisplayName("Should create with minimum epochs")
        void shouldCreateWithMinimumEpochs() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "s3://data/data.jsonl",
                    1,
                    0.0001
            );

            assertThat(request.epochs()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should create with maximum epochs")
        void shouldCreateWithMaximumEpochs() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "bert-base",
                    "s3://data/data.jsonl",
                    1000,
                    0.0001
            );

            assertThat(request.epochs()).isEqualTo(1000);
        }
    }

    @Nested
    @DisplayName("Base Model Tests")
    class BaseModelTests {

        @ParameterizedTest
        @ValueSource(strings = {"gpt-3.5-turbo", "gpt-4", "bert-base-uncased", "llama-2-7b"})
        @DisplayName("Should accept various base model names")
        void shouldAcceptVariousBaseModelNames(String modelName) {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    modelName,
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.baseModel()).isEqualTo(modelName);
        }

        @Test
        @DisplayName("Should accept model with version")
        void shouldAcceptModelWithVersion() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo-0613",
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.baseModel()).contains("-");
        }
    }

    @Nested
    @DisplayName("Training Data URL Tests")
    class TrainingDataUrlTests {

        @Test
        @DisplayName("Should accept S3 URL")
        void shouldAcceptS3Url() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo",
                    "s3://bucket/data/training.jsonl",
                    5,
                    0.001
            );

            assertThat(request.trainingDataUrl()).startsWith("s3://");
        }

        @Test
        @DisplayName("Should accept GCS URL")
        void shouldAcceptGcsUrl() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "gs://bucket/data.jsonl",
                    5,
                0.001
            );

            assertThat(request.trainingDataUrl()).startsWith("gs://");
        }

        @Test
        @DisplayName("Should accept HTTPS URL")
        void shouldAcceptHttpsUrl() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "bert-base",
                    "https://example.com/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.trainingDataUrl()).startsWith("https://");
        }

        @Test
        @DisplayName("Should accept file path URL")
        void shouldAcceptFilePathUrl() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "llama-2",
                    "file:///data/training.jsonl",
                    5,
                    0.001
            );

            assertThat(request.trainingDataUrl()).startsWith("file://");
        }
    }

    @Nested
    @DisplayName("Epochs Tests")
    class EpochsTests {

        @Test
        @DisplayName("Should accept low epoch count")
        void shouldAcceptLowEpochCount() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo",
                    "s3://data/data.jsonl",
                    3,
                    0.001
            );

            assertThat(request.epochs()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should accept medium epoch count")
        void shouldAcceptMediumEpochCount() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "s3://data/data.jsonl",
                    50,
                    0.001
            );

            assertThat(request.epochs()).isEqualTo(50);
        }

        @Test
        @DisplayName("Should accept high epoch count")
        void shouldAcceptHighEpochCount() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "bert-base",
                    "s3://data/data.jsonl",
                    500,
                    0.001
            );

            assertThat(request.epochs()).isEqualTo(500);
        }
    }

    @Nested
    @DisplayName("Learning Rate Tests")
    class LearningRateTests {

        @Test
        @DisplayName("Should accept minimum learning rate")
        void shouldAcceptMinimumLearningRate() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo",
                    "s3://data/data.jsonl",
                    5,
                    0.0001
            );

            assertThat(request.learningRate()).isEqualTo(0.0001);
        }

        @Test
        @DisplayName("Should accept typical learning rate")
        void shouldAcceptTypicalLearningRate() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.learningRate()).isEqualTo(0.001);
        }

        @Test
        @DisplayName("Should accept high learning rate")
        void shouldAcceptHighLearningRate() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "bert-base",
                    "s3://data/data.jsonl",
                    5,
                    0.01
            );

            assertThat(request.learningRate()).isEqualTo(0.01);
        }

        @Test
        @DisplayName("Should accept very low learning rate")
        void shouldAcceptVeryLowLearningRate() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "llama-2",
                    "s3://data/data.jsonl",
                    5,
                    0.00005
            );

            assertThat(request.learningRate()).isEqualTo(0.00005);
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            FineTuneRequestDto request1 = new FineTuneRequestDto(
                    "gpt-3.5-turbo", "s3://data.jsonl", 5, 0.001
            );
            FineTuneRequestDto request2 = new FineTuneRequestDto(
                    "gpt-3.5-turbo", "s3://data.jsonl", 5, 0.001
            );

            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when base models differ")
        void shouldNotBeEqualWhenBaseModelsDiffer() {
            FineTuneRequestDto request1 = new FineTuneRequestDto(
                    "gpt-3.5-turbo", "s3://data.jsonl", 5, 0.001
            );
            FineTuneRequestDto request2 = new FineTuneRequestDto(
                    "gpt-4", "s3://data.jsonl", 5, 0.001
            );

            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should not be equal when epochs differ")
        void shouldNotBeEqualWhenEpochsDiffer() {
            FineTuneRequestDto request1 = new FineTuneRequestDto(
                    "gpt-4", "s3://data.jsonl", 5, 0.001
            );
            FineTuneRequestDto request2 = new FineTuneRequestDto(
                    "gpt-4", "s3://data.jsonl", 10, 0.001
            );

            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should not be equal when learning rates differ")
        void shouldNotBeEqualWhenLearningRatesDiffer() {
            FineTuneRequestDto request1 = new FineTuneRequestDto(
                    "gpt-4", "s3://data.jsonl", 5, 0.001
            );
            FineTuneRequestDto request2 = new FineTuneRequestDto(
                    "gpt-4", "s3://data.jsonl", 5, 0.0001
            );

            assertThat(request1).isNotEqualTo(request2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in base model name")
        void shouldHandleUnicodeInBaseModelName() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "モデル-3.5",
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.baseModel()).isEqualTo("モデル-3.5");
        }

        @Test
        @DisplayName("Should handle very long base model name")
        void shouldHandleVeryLongBaseModelName() {
            String longModelName = "model-" + "a".repeat(200);
            FineTuneRequestDto request = new FineTuneRequestDto(
                    longModelName,
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.baseModel()).hasSizeGreaterThan(200);
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access baseModel component")
        void shouldAccessBaseModelComponent() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4-turbo",
                    "s3://data/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.baseModel()).isEqualTo("gpt-4-turbo");
        }

        @Test
        @DisplayName("Should access trainingDataUrl component")
        void shouldAccessTrainingDataUrlComponent() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-3.5-turbo",
                    "s3://my-bucket/training/data.jsonl",
                    5,
                    0.001
            );

            assertThat(request.trainingDataUrl()).isEqualTo("s3://my-bucket/training/data.jsonl");
        }

        @Test
        @DisplayName("Should access epochs component")
        void shouldAccessEpochsComponent() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "s3://data/data.jsonl",
                    100,
                    0.001
            );

            assertThat(request.epochs()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should access learningRate component")
        void shouldAccessLearningRateComponent() {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "bert-base",
                    "s3://data/data.jsonl",
                    5,
                    0.0005
            );

            assertThat(request.learningRate()).isEqualTo(0.0005);
        }
    }
}
