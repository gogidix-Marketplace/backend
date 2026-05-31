package com.gogidix.aiservices.performanceoptimizationservice.interfaces.rest;

import com.gogidix.aiservices.performanceoptimizationservice.application.service.PerformanceAnalysisService;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.Bottleneck;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.PerformanceAnalysis;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.Recommendation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OptimizationController REST API Tests")
class OptimizationControllerTest {

    @Mock
    private PerformanceAnalysisService service;

    private MockMvc mockMvc;
    private OptimizationController controller;

    private static final String TENANT_ID = "tenant-001";
    private static final String SERVICE_NAME = "ai-fraud-detection";

    @BeforeEach
    void setUp() {
        controller = new OptimizationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Nested
    @DisplayName("POST /api/v1/optimization/analyze")
    class AnalyzeEndpointTests {

        @Test
        @DisplayName("Should create performance analysis")
        void shouldCreatePerformanceAnalysis() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.analysisId").value(dynamicAnalysisId))
                    .andExpect(jsonPath("$.tenantId").value(TENANT_ID))
                    .andExpect(jsonPath("$.service").value(SERVICE_NAME));

            verify(service).analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList());
        }

        @Test
        @DisplayName("Should return 201 with Location header")
        void shouldReturn201WithLocationHeader() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location",
                            "/api/v1/optimization/analyses/" + dynamicAnalysisId));
        }

        @Test
        @DisplayName("Should accept various service names")
        void shouldAcceptVariousServiceNames() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("recommendation-engine"), anyList())).thenReturn(analysis);
            when(service.analyze(eq(TENANT_ID), eq("sentiment-analysis"), anyList())).thenReturn(analysis);
            when(service.analyze(eq(TENANT_ID), eq("notification-service"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "recommendation-engine"))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "sentiment-analysis"))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "notification-service"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept various tenant IDs")
        void shouldAcceptVariousTenantIds() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq("tenant-001"), anyString(), anyList())).thenReturn(analysis);
            when(service.analyze(eq("tenant-002"), anyString(), anyList())).thenReturn(analysis);
            when(service.analyze(eq("org-123"), anyString(), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", "tenant-001")
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", "tenant-002")
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", "org-123")
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept service name with special characters")
        void shouldAcceptServiceNameWithSpecialCharacters() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("api-gateway-v2"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "api-gateway-v2"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept service name with numbers")
        void shouldAcceptServiceNameWithNumbers() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("service-v2-api"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "service-v2-api"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/optimization/analyses/{analysisId}")
    class GetAnalysisEndpointTests {

        @Test
        @DisplayName("Should get analysis by ID")
        void shouldGetAnalysisById() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID))).thenReturn(analysis);

            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").value(dynamicAnalysisId))
                    .andExpect(jsonPath("$.tenantId").value(TENANT_ID))
                    .andExpect(jsonPath("$.service").value(SERVICE_NAME));

            verify(service).getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200Ok() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID))).thenReturn(analysis);

            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept various analysis IDs")
        void shouldAcceptVariousAnalysisIds() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.getAnalysisById(anyString(), eq(TENANT_ID))).thenReturn(analysis);

            mockMvc.perform(get("/api/v1/optimization/analyses/analysis-001")
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/optimization/analyses/analysis-abc")
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/optimization/analyses/analysis-x9y8z7")
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept various tenant IDs")
        void shouldAcceptVariousTenantIds() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.getAnalysisById(eq(dynamicAnalysisId), anyString())).thenReturn(analysis);

            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk());

            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", "tenant-002"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/optimization/cache/{pattern}")
    class ClearCacheEndpointTests {

        @Test
        @DisplayName("Should clear cache with pattern")
        void shouldClearCacheWithPattern() throws Exception {
            doNothing().when(service).clearCache(anyString());

            mockMvc.perform(delete("/api/v1/optimization/cache/*"))
                    .andExpect(status().isNoContent());

            verify(service).clearCache("*");
        }

        @Test
        @DisplayName("Should return 204 No Content")
        void shouldReturn204NoContent() throws Exception {
            doNothing().when(service).clearCache(anyString());

            mockMvc.perform(delete("/api/v1/optimization/cache/*"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should accept various cache patterns")
        void shouldAcceptVariousCachePatterns() throws Exception {
            doNothing().when(service).clearCache(anyString());

            mockMvc.perform(delete("/api/v1/optimization/cache/*"))
                    .andExpect(status().isNoContent());

            mockMvc.perform(delete("/api/v1/optimization/cache/tenant:*"))
                    .andExpect(status().isNoContent());

            mockMvc.perform(delete("/api/v1/optimization/cache/analysis-*"))
                    .andExpect(status().isNoContent());

            verify(service, times(3)).clearCache(anyString());
        }

        @Test
        @DisplayName("Should accept pattern with special characters")
        void shouldAcceptPatternWithSpecialCharacters() throws Exception {
            doNothing().when(service).clearCache(anyString());

            mockMvc.perform(delete("/api/v1/optimization/cache/tenant-001:*"))
                    .andExpect(status().isNoContent());

            verify(service).clearCache("tenant-001:*");
        }
    }

    @Nested
    @DisplayName("GET /api/v1/optimization/health")
    class HealthEndpointTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/optimization/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.message").value("Performance Optimization Service is running"));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200OkForHealth() throws Exception {
            mockMvc.perform(get("/api/v1/optimization/health"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return health response structure")
        void shouldReturnHealthResponseStructure() throws Exception {
            mockMvc.perform(get("/api/v1/optimization/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete analysis workflow")
        void shouldHandleCompleteAnalysisWorkflow() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysisWithResults();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList())).thenReturn(analysis);
            when(service.getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID))).thenReturn(analysis);

            // Create analysis
            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.analysisId").value(dynamicAnalysisId));

            // Get analysis
            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.analysisId").value(dynamicAnalysisId))
                    .andExpect(jsonPath("$.bottlenecks").isArray())
                    .andExpect(jsonPath("$.recommendations").isArray())
                    .andExpect(jsonPath("$.score").value(75));
        }

        @Test
        @DisplayName("Should handle cache clearing after analysis")
        void shouldHandleCacheClearingAfterAnalysis() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList())).thenReturn(analysis);
            doNothing().when(service).clearCache(anyString());

            // Create analysis
            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());

            // Clear cache
            mockMvc.perform(delete("/api/v1/optimization/cache/*"))
                    .andExpect(status().isNoContent());

            verify(service).analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList());
            verify(service).clearCache("*");
        }

        @Test
        @DisplayName("Should support multiple analyses in sequence")
        void shouldSupportMultipleAnalysesInSequence() throws Exception {
            PerformanceAnalysis analysis1 = createMockAnalysis();
            PerformanceAnalysis analysis2 = createMockAnalysis();

            when(service.analyze(eq(TENANT_ID), eq("service-1"), anyList())).thenReturn(analysis1);
            when(service.analyze(eq(TENANT_ID), eq("service-2"), anyList())).thenReturn(analysis2);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "service-1"))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "service-2"))
                    .andExpect(status().isCreated());

            verify(service, times(2)).analyze(eq(TENANT_ID), anyString(), anyList());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle missing tenantId parameter")
        void shouldHandleMissingTenantIdParameter() throws Exception {
            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle missing serviceName parameter")
        void shouldHandleMissingServiceNameParameter() throws Exception {
            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle empty tenantId")
        void shouldHandleEmptyTenantId() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(""), eq(SERVICE_NAME), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", "")
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle empty serviceName")
        void shouldHandleEmptyServiceName() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq(""), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", ""))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long service name")
        void shouldHandleVeryLongServiceName() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String longServiceName = "service-" + "x".repeat(200);
            when(service.analyze(eq(TENANT_ID), eq(longServiceName), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", longServiceName))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long tenant ID")
        void shouldHandleVeryLongTenantId() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String longTenantId = "tenant-" + "y".repeat(200);
            when(service.analyze(eq(longTenantId), eq(SERVICE_NAME), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", longTenantId)
                            .param("serviceName", SERVICE_NAME))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle unicode in service name")
        void shouldHandleUnicodeInServiceName() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("サービス検出"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "サービス検出"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle special characters in service name")
        void shouldHandleSpecialCharactersInServiceName() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("service-v2.0_API"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "service-v2.0_API"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle URL-encoded parameters")
        void shouldHandleUrlEncodedParameters() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq("api gateway"), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", "api gateway"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Service Interaction Tests")
    class ServiceInteractionTests {

        @Test
        @DisplayName("Should call service with correct parameters")
        void shouldCallServiceWithCorrectParameters() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            when(service.analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList())).thenReturn(analysis);

            mockMvc.perform(post("/api/v1/optimization/analyze")
                            .param("tenantId", TENANT_ID)
                            .param("serviceName", SERVICE_NAME));

            verify(service).analyze(eq(TENANT_ID), eq(SERVICE_NAME), anyList());
        }

        @Test
        @DisplayName("Should call service for get analysis")
        void shouldCallServiceForGetAnalysis() throws Exception {
            PerformanceAnalysis analysis = createMockAnalysis();
            String dynamicAnalysisId = analysis.getAnalysisId();
            when(service.getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID))).thenReturn(analysis);

            mockMvc.perform(get("/api/v1/optimization/analyses/" + dynamicAnalysisId)
                            .param("tenantId", TENANT_ID));

            verify(service).getAnalysisById(eq(dynamicAnalysisId), eq(TENANT_ID));
        }

        @Test
        @DisplayName("Should call service for cache clearing")
        void shouldCallServiceForCacheClearing() throws Exception {
            doNothing().when(service).clearCache(anyString());

            mockMvc.perform(delete("/api/v1/optimization/cache/*"));

            verify(service).clearCache("*");
        }
    }

    // Helper methods
    private PerformanceAnalysis createMockAnalysis() {
        PerformanceAnalysis analysis = new PerformanceAnalysis(TENANT_ID, SERVICE_NAME, List.of("cpu", "memory", "responseTime"));
        return analysis;
    }

    private PerformanceAnalysis createMockAnalysisWithResults() {
        PerformanceAnalysis analysis = createMockAnalysis();
        List<Bottleneck> bottlenecks = List.of(
                new Bottleneck("Database", "HIGH", "Slow query execution", 85.0),
                new Bottleneck("Cache", "MEDIUM", "Low hit ratio", 60.0)
        );
        List<Recommendation> recommendations = List.of(
                new Recommendation("OPTIMIZATION", "Add database index", 1, "CREATE INDEX idx_user_email ON users(email);"),
                new Recommendation("CACHING", "Implement Redis caching", 2, "Add @Cacheable annotation to getUser method")
        );
        analysis.complete(bottlenecks, recommendations, 75);
        return analysis;
    }
}
