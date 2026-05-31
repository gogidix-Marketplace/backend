package com.gogidix.aiservices.aiprediction.application.service;

import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.domain.aggregate.PredictionExecution;
import com.gogidix.aiservices.aiprediction.domain.model.PredictionResult;
import com.gogidix.aiservices.aiprediction.domain.port.out.PredictionRepository;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Prediction Service Application Tests")
class PredictionServiceTest {

    @Mock
    private PredictionRepository predictionRepository;

    @InjectMocks
    private PredictionService predictionService;

    private static final String MODEL_ID = "model-abc";
    private static final String EXECUTION_ID = "exec-123";

    @Nested
    @DisplayName("Prediction Generation Tests")
    class GenerationTests {

        @Test
        @DisplayName("Should generate prediction successfully")
        void shouldGeneratePrediction() {
            GeneratePredictionRequest request = GeneratePredictionRequest.builder()
                    .modelId(MODEL_ID)
                    .modelVersion("v1.0")
                    .inputData(Map.of("age", 30))
                    .build();

            PredictionExecution execution = PredictionExecution.create(MODEL_ID, "v1.0", Map.of("age", 30));
            PredictionResult result = PredictionResult.create("pred-1", MODEL_ID);
            result.setConfidence(0.95);
            execution.start();
            execution.complete(result);

            when(predictionRepository.save(any())).thenReturn(execution);
            when(predictionRepository.findById(any())).thenReturn(Optional.of(execution));

            PredictionResponse response = predictionService.generatePrediction(request);

            assertThat(response).isNotNull();
            verify(predictionRepository).save(any());
        }

        @Test
        @DisplayName("Should reject prediction with null model ID")
        void shouldRejectNullModelId() {
            GeneratePredictionRequest request = GeneratePredictionRequest.builder()
                    .modelId(null)
                    .inputData(Map.of())
                    .build();

            assertThatThrownBy(() -> predictionService.generatePrediction(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should handle prediction timeout")
        void shouldHandleTimeout() {
            GeneratePredictionRequest request = GeneratePredictionRequest.builder()
                    .modelId(MODEL_ID)
                    .inputData(Map.of())
                    .timeout(30)
                    .build();

            PredictionExecution execution = PredictionExecution.create(MODEL_ID, "v1.0", Map.of());
            execution.setTimeout(30);
            execution.start();
            execution.fail("Timeout");

            when(predictionRepository.save(any())).thenReturn(execution);
            when(predictionRepository.findById(any())).thenReturn(Optional.of(execution));

            assertThatThrownBy(() -> predictionService.generatePrediction(request))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Prediction Retrieval Tests")
    class RetrievalTests {

        @Test
        @DisplayName("Should get prediction by ID")
        void shouldGetPrediction() {
            PredictionExecution execution = PredictionExecution.create(MODEL_ID, "v1.0", Map.of());
            PredictionResult result = PredictionResult.create("pred-1", MODEL_ID);
            execution.start();
            execution.complete(result);

            when(predictionRepository.findById(EXECUTION_ID)).thenReturn(Optional.of(execution));

            PredictionResponse response = predictionService.getPrediction(EXECUTION_ID);

            assertThat(response).isNotNull();
            verify(predictionRepository).findById(EXECUTION_ID);
        }

        @Test
        @DisplayName("Should throw exception when prediction not found")
        void shouldThrowWhenNotFound() {
            when(predictionRepository.findById(EXECUTION_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> predictionService.getPrediction(EXECUTION_ID))
                    .isInstanceOf(PredictionNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Batch Prediction Tests")
    class BatchTests {

        @Test
        @DisplayName("Should process batch predictions")
        void shouldProcessBatch() {
            GeneratePredictionRequest request1 = GeneratePredictionRequest.builder()
                    .modelId(MODEL_ID)
                    .inputData(Map.of("id", 1))
                    .build();
            GeneratePredictionRequest request2 = GeneratePredictionRequest.builder()
                    .modelId(MODEL_ID)
                    .inputData(Map.of("id", 2))
                    .build();

            when(predictionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            var responses = predictionService.batchPredict(java.util.List.of(request1, request2));

            assertThat(responses).hasSize(2);
            verify(predictionRepository, times(2)).save(any());
        }
    }
}
