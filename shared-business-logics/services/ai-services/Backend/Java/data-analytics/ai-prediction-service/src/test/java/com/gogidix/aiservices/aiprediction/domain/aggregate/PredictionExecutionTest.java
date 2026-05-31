package com.gogidix.aiservices.aiprediction.domain.aggregate;

import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PredictionExecution Aggregate Tests")
class PredictionExecutionTest {

    private static final String MODEL_ID = "model-abc";
    private static final String MODEL_VERSION = "v1.0";
    private static final Map<String, Object> INPUT_DATA = Map.of("age", 30, "income", 50000);

    @Nested
    @DisplayName("PredictionExecution Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create prediction execution with valid parameters")
        void shouldCreateWithValidParameters() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);

            assertThat(execution).isNotNull();
            assertThat(execution.getExecutionId()).isNotNull();
            assertThat(execution.getModelId()).isEqualTo(MODEL_ID);
            assertThat(execution.getModelVersion()).isEqualTo(MODEL_VERSION);
            assertThat(execution.getInputData()).isEqualTo(INPUT_DATA);
            assertThat(execution.getStatus()).isEqualTo(Status.PENDING);
        }

        @Test
        @DisplayName("Should reject null model ID")
        void shouldRejectNullModelId() {
            assertThatThrownBy(() -> PredictionExecution.create(null, MODEL_VERSION, INPUT_DATA))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Model ID cannot be null");
        }

        @Test
        @DisplayName("Should reject null model version")
        void shouldRejectNullModelVersion() {
            assertThatThrownBy(() -> PredictionExecution.create(MODEL_ID, null, INPUT_DATA))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Model version cannot be null");
        }

        @Test
        @DisplayName("Should reject null input data")
        void shouldRejectNullInputData() {
            assertThatThrownBy(() -> PredictionExecution.create(MODEL_ID, MODEL_VERSION, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Input data cannot be null");
        }
    }

    @Nested
    @DisplayName("Execution Status Tests")
    class StatusTests {

        @Test
        @DisplayName("Should start execution")
        void shouldStartExecution() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);

            execution.start();

            assertThat(execution.getStatus()).isEqualTo(Status.RUNNING);
            assertThat(execution.getStartedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should complete execution")
        void shouldCompleteExecution() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);
            execution.start();
            PredictionResult result = PredictionResult.create("pred-1", MODEL_ID);
            result.setConfidence(0.95);

            execution.complete(result);

            assertThat(execution.getStatus()).isEqualTo(Status.COMPLETED);
            assertThat(execution.getResult()).isNotNull();
            assertThat(execution.getCompletedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should fail execution")
        void shouldFailExecution() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);
            execution.start();

            execution.fail("Model inference error");

            assertThat(execution.getStatus()).isEqualTo(Status.FAILED);
            assertThat(execution.getErrorMessage()).isEqualTo("Model inference error");
        }
    }

    @Nested
    @DisplayName("Timeout Tests")
    class TimeoutTests {

        @Test
        @DisplayName("Should set timeout")
        void shouldSetTimeout() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);

            execution.setTimeout(30);

            assertThat(execution.getTimeout()).isEqualTo(30);
        }

        @Test
        @DisplayName("Should reject timeout below minimum")
        void shouldRejectLowTimeout() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);

            assertThatThrownBy(() -> execution.setTimeout(0))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("at least 1");
        }

        @Test
        @DisplayName("Should reject timeout above maximum")
        void shouldRejectHighTimeout() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, MODEL_VERSION, INPUT_DATA);

            assertThatThrownBy(() -> execution.setTimeout(31))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("cannot exceed 30");
        }
    }

    enum Status {
        PENDING, RUNNING, COMPLETED, FAILED
    }
}
