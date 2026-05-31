package com.gogidix.aiservices.aiprediction.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aiprediction.application.dto.request.GeneratePredictionRequest;
import com.gogidix.aiservices.aiprediction.application.dto.response.PredictionResponse;
import com.gogidix.aiservices.aiprediction.application.service.PredictionService;
import com.gogidix.aiservices.aiprediction.shared.exception.PredictionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PredictionController.class)
@DisplayName("Prediction Controller Interface Tests")
class PredictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PredictionService predictionService;

    private static final String EXECUTION_ID = "exec-123";

    @Nested
    @DisplayName("Prediction Endpoints")
    class PredictionEndpoints {

        @Test
        @DisplayName("POST /api/v1/predictions/generate - Should generate prediction")
        void shouldGeneratePrediction() throws Exception {
            GeneratePredictionRequest request = GeneratePredictionRequest.builder()
                    .modelId("model-abc")
                    .modelVersion("v1.0")
                    .inputData(Map.of("age", 30))
                    .build();

            PredictionResponse response = PredictionResponse.builder()
                    .predictionId(EXECUTION_ID)
                    .confidence(0.95)
                    .build();

            when(predictionService.generatePrediction(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/predictions/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.predictionId").exists())
                    .andExpect(jsonPath("$.confidence").value(0.95));

            verify(predictionService).generatePrediction(any());
        }

        @Test
        @DisplayName("GET /api/v1/predictions/{id} - Should get prediction")
        void shouldGetPrediction() throws Exception {
            PredictionResponse response = PredictionResponse.builder()
                    .predictionId(EXECUTION_ID)
                    .confidence(0.95)
                    .build();

            when(predictionService.getPrediction(EXECUTION_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/predictions/{id}", EXECUTION_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.predictionId").value(EXECUTION_ID));
        }

        @Test
        @DisplayName("GET /api/v1/predictions/{id} - Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(predictionService.getPrediction(EXECUTION_ID))
                    .thenThrow(new PredictionNotFoundException(EXECUTION_ID));

            mockMvc.perform(get("/api/v1/predictions/{id}", EXECUTION_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());
        }
    }

    @Nested
    @DisplayName("Batch Prediction Endpoints")
    class BatchEndpoints {

        @Test
        @DisplayName("POST /api/v1/predictions/batch - Should process batch")
        void shouldProcessBatch() throws Exception {
            List<Map<String, Object>> requests = List.of(
                    Map.of("modelId", "model-abc", "inputData", Map.of("id", 1))
            );

            when(predictionService.batchPredict(any())).thenReturn(List.of());

            mockMvc.perform(post("/api/v1/predictions/batch")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requests)))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle prediction not found")
        void shouldHandleNotFound() throws Exception {
            when(predictionService.getPrediction(EXECUTION_ID))
                    .thenThrow(new PredictionNotFoundException(EXECUTION_ID));

            mockMvc.perform(get("/api/v1/predictions/{id}", EXECUTION_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());
        }
    }
}
