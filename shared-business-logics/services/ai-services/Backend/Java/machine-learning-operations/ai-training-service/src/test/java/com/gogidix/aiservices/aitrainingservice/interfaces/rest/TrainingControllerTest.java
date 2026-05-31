package com.gogidix.aiservices.aitrainingservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuneRequestDto;
import com.gogidix.aiservices.aitrainingservice.application.dto.FineTuningJobResponseDto;
import com.gogidix.aiservices.aitrainingservice.application.service.TrainingApplicationService;
import com.gogidix.aiservices.aitrainingservice.domain.model.FineTuningStatus;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingController.class)
@DisplayName("TrainingController REST API Tests")
class TrainingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TrainingApplicationService trainingService;

    private FineTuneRequestDto validRequest;
    private FineTuningJobResponseDto fineTuningResponse;

    @BeforeEach
    void setUp() {
        validRequest = new FineTuneRequestDto(
                "gpt-3.5-turbo",
                "s3://data/training.jsonl",
                10,
                0.001
        );

        fineTuningResponse = new FineTuningJobResponseDto(
                "fine-tune-job-123",
                "gpt-3.5-turbo",
                FineTuningStatus.PENDING,
                null,
                null,
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );
    }

    @Nested
    @DisplayName("POST /api/v1/training/fine-tune - Fine-tune Model")
    class FineTuneModelTests {

        @Test
        @DisplayName("Should start fine-tuning job successfully")
        void shouldStartFineTuningJob() throws Exception {
            when(trainingService.fineTuneModel(any(FineTuneRequestDto.class)))
                    .thenReturn(fineTuningResponse);

            mockMvc.perform(post("/api/v1/training/fine-tune")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.fineTuningJobId").exists())
                    .andExpect(jsonPath("$.baseModel").value("gpt-3.5-turbo"))
                    .andExpect(jsonPath("$.status").value("PENDING"))
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.estimatedCompletion").exists());

            verify(trainingService).fineTuneModel(any(FineTuneRequestDto.class));
        }

        @Test
        @DisplayName("Should accept various base models")
        void shouldAcceptVariousBaseModels() throws Exception {
            String[] models = {"gpt-3.5-turbo", "gpt-4", "bert-base-uncased", "llama-2-7b"};

            for (String model : models) {
                FineTuneRequestDto request = new FineTuneRequestDto(
                        model,
                        "s3://data/training.jsonl",
                        5,
                        0.001
                );

                when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

                mockMvc.perform(post("/api/v1/training/fine-tune")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept different epoch values")
        void shouldAcceptDifferentEpochValues() throws Exception {
            Integer[] epochs = {1, 5, 10, 50, 100, 500, 1000};

            for (Integer epoch : epochs) {
                FineTuneRequestDto request = new FineTuneRequestDto(
                        "gpt-3.5-turbo",
                        "s3://data/training.jsonl",
                        epoch,
                        0.001
                );

                when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

                mockMvc.perform(post("/api/v1/training/fine-tune")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept different learning rates")
        void shouldAcceptDifferentLearningRates() throws Exception {
            Double[] learningRates = {0.0001, 0.0005, 0.001, 0.005, 0.01};

            for (Double lr : learningRates) {
                FineTuneRequestDto request = new FineTuneRequestDto(
                        "gpt-4",
                        "s3://data/training.jsonl",
                        10,
                        lr
                );

                when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

                mockMvc.perform(post("/api/v1/training/fine-tune")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept different data sources")
        void shouldAcceptDifferentDataSources() throws Exception {
            String[] dataSources = {
                    "s3://bucket/training.jsonl",
                    "gs://bucket/data.jsonl",
                    "https://example.com/data.jsonl",
                    "file:///local/training.jsonl"
            };

            for (String dataSource : dataSources) {
                FineTuneRequestDto request = new FineTuneRequestDto(
                        "bert-base",
                        dataSource,
                        5,
                        0.001
                );

                when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

                mockMvc.perform(post("/api/v1/training/fine-tune")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/training/fine-tune/{jobId} - Get Fine-Tuning Status")
    class GetFineTuningStatusTests {

        @Test
        @DisplayName("Should return fine-tuning status")
        void shouldReturnFineTuningStatus() throws Exception {
            when(trainingService.getFineTuningStatus(eq("fine-tune-job-123"), eq("tenant-001")))
                    .thenReturn(fineTuningResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "fine-tune-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fineTuningJobId").value("fine-tune-job-123"))
                    .andExpect(jsonPath("$.baseModel").value("gpt-3.5-turbo"))
                    .andExpect(jsonPath("$.status").value("PENDING"));

            verify(trainingService).getFineTuningStatus("fine-tune-job-123", "tenant-001");
        }

        @Test
        @DisplayName("Should return PENDING status")
        void shouldReturnPendingStatus() throws Exception {
            FineTuningJobResponseDto pendingResponse = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.PENDING,
                    null,
                    null,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );

            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(pendingResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("PENDING"));
        }

        @Test
        @DisplayName("Should return RUNNING status")
        void shouldReturnRunningStatus() throws Exception {
            FineTuningJobResponseDto runningResponse = new FineTuningJobResponseDto(
                    "job-123",
                    "bert-base",
                    FineTuningStatus.RUNNING,
                    null,
                    null,
                    Instant.now().minusSeconds(300),
                    Instant.now().plusSeconds(3300)
            );

            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(runningResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("RUNNING"));
        }

        @Test
        @DisplayName("Should return COMPLETED status with metrics")
        void shouldReturnCompletedStatusWithMetrics() throws Exception {
            FineTuningJobResponseDto.FineTuningJobMetricsDto metrics =
                    new FineTuningJobResponseDto.FineTuningJobMetricsDto(0.15, 0.92, 10);

            FineTuningJobResponseDto completedResponse = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-3.5-turbo",
                    FineTuningStatus.COMPLETED,
                    "s3://models/fine-tuned.pkl",
                    metrics,
                    Instant.now().minusSeconds(3600),
                    Instant.now()
            );

            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(completedResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("COMPLETED"))
                    .andExpect(jsonPath("$.fineTunedModelUrl").exists())
                    .andExpect(jsonPath("$.metrics").exists())
                    .andExpect(jsonPath("$.metrics.loss").value(0.15))
                    .andExpect(jsonPath("$.metrics.accuracy").value(0.92))
                    .andExpect(jsonPath("$.metrics.epochsCompleted").value(10));
        }

        @Test
        @DisplayName("Should return FAILED status")
        void shouldReturnFailedStatus() throws Exception {
            FineTuningJobResponseDto failedResponse = new FineTuningJobResponseDto(
                    "job-123",
                    "gpt-4",
                    FineTuningStatus.FAILED,
                    null,
                    null,
                    Instant.now().minusSeconds(60),
                    Instant.now()
            );

            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(failedResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("FAILED"));
        }

        @Test
        @DisplayName("Should return CANCELLED status")
        void shouldReturnCancelledStatus() throws Exception {
            FineTuningJobResponseDto cancelledResponse = new FineTuningJobResponseDto(
                    "job-123",
                    "bert-base",
                    FineTuningStatus.CANCELLED,
                    null,
                    null,
                    Instant.now().minusSeconds(30),
                    Instant.now()
            );

            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(cancelledResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("CANCELLED"));
        }

        @Test
        @DisplayName("Should handle special characters in job ID")
        void shouldHandleSpecialCharactersInJobId() throws Exception {
            when(trainingService.getFineTuningStatus(anyString(), anyString()))
                    .thenReturn(fineTuningResponse);

            mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-with_special.chars")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/training/schedule - Schedule Retraining")
    class ScheduleRetrainingTests {

        @Test
        @DisplayName("Should schedule retraining successfully")
        void shouldScheduleRetraining() throws Exception {
            doNothing().when(trainingService).scheduleRetraining(
                    eq("gpt-3.5-turbo"), eq("0 0 * * 0"), eq("tenant-001")
            );

            mockMvc.perform(post("/api/v1/training/schedule")
                            .param("baseModel", "gpt-3.5-turbo")
                            .param("cronExpression", "0 0 * * 0")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isAccepted());

            verify(trainingService).scheduleRetraining("gpt-3.5-turbo", "0 0 * * 0", "tenant-001");
        }

        @Test
        @DisplayName("Should accept various cron expressions")
        void shouldAcceptVariousCronExpressions() throws Exception {
            String[] cronExpressions = {
                    "0 0 * * *",     // Daily at midnight
                    "0 0 * * 0",     // Weekly on Sunday
                    "0 0 1 * *",     // Monthly on 1st
                    "*/30 * * * *",  // Every 30 minutes
                    "0 9 * * 1-5"    // Weekdays at 9am
            };

            for (String cron : cronExpressions) {
                doNothing().when(trainingService).scheduleRetraining(
                        anyString(), eq(cron), anyString()
                );

                mockMvc.perform(post("/api/v1/training/schedule")
                                .param("baseModel", "gpt-4")
                                .param("cronExpression", cron)
                                .header("X-Tenant-ID", "tenant-001"))
                        .andExpect(status().isAccepted());
            }
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/training/fine-tune/{jobId} - Cancel Fine-Tuning")
    class CancelFineTuningTests {

        @Test
        @DisplayName("Should cancel fine-tuning job successfully")
        void shouldCancelFineTuningJob() throws Exception {
            doNothing().when(trainingService).cancelFineTuning(
                    eq("fine-tune-job-123"), eq("tenant-001")
            );

            mockMvc.perform(delete("/api/v1/training/fine-tune/{jobId}", "fine-tune-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());

            verify(trainingService).cancelFineTuning("fine-tune-job-123", "tenant-001");
        }

        @Test
        @DisplayName("Should handle cancel for pending job")
        void shouldHandleCancelForPendingJob() throws Exception {
            doNothing().when(trainingService).cancelFineTuning(anyString(), anyString());

            mockMvc.perform(delete("/api/v1/training/fine-tune/{jobId}", "pending-job-123")
                            .header("X-Tenant-ID", "tenant-001"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should handle cancel for running job")
        void shouldHandleCancelForRunningJob() throws Exception {
            doNothing().when(trainingService).cancelFineTuning(anyString(), anyString());

            mockMvc.perform(delete("/api/v1/training/fine-tune/{jobId}", "running-job-456")
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
                    .andExpect(content().string("Training Service is running"));
        }

        @Test
        @DisplayName("Should not require authentication for health check")
        void shouldNotRequireAuthForHealthCheck() throws Exception {
            mockMvc.perform(get("/api/v1/training/health"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("FineTuningStatus Enum Tests")
    class FineTuningStatusTests {

        @Test
        @DisplayName("Should handle all FineTuningStatus values")
        void shouldHandleAllFineTuningStatusValues() throws Exception {
            FineTuningStatus[] statuses = FineTuningStatus.values();

            for (FineTuningStatus status : statuses) {
                FineTuningJobResponseDto response = new FineTuningJobResponseDto(
                        "job-123",
                        "gpt-3.5-turbo",
                        status,
                        null,
                        null,
                        Instant.now(),
                        Instant.now()
                );

                when(trainingService.getFineTuningStatus(anyString(), anyString()))
                        .thenReturn(response);

                mockMvc.perform(get("/api/v1/training/fine-tune/{jobId}", "job-123")
                                .header("X-Tenant-ID", "tenant-001"))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in base model name")
        void shouldHandleUnicodeInBaseModelName() throws Exception {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "モデル-3.5",
                    "s3://data/training.jsonl",
                    5,
                    0.001
            );

            when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

            mockMvc.perform(post("/api/v1/training/fine-tune")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long base model name")
        void shouldHandleVeryLongBaseModelName() throws Exception {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "a".repeat(200),
                    "s3://data/training.jsonl",
                    5,
                    0.001
            );

            when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

            mockMvc.perform(post("/api/v1/training/fine-tune")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle minimum learning rate")
        void shouldHandleMinimumLearningRate() throws Exception {
            FineTuneRequestDto request = new FineTuneRequestDto(
                    "gpt-4",
                    "s3://data/training.jsonl",
                    5,
                    0.0001
            );

            when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

            mockMvc.perform(post("/api/v1/training/fine-tune")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for fine-tuning response")
        void shouldReturnProperJsonStructureForFineTuningResponse() throws Exception {
            when(trainingService.fineTuneModel(any())).thenReturn(fineTuningResponse);

            mockMvc.perform(post("/api/v1/training/fine-tune")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.fineTuningJobId").exists())
                    .andExpect(jsonPath("$.baseModel").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.fineTunedModelUrl").exists())
                    .andExpect(jsonPath("$.metrics").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.estimatedCompletion").exists());
        }
    }
}
