package com.gogidix.aiservices.aimodeltrainingservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aimodeltrainingservice.application.dto.StartTrainingJobRequestDto;
import com.gogidix.aiservices.aimodeltrainingservice.application.dto.TrainingJobResponseDto;
import com.gogidix.aiservices.aimodeltrainingservice.application.service.ModelTrainingApplicationService;
import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingAlgorithm;
import com.gogidix.aiservices.aimodeltrainingservice.domain.model.TrainingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModelTrainingController.class)
@DisplayName("ModelTrainingController REST API Tests")
class ModelTrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelTrainingApplicationService trainingService;

    private StartTrainingJobRequestDto validRequest;
    private TrainingJobResponseDto trainingResponse;

    @BeforeEach
    void setUp() {
        validRequest = new StartTrainingJobRequestDto(
                "sentiment_classifier",
                "s3://data/training.csv",
                Map.of("epochs", 100, "learning_rate", 0.001),
                TrainingAlgorithm.NEURAL_NETWORK
        );

        trainingResponse = new TrainingJobResponseDto(
                "training-job-123",
                "sentiment_classifier",
                TrainingStatus.QUEUED,
                null,
                null,
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );
    }

    @Nested
    @DisplayName("POST /api/v1/training/jobs - Start Training Job")
    class StartTrainingJobTests {

        @Test
        @DisplayName("Should start training job successfully")
        void shouldStartTrainingJob() throws Exception {
            when(trainingService.startTrainingJob(any(StartTrainingJobRequestDto.class)))
                    .thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.trainingJobId").exists())
                    .andExpect(jsonPath("$.modelType").value("sentiment_classifier"))
                    .andExpect(jsonPath("$.status").value("QUEUED"))
                    .andExpect(jsonPath("$.startedAt").exists())
                    .andExpect(jsonPath("$.estimatedCompletion").exists());

            verify(trainingService).startTrainingJob(any(StartTrainingJobRequestDto.class));
        }

        @Test
        @DisplayName("Should accept all training algorithms")
        void shouldAcceptAllTrainingAlgorithms() throws Exception {
            TrainingAlgorithm[] algorithms = {
                    TrainingAlgorithm.RANDOM_FOREST,
                    TrainingAlgorithm.NEURAL_NETWORK,
                    TrainingAlgorithm.XGBOOST,
                    TrainingAlgorithm.LINEAR_REGRESSION,
                    TrainingAlgorithm.LOGISTIC_REGRESSION,
                    TrainingAlgorithm.SVM,
                    TrainingAlgorithm.KNN,
                    TrainingAlgorithm.DECISION_TREE
            };

            for (TrainingAlgorithm algorithm : algorithms) {
                StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                        "model",
                        "s3://data/data.csv",
                        null,
                        algorithm
                );

                when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

                mockMvc.perform(post("/api/v1/training/jobs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept hyperparameters")
        void shouldAcceptHyperparameters() throws Exception {
            Map<String, Object> hyperparameters = Map.of(
                    "epochs", 100,
                    "batch_size", 32,
                    "learning_rate", 0.001,
                    "early_stopping", true
            );

            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "classifier",
                    "s3://data/data.csv",
                    hyperparameters,
                    TrainingAlgorithm.NEURAL_NETWORK
            );

            when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept different data sources")
        void shouldAcceptDifferentDataSources() throws Exception {
            String[] dataSources = {
                    "s3://bucket/data.csv",
                    "gs://bucket/data.csv",
                    "https://example.com/data.csv",
                    "file:///local/data.csv"
            };

            for (String dataSource : dataSources) {
                StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                        "model",
                        dataSource,
                        null,
                        TrainingAlgorithm.LINEAR_REGRESSION
                );

                when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

                mockMvc.perform(post("/api/v1/training/jobs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/training/jobs/{jobId}/status - Get Training Status")
    class GetTrainingStatusTests {

        @Test
        @DisplayName("Should return training status")
        void shouldReturnTrainingStatus() throws Exception {
            when(trainingService.getTrainingStatus(eq("training-job-123"), eq("tenant-001")))
                    .thenReturn(trainingResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "training-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.trainingJobId").value("training-job-123"))
                    .andExpect(jsonPath("$.modelType").value("sentiment_classifier"))
                    .andExpect(jsonPath("$.status").value("QUEUED"));

            verify(trainingService).getTrainingStatus("training-job-123", "tenant-001");
        }

        @Test
        @DisplayName("Should return QUEUED status")
        void shouldReturnQueuedStatus() throws Exception {
            TrainingJobResponseDto queuedResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.QUEUED,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(queuedResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("QUEUED"));
        }

        @Test
        @DisplayName("Should return RUNNING status")
        void shouldReturnRunningStatus() throws Exception {
            TrainingJobResponseDto runningResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.RUNNING,
                    null,
                    null,
                    Instant.now().minusSeconds(300),
                    Instant.now().plusSeconds(3300)
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(runningResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("RUNNING"));
        }

        @Test
        @DisplayName("Should return COMPLETED status with metrics")
        void shouldReturnCompletedStatusWithMetrics() throws Exception {
            TrainingJobResponseDto completedResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.COMPLETED,
                    "s3://models/model.pkl",
                    Map.of("accuracy", 0.95, "loss", 0.05),
                    Instant.now().minusSeconds(3600),
                    Instant.now()
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(completedResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.modelArtifactUrl").exists())
                    .andExpect(jsonPath("$.metrics").exists());
        }

        @Test
        @DisplayName("Should return FAILED status")
        void shouldReturnFailedStatus() throws Exception {
            TrainingJobResponseDto failedResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.FAILED,
                    null,
                    null,
                    Instant.now().minusSeconds(60),
                    Instant.now()
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(failedResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("FAILED"));
        }

        @Test
        @DisplayName("Should return CANCELLED status")
        void shouldReturnCancelledStatus() throws Exception {
            TrainingJobResponseDto cancelledResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.CANCELLED,
                    null,
                    null,
                    Instant.now().minusSeconds(30),
                    Instant.now()
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(cancelledResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }

        @Test
        @DisplayName("Should return TIMEOUT status")
        void shouldReturnTimeoutStatus() throws Exception {
            TrainingJobResponseDto timeoutResponse = new TrainingJobResponseDto(
                    "job-123",
                    "model",
                    TrainingStatus.TIMEOUT,
                    null,
                    null,
                    Instant.now().minusSeconds(30),
                    Instant.now()
            );

            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(timeoutResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("TIMEOUT"));
        }

        @Test
        @DisplayName("Should handle special characters in job ID")
        void shouldHandleSpecialCharactersInJobId() throws Exception {
            when(trainingService.getTrainingStatus(anyString(), anyString()))
                    .thenReturn(trainingResponse);

            mockMvc.perform(get("/api/v1/training/jobs/{jobId}/status", "job-with_special.chars")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/training/jobs/{jobId} - Cancel Training Job")
    class CancelTrainingJobTests {

        @Test
        @DisplayName("Should cancel training job successfully")
        void shouldCancelTrainingJob() throws Exception {
            doNothing().when(trainingService).cancelTrainingJob(eq("training-job-123"), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/training/jobs/{jobId}", "training-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());

            verify(trainingService).cancelTrainingJob("training-job-123", "tenant-001");
        }

        @Test
        @DisplayName("Should handle cancel for queued job")
        void shouldHandleCancelForQueuedJob() throws Exception {
            doNothing().when(trainingService).cancelTrainingJob(anyString(), anyString());

            mockMvc.perform(delete("/api/v1/training/jobs/{jobId}", "queued-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should handle cancel for running job")
        void shouldHandleCancelForRunningJob() throws Exception {
            doNothing().when(trainingService).cancelTrainingJob(anyString(), anyString());

            mockMvc.perform(delete("/api/v1/training/jobs/{jobId}", "running-job-456")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/training/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/training/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Model Training Service is running"));
        }

        @Test
        @DisplayName("Should not require authentication for health check")
        void shouldNotRequireAuthForHealthCheck() throws Exception {
            mockMvc.perform(get("/api/v1/training/health"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("TrainingAlgorithm Enum Tests")
    class TrainingAlgorithmTests {

        @Test
        @DisplayName("Should handle all TrainingAlgorithm values")
        void shouldHandleAllTrainingAlgorithmValues() throws Exception {
            TrainingAlgorithm[] algorithms = TrainingAlgorithm.values();

            for (TrainingAlgorithm algorithm : algorithms) {
                StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                        "model",
                        "s3://data/data.csv",
                        null,
                        algorithm
                );

                when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

                mockMvc.perform(post("/api/v1/training/jobs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in model type")
        void shouldHandleUnicodeInModelType() throws Exception {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "分類器",
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.SVM
            );

            when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long model type")
        void shouldHandleVeryLongModelType() throws Exception {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "a".repeat(200),
                    "s3://data/data.csv",
                    null,
                    TrainingAlgorithm.LINEAR_REGRESSION
            );

            when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle empty hyperparameters")
        void shouldHandleEmptyHyperparameters() throws Exception {
            StartTrainingJobRequestDto request = new StartTrainingJobRequestDto(
                    "model",
                    "s3://data/data.csv",
                    Map.of(),
                    TrainingAlgorithm.DECISION_TREE
            );

            when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for training job response")
        void shouldReturnProperJsonStructureForTrainingJobResponse() throws Exception {
            when(trainingService.startTrainingJob(any())).thenReturn(trainingResponse);

            mockMvc.perform(post("/api/v1/training/jobs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.trainingJobId").exists())
                    .andExpect(jsonPath("$.modelType").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.modelArtifactUrl").exists())
                    .andExpect(jsonPath("$.metrics").exists())
                    .andExpect(jsonPath("$.startedAt").exists())
                    .andExpect(jsonPath("$.estimatedCompletion").exists());
        }
    }
}
