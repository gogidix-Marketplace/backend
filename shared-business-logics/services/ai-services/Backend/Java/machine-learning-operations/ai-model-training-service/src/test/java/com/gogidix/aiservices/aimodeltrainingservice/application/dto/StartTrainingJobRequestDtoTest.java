package com.gogidix.aiservices.aimodeltrainingservice.application.dto;

import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StartTrainingJobRequestDto Tests")
class StartTrainingJobRequestDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request with all fields")
        void shouldCreateWithAllFields() {
            Map<String, Object> hyperparameters = Map.of(
                    "epochs", 100,
                    "learning_rate", 0.001,
                    "batch_size", 32
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "sentiment_classifier",
                    "s3://data/training.csv",
                    hyperparameters,
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request.modelType()).isEqualTo("sentiment_classifier");
            assertThat(request.trainingDataUrl()).isEqualTo("s3://data/training.csv");
            assertThat(request.hyperparameters()).hasSize(3);
            assertThat(request.algorithm()).isEqualTo(TrainingAlgorithm.NEURAL_NETWORK);
        }

        @Test
        @DisplayName("Should create request with minimal fields")
        void shouldCreateWithMinimalFields() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "regressor",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.modelType()).isEqualTo("regressor");
            assertThat(request.trainingDataUrl()).isEqualTo("s3://data/data.csv");
            assertThat(request.hyperparameters()).isNull();
        }

        @Test
        @DisplayName("Should create request with empty hyperparameters")
        void shouldCreateWithEmptyHyperparameters() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "classifier",
                    "s3://data/data.csv",
                    Map.of(),
                    TrainingAlgorithm.DECISION_TREE
            );

            assertThat(request.hyperparameters()).isEmpty();
        }
    }

    @Nested
    @DisplayName("TrainingAlgorithm Support Tests")
    class TrainingAlgorithmSupportTests {

        @ParameterizedTest
        @EnumSource(TrainingAlgorithm.class)
        @DisplayName("Should accept all TrainingAlgorithm values")
        void shouldAcceptAllTrainingAlgorithms(TrainingAlgorithm algorithm) {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    null,
                    algorithm
            );

            assertThat(request.algorithm()).isEqualTo(algorithm);
        }

        @Test
        @DisplayName("Should accept RANDOM_FOREST algorithm")
        void shouldAcceptRandomForestAlgorithm() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "classifier",
                    "s3://data/data.csv",
                    Map.of("n_estimators", 100),
                    TrainingAlgorithm.RANDOM_FOREST
            );

            assertThat(request.algorithm()).isEqualTo(TrainingAlgorithm.RANDOM_FOREST);
        }

        @Test
        @DisplayName("Should accept NEURAL_NETWORK algorithm")
        void shouldAcceptNeuralNetworkAlgorithm() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "classifier",
                    "s3://data/data.csv",
                    Map.of("epochs", 50, "layers", 3),
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request.algorithm()).isEqualTo(TrainingAlgorithm.NEURAL_NETWORK);
        }

        @Test
        @DisplayName("Should accept XGBOOST algorithm")
        void shouldAcceptXgboostAlgorithm() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "regressor",
                    "s3://data/data.csv",
                    Map.of("max_depth", 6),
                    TrainingAlgorithm.XGBOOST
            );

            assertThat(request.algorithm()).isEqualTo(TrainingAlgorithm.XGBOOST);
        }
    }

    @Nested
    @DisplayName("Hyperparameters Tests")
    class HyperparametersTests {

        @Test
        @DisplayName("Should accept numeric hyperparameters")
        void shouldAcceptNumericHyperparameters() {
            Map<String, Object> hyperparameters = Map.of(
                    "learning_rate", 0.01,
                    "epochs", 100,
                    "batch_size", 32
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    hyperparameters,
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request.hyperparameters()).containsKey("learning_rate");
            assertThat(request.hyperparameters().get("learning_rate")).isEqualTo(0.01);
        }

        @Test
        @DisplayName("Should accept string hyperparameters")
        void shouldAcceptStringHyperparameters() {
            Map<String, Object> hyperparameters = Map.of(
                    "optimizer", "adam",
                    "loss_function", "mse"
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    hyperparameters,
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request.hyperparameters().get("optimizer")).isEqualTo("adam");
        }

        @Test
        @DisplayName("Should accept boolean hyperparameters")
        void shouldAcceptBooleanHyperparameters() {
            Map<String, Object> hyperparameters = Map.of(
                    "early_stopping", true,
                    "normalize", false
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    hyperparameters,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.hyperparameters().get("early_stopping")).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept nested hyperparameters")
        void shouldAcceptNestedHyperparameters() {
            Map<String, Object> hyperparameters = Map.of(
                    "layer_config", Map.of("hidden_layers", 3, "units", 128)
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    hyperparameters,
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request.hyperparameters().get("layer_config")).isInstanceOf(Map.class);
        }
    }

    @Nested
    @DisplayName("Data URL Tests")
    class DataUrlTests {

        @Test
        @DisplayName("Should accept S3 URL")
        void shouldAcceptS3Url() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://bucket/path/to/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.trainingDataUrl()).startsWith("s3://");
        }

        @Test
        @DisplayName("Should accept GCS URL")
        void shouldAcceptGcsUrl() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "gs://bucket/path/to/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.trainingDataUrl()).startsWith("gs://");
        }

        @Test
        @DisplayName("Should accept HTTPS URL")
        void shouldAcceptHttpsUrl() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "https://storage.example.com/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.trainingDataUrl()).startsWith("https://");
        }

        @Test
        @DisplayName("Should accept file path URL")
        void shouldAcceptFilePathUrl() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "file:///data/training.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.trainingDataUrl()).startsWith("file://");
        }
    }

    @Nested
    @DisplayName("Model Type Tests")
    class ModelTypeTests {

        @Test
        @DisplayName("Should accept classifier model type")
        void shouldAcceptClassifierModelType() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "binary_classifier",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.LOGISTIC_REGRESSION
            );

            assertThat(request.modelType()).contains("classifier");
        }

        @Test
        @DisplayName("Should accept regressor model type")
        void shouldAcceptRegressorModelType() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "linear_regressor",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.modelType()).contains("regressor");
        }

        @Test
        @DisplayName("Should accept custom model type")
        void shouldAcceptCustomModelType() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "custom_anomaly_detector",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.SVM
            );

            assertThat(request.modelType()).contains("anomaly");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Map<String, Object> hyperparams = Map.of("lr", 0.01);

            StartTrainingJobRequestDto request1 = new StartTrainingJobRequestDto(
                    "model", "s3://data.csv", hyperparams, TrainingAlgorithm.NEURAL_NETWORK
            );
            StartTrainingJobRequestDto request2 = new StartTrainingJobRequestDto(
                    "model", "s3://data.csv", hyperparams, TrainingAlgorithm.NEURAL_NETWORK
            );

            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when model types differ")
        void shouldNotBeEqualWhenModelTypesDiffer() {
            StartTrainingJobRequestDto request1 = new StartTrainingJobRequestDto(
                    "model1", "s3://data.csv", null, TrainingAlgorithm.LINEAR_REGRESSION
            );
            StartTrainingJobRequestDto request2 = new StartTrainingJobRequestDto(
                    "model2", "s3://data.csv", null, TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Should not be equal when algorithms differ")
        void shouldNotBeEqualWhenAlgorithmsDiffer() {
            StartTrainingJobRequestDto request1 = new StartTrainingJobRequestDto(
                    "model", "s3://data.csv", null, TrainingAlgorithm.LINEAR_REGRESSION
            );
            StartTrainingJobRequestDto request2 = new StartTrainingJobRequestDto(
                    "model", "s3://data.csv", null, TrainingAlgorithm.RANDOM_FOREST
            );

            assertThat(request1).isNotEqualTo(request2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in model type")
        void shouldHandleUnicodeInModelType() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "モデル-分類器",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.SVM
            );

            assertThat(request.modelType()).isEqualTo("モデル-分類器");
        }

        @Test
        @DisplayName("Should handle very long model type")
        void shouldHandleVeryLongModelType() {
            String longModelType = "a".repeat(200);
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    longModelType,
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            assertThat(request.modelType()).hasSize(200);
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access modelType component")
        void shouldAccessModelTypeComponent() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "my-model",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.DECISION_TREE
            );

            assertThat(request.modelType()).isEqualTo("my-model");
        }

        @Test
        @DisplayName("Should access trainingDataUrl component")
        void shouldAccessTrainingDataUrlComponent() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://my-bucket/data.csv",
                    null,
                    TrainingAlgorithm.SVM
            );

            assertThat(request.trainingDataUrl()).isEqualTo("s3://my-bucket/data.csv");
        }

        @Test
        @DisplayName("Should access hyperparameters component")
        void shouldAccessHyperparametersComponent() {
            Map<String, Object> hyperparams = Map.of("key", "value");
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    hyperparams,
                    TrainingAlgorithm.KNN
            );

            assertThat(request.hyperparameters()).isEqualTo(hyperparams);
        }

        @Test
        @DisplayName("Should access algorithm component")
        void shouldAccessAlgorithmComponent() {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.XGBOOST
            );

            assertThat(request.algorithm()).isEqualTo(TrainingAlgorithm.XGBOOST);
        }
    }
}
