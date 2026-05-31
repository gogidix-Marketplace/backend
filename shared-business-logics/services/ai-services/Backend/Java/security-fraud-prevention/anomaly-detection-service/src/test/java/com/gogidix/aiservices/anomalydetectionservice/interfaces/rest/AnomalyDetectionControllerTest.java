package com.gogidix.aiservices.anomalydetectionservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.anomalydetectionservice.application.dto.request.DetectionRequest;
import com.gogidix.aiservices.anomalydetectionservice.application.service.AnomalyDetectionService;
import com.gogidix.aiservices.anomalydetectionservice.domain.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnomalyDetectionController REST API Tests")
class AnomalyDetectionControllerTest {

    @Mock
    private AnomalyDetectionService service;

    private MockMvc mockMvc;
    private AnomalyDetectionController controller;
    private ObjectMapper objectMapper;

    private static final String ANALYSIS_ID = "analysis-123";
    private static final String DATA_SOURCE = "api-gateway-metrics";

    @BeforeEach
    void setUp() {
        controller = new AnomalyDetectionController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Nested
    @DisplayName("POST /api/v1/anomalies/detect")
    class DetectEndpointTests {

        @Test
        @DisplayName("Should detect anomalies successfully")
        void shouldDetectAnomaliesSuccessfully() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setStartTime(Instant.now().minusSeconds(3600));
            request.setEndTime(Instant.now());
            request.setSensitivity(SensitivityLevel.HIGH);
            request.setAlgorithms(List.of(DetectionAlgorithm.ISOLATION_FOREST));

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").exists());

            verify(service).detectAnomalies(anyString(), any(), any(), any(), any());
        }

        @Test
        @DisplayName("Should accept MEDIUM as default sensitivity")
        void shouldAcceptMEDIUMAsDefaultSensitivity() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(service).detectAnomalies(eq(DATA_SOURCE), any(), any(), eq(SensitivityLevel.MEDIUM), any());
        }

        @ParameterizedTest
        @EnumSource(SensitivityLevel.class)
        @DisplayName("Should accept all sensitivity levels")
        void shouldAcceptAllSensitivityLevels(SensitivityLevel sensitivity) throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setSensitivity(sensitivity);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should reject empty data source")
        void shouldRejectEmptyDataSource() throws Exception {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource("");

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should reject null data source")
        void shouldRejectNullDataSource() throws Exception {
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(null);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @ValueSource(strings = {"api-gateway-metrics", "database-logs", "application-traces"})
        @DisplayName("Should accept various data sources")
        void shouldAcceptVariousDataSources(String dataSource) throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(dataSource);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/anomalies/analysis/{analysisId}")
    class GetAnalysisEndpointTests {

        @Test
        @DisplayName("Should get analysis by ID")
        void shouldGetAnalysisById() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.getAnalysisResult(eq(ANALYSIS_ID))).thenReturn(detection);

            mockMvc.perform(get("/api/v1/anomalies/analysis/" + ANALYSIS_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").value(ANALYSIS_ID));

            verify(service).getAnalysisResult(eq(ANALYSIS_ID));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200Ok() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.getAnalysisResult(anyString())).thenReturn(detection);

            mockMvc.perform(get("/api/v1/anomalies/analysis/analysis-001"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept various analysis IDs")
        void shouldAcceptVariousAnalysisIds() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.getAnalysisResult(anyString())).thenReturn(detection);

            String[] analysisIds = {"analysis-001", "analysis-abc", "analysis-x9y8z7"};

            for (String analysisId : analysisIds) {
                mockMvc.perform(get("/api/v1/anomalies/analysis/" + analysisId))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("GET /api/v1/anomalies/history")
    class GetHistoryEndpointTests {

        @Test
        @DisplayName("Should get history for data source")
        void shouldGetHistoryForDataSource() throws Exception {
            List<AnomalyDetection> history = List.of(createMockDetection());
            when(service.getHistory(eq(DATA_SOURCE), eq(10))).thenReturn(history);

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

            verify(service).getHistory(eq(DATA_SOURCE), eq(10));
        }

        @Test
        @DisplayName("Should use default limit of 10")
        void shouldUseDefaultLimitOf10() throws Exception {
            when(service.getHistory(anyString(), eq(10))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE))
                    .andExpect(status().isOk());

            verify(service).getHistory(eq(DATA_SOURCE), eq(10));
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 5, 10, 20, 50})
        @DisplayName("Should accept various limit values")
        void shouldAcceptVariousLimitValues(int limit) throws Exception {
            when(service.getHistory(anyString(), eq(limit))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE)
                            .param("limit", String.valueOf(limit)))
                    .andExpect(status().isOk());

            verify(service).getHistory(eq(DATA_SOURCE), eq(limit));
        }

        @Test
        @DisplayName("Should accept various data sources")
        void shouldAcceptVariousDataSources() throws Exception {
            when(service.getHistory(anyString(), anyInt())).thenReturn(List.of());

            String[] dataSources = {"api-gateway-metrics", "database-logs", "application-traces"};

            for (String dataSource : dataSources) {
                mockMvc.perform(get("/api/v1/anomalies/history")
                                .param("dataSource", dataSource))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should return empty list when no history")
        void shouldReturnEmptyListWhenNoHistory() throws Exception {
            when(service.getHistory(anyString(), anyInt())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle detect then retrieve workflow")
        void shouldHandleDetectThenRetrieveWorkflow() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);
            when(service.getAnalysisResult(eq(ANALYSIS_ID))).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setSensitivity(SensitivityLevel.HIGH);

            // Detect
            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            // Retrieve
            mockMvc.perform(get("/api/v1/anomalies/analysis/" + ANALYSIS_ID))
                    .andExpect(status().isOk());

            verify(service).detectAnomalies(anyString(), any(), any(), any(), any());
            verify(service).getAnalysisResult(eq(ANALYSIS_ID));
        }

        @Test
        @DisplayName("Should handle detect then history workflow")
        void shouldHandleDetectThenHistoryWorkflow() throws Exception {
            AnomalyDetection detection = createMockDetection();
            List<AnomalyDetection> history = List.of(detection);

            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);
            when(service.getHistory(eq(DATA_SOURCE), anyInt())).thenReturn(history);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);

            // Detect
            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            // Get history
            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE))
                    .andExpect(status().isOk());

            verify(service).detectAnomalies(anyString(), any(), any(), any(), any());
            verify(service).getHistory(eq(DATA_SOURCE), eq(10));
        }
    }

    @Nested
    @DisplayName("Time Range Tests")
    class TimeRangeTests {

        @Test
        @DisplayName("Should accept time range")
        void shouldAcceptTimeRange() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setStartTime(Instant.now().minusSeconds(7200));
            request.setEndTime(Instant.now());

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept null start time")
        void shouldAcceptNullStartTime() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setStartTime(null);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept null end time")
        void shouldAcceptNullEndTime() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setEndTime(null);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Algorithm Tests")
    class AlgorithmTests {

        @Test
        @DisplayName("Should accept single algorithm")
        void shouldAcceptSingleAlgorithm() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setAlgorithms(List.of(DetectionAlgorithm.Z_SCORE));

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept multiple algorithms")
        void shouldAcceptMultipleAlgorithms() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setAlgorithms(List.of(
                    DetectionAlgorithm.ISOLATION_FOREST,
                    DetectionAlgorithm.Z_SCORE,
                    DetectionAlgorithm.LOCAL_OUTLIER_FACTOR
            ));

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept null algorithms")
        void shouldAcceptNullAlgorithms() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);
            request.setAlgorithms(null);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json")
        void shouldAcceptApplicationJson() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return application/json")
        void shouldReturnApplicationJson() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            DetectionRequest request = new DetectionRequest();
            request.setDataSource(DATA_SOURCE);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long data source name")
        void shouldHandleVeryLongDataSourceName() throws Exception {
            AnomalyDetection detection = createMockDetection();
            when(service.detectAnomalies(anyString(), any(), any(), any(), any())).thenReturn(detection);

            String longDataSource = "source-" + "x".repeat(200);
            DetectionRequest request = new DetectionRequest();
            request.setDataSource(longDataSource);

            mockMvc.perform(post("/api/v1/anomalies/detect")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very large limit")
        void shouldHandleVeryLargeLimit() throws Exception {
            when(service.getHistory(anyString(), anyInt())).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE)
                            .param("limit", "10000"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle zero limit")
        void shouldHandleZeroLimit() throws Exception {
            when(service.getHistory(anyString(), eq(0))).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/anomalies/history")
                            .param("dataSource", DATA_SOURCE)
                            .param("limit", "0"))
                    .andExpect(status().isOk());
        }
    }

    // Helper methods
    private AnomalyDetection createMockDetection() {
        return AnomalyDetection.builder()
                .analysisId(ANALYSIS_ID)
                .dataSource(DATA_SOURCE)
                .startTime(Instant.now().minusSeconds(3600))
                .endTime(Instant.now())
                .sensitivity(SensitivityLevel.HIGH)
                .algorithms(List.of(DetectionAlgorithm.ISOLATION_FOREST))
                .anomalies(List.of(Anomaly.builder().score(0.9).build()))
                .summary(AnomalySummary.builder()
                        .totalAnomalies(10)
                        .highSeverity(2)
                        .mediumSeverity(5)
                        .lowSeverity(3)
                        .averageScore(0.75)
                        .build())
                .build();
    }
}
