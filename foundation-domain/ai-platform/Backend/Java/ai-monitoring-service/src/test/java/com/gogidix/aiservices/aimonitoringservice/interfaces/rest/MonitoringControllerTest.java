package com.gogidix.aiservices.aimonitoringservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aimonitoringservice.application.dto.AlertRuleResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.CreateAlertRuleRequestDto;
import com.gogidix.aiservices.aimonitoringservice.application.dto.ServiceHealthResponseDto;
import com.gogidix.aiservices.aimonitoringservice.application.service.AlertRuleApplicationService;
import com.gogidix.aiservices.aimonitoringservice.application.service.ServiceHealthApplicationService;
import com.gogidix.aiservices.aimonitoringservice.domain.model.AlertRuleStatus;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ConditionType;
import com.gogidix.aiservices.aimonitoringservice.domain.model.ServiceHealth;
import com.gogidix.aiservices.aimonitoringservice.shared.requestcontext.RequestContext;
import com.gogidix.aiservices.aimonitoringservice.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MonitoringController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@DisplayName("MonitoringController REST API Tests")
class MonitoringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlertRuleApplicationService alertService;

    @MockBean
    private ServiceHealthApplicationService healthService;

    private CreateAlertRuleRequestDto validRequest;
    private AlertRuleResponseDto alertResponse;
    private ServiceHealthResponseDto healthResponse;

    @BeforeEach
    void setUp() {
        RequestContext context = RequestContext.create("tenant-001", "user-123");
        RequestContextHolder.setContext(context);

        validRequest = new CreateAlertRuleRequestDto(
                "CPU High Alert",
                "cpu_usage_percent",
                ConditionType.GREATER_THAN,
                80.0,
                List.of("email", "slack"),
                60
        );

        alertResponse = new AlertRuleResponseDto(
                "id-123",
                "alert-123",
                "tenant-001",
                "CPU High Alert",
                "cpu_usage_percent",
                ConditionType.GREATER_THAN,
                80.0,
                List.of("email", "slack"),
                AlertRuleStatus.ACTIVE,
                60,
                Instant.now(),
                Instant.now(),
                null,
                0L
        );

        healthResponse = new ServiceHealthResponseDto(
                "health-123",
                "ai-model-service",
                "tenant-001",
                ServiceHealth.HealthStatus.UP,
                "Service is healthy",
                Map.of("cpu", 50.0, "memory", 75.0),
                Instant.now(),
                Instant.now(),
                100L,
                0L,
                100.0
        );
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.clearContext();
    }

    @Nested
    @DisplayName("POST /api/v1/monitoring/alerts - Create Alert Rule")
    class CreateAlertTests {

        @Test
        @DisplayName("Should create alert rule successfully")
        void shouldCreateAlertRule() throws Exception {
            when(alertService.createRule(eq("tenant-001"), any(CreateAlertRuleRequestDto.class)))
                    .thenReturn(alertResponse);

            mockMvc.perform(post("/api/v1/monitoring/alerts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.alertId").exists())
                    .andExpect(jsonPath("$.name").value("CPU High Alert"))
                    .andExpect(jsonPath("$.metric").value("cpu_usage_percent"))
                    .andExpect(jsonPath("$.condition").value("GREATER_THAN"))
                    .andExpect(jsonPath("$.threshold").value(80.0))
                    .andExpect(jsonPath("$.status").value("ACTIVE"))
                    .andExpect(jsonPath("$.minAlertInterval").value(60));

            verify(alertService).createRule(eq("tenant-001"), any(CreateAlertRuleRequestDto.class));
        }

        @Test
        @DisplayName("Should accept all condition types")
        void shouldAcceptAllConditionTypes() throws Exception {
            ConditionType[] conditionTypes = {
                    ConditionType.GREATER_THAN,
                    ConditionType.LESS_THAN,
                    ConditionType.EQUALS,
                    ConditionType.NOT_EQUALS,
                    ConditionType.CONTAINS
            };

            for (ConditionType conditionType : conditionTypes) {
                CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                        "Test Alert",
                        "metric",
                        conditionType,
                        50.0,
                        null,
                        null
                );

                when(alertService.createRule(eq("tenant-001"), any()))
                        .thenReturn(alertResponse);

                mockMvc.perform(post("/api/v1/monitoring/alerts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept various thresholds")
        void shouldAcceptVariousThresholds() throws Exception {
            Double[] thresholds = {0.0, 50.0, 100.0, 1000.0};

            for (Double threshold : thresholds) {
                CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                        "Test Alert",
                        "metric",
                        ConditionType.GREATER_THAN,
                        threshold,
                        null,
                        null
                );

                when(alertService.createRule(eq("tenant-001"), any()))
                        .thenReturn(alertResponse);

                mockMvc.perform(post("/api/v1/monitoring/alerts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }
        }

        @Test
        @DisplayName("Should accept notification channels")
        void shouldAcceptNotificationChannels() throws Exception {
            CreateAlertRuleRequestDto request = new CreateAlertRuleRequestDto(
                    "Test Alert",
                    "metric",
                    ConditionType.GREATER_THAN,
                    50.0,
                    Arrays.asList("email", "slack", "sms"),
                    null
            );

            when(alertService.createRule(eq("tenant-001"), any()))
                    .thenReturn(alertResponse);

            mockMvc.perform(post("/api/v1/monitoring/alerts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/monitoring/alerts/{alertId} - Get Alert Rule")
    class GetAlertTests {

        @Test
        @DisplayName("Should return alert rule by ID")
        void shouldReturnAlertRuleById() throws Exception {
            String alertId = alertResponse.alertId();

            when(alertService.getRuleById(eq(alertId), eq("tenant-001")))
                    .thenReturn(alertResponse);

            mockMvc.perform(get("/api/v1/monitoring/alerts/{alertId}", alertId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.alertId").exists())
                    .andExpect(jsonPath("$.name").value("CPU High Alert"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));

            verify(alertService).getRuleById(alertId, "tenant-001");
        }

        @Test
        @DisplayName("Should return alert with ACTIVE status")
        void shouldReturnAlertWithActiveStatus() throws Exception {
            AlertRuleResponseDto activeAlert = new AlertRuleResponseDto(
                    "id-456",
                    "alert-456",
                    "tenant-001",
                    "Active Alert",
                    "metric",
                    ConditionType.GREATER_THAN,
                    80.0,
                    List.of(),
                    AlertRuleStatus.ACTIVE,
                    60,
                    Instant.now(),
                    Instant.now(),
                    null,
                    0L
            );

            when(alertService.getRuleById(anyString(), eq("tenant-001")))
                    .thenReturn(activeAlert);

            mockMvc.perform(get("/api/v1/monitoring/alerts/{alertId}", "alert-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Should return alert with PAUSED status")
        void shouldReturnAlertWithPausedStatus() throws Exception {
            AlertRuleResponseDto pausedAlert = new AlertRuleResponseDto(
                    "id-789",
                    "alert-789",
                    "tenant-001",
                    "Paused Alert",
                    "metric",
                    ConditionType.LESS_THAN,
                    20.0,
                    List.of(),
                    AlertRuleStatus.PAUSED,
                    60,
                    Instant.now(),
                    Instant.now(),
                    null,
                    0L
            );

            when(alertService.getRuleById(anyString(), eq("tenant-001")))
                    .thenReturn(pausedAlert);

            mockMvc.perform(get("/api/v1/monitoring/alerts/{alertId}", "alert-456"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("PAUSED"));
        }

        @Test
        @DisplayName("Should return alert with INACTIVE status")
        void shouldReturnAlertWithInactiveStatus() throws Exception {
            AlertRuleResponseDto inactiveAlert = new AlertRuleResponseDto(
                    "id-101",
                    "alert-101",
                    "tenant-001",
                    "Inactive Alert",
                    "metric",
                    ConditionType.EQUALS,
                    1.0,
                    List.of(),
                    AlertRuleStatus.INACTIVE,
                    60,
                    Instant.now(),
                    Instant.now(),
                    null,
                    0L
            );

            when(alertService.getRuleById(anyString(), eq("tenant-001")))
                    .thenReturn(inactiveAlert);

            mockMvc.perform(get("/api/v1/monitoring/alerts/{alertId}", "alert-789"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("INACTIVE"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/monitoring/alerts - List Alert Rules")
    class ListAlertsTests {

        @Test
        @DisplayName("Should return list of alert rules")
        void shouldReturnListOfAlertRules() throws Exception {
            List<AlertRuleResponseDto> alerts = Arrays.asList(alertResponse);

            when(alertService.getRulesByTenant("tenant-001"))
                    .thenReturn(alerts);

            mockMvc.perform(get("/api/v1/monitoring/alerts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].alertId").exists())
                    .andExpect(jsonPath("$[0].name").value("CPU High Alert"));

            verify(alertService).getRulesByTenant("tenant-001");
        }

        @Test
        @DisplayName("Should return empty list when no alerts")
        void shouldReturnEmptyList() throws Exception {
            when(alertService.getRulesByTenant("tenant-001"))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/monitoring/alerts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should return multiple alert rules")
        void shouldReturnMultipleAlertRules() throws Exception {
            AlertRuleResponseDto alert2 = new AlertRuleResponseDto(
                    "id-2",
                    "alert-2",
                    "tenant-001",
                    "Memory Low Alert",
                    "memory_usage",
                    ConditionType.LESS_THAN,
                    20.0,
                    List.of(),
                    AlertRuleStatus.ACTIVE,
                    60,
                    Instant.now(),
                    Instant.now(),
                    null,
                    0L
            );

            List<AlertRuleResponseDto> alerts = Arrays.asList(alertResponse, alert2);

            when(alertService.getRulesByTenant("tenant-001"))
                    .thenReturn(alerts);

            mockMvc.perform(get("/api/v1/monitoring/alerts"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/monitoring/alerts/{alertId} - Delete Alert Rule")
    class DeleteAlertTests {

        @Test
        @DisplayName("Should delete alert rule successfully")
        void shouldDeleteAlertRule() throws Exception {
            doNothing().when(alertService).deleteRule(eq("alert-123"), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/monitoring/alerts/{alertId}", "alert-123"))
                    .andExpect(status().isNoContent());

            verify(alertService).deleteRule("alert-123", "tenant-001");
        }

        @Test
        @DisplayName("Should delete alert with UUID")
        void shouldDeleteAlertWithUuid() throws Exception {
            String uuid = "alert-abc123";
            doNothing().when(alertService).deleteRule(eq(uuid), eq("tenant-001"));

            mockMvc.perform(delete("/api/v1/monitoring/alerts/{alertId}", uuid))
                    .andExpect(status().isNoContent());

            verify(alertService).deleteRule(uuid, "tenant-001");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/monitoring/alerts/{alertId}/activate - Activate Alert")
    class ActivateAlertTests {

        @Test
        @DisplayName("Should activate alert rule successfully")
        void shouldActivateAlertRule() throws Exception {
            doNothing().when(alertService).activateRule(eq("alert-123"), eq("tenant-001"));

            mockMvc.perform(post("/api/v1/monitoring/alerts/{alertId}/activate", "alert-123"))
                    .andExpect(status().isNoContent());

            verify(alertService).activateRule("alert-123", "tenant-001");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/monitoring/alerts/{alertId}/deactivate - Deactivate Alert")
    class DeactivateAlertTests {

        @Test
        @DisplayName("Should deactivate alert rule successfully")
        void shouldDeactivateAlertRule() throws Exception {
            doNothing().when(alertService).deactivateRule(eq("alert-123"), eq("tenant-001"));

            mockMvc.perform(post("/api/v1/monitoring/alerts/{alertId}/deactivate", "alert-123"))
                    .andExpect(status().isNoContent());

            verify(alertService).deactivateRule("alert-123", "tenant-001");
        }
    }

    @Nested
    @DisplayName("GET /api/v1/monitoring/health/{serviceName} - Get Service Health")
    class GetServiceHealthTests {

        @Test
        @DisplayName("Should return service health")
        void shouldReturnServiceHealth() throws Exception {
            when(healthService.getServiceHealth(eq("ai-model-service"), eq("tenant-001")))
                    .thenReturn(healthResponse);

            mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", "ai-model-service"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.serviceName").value("ai-model-service"))
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.successRate").value(100.0))
                    .andExpect(jsonPath("$.lastCheckAt").exists());

            verify(healthService).getServiceHealth("ai-model-service", "tenant-001");
        }

        @Test
        @DisplayName("Should accept various service names")
        void shouldAcceptVariousServiceNames() throws Exception {
            String[] services = {
                    "ai-model-service",
                    "ai-inference-service",
                    "ai-training-service",
                    "data-processing-service"
            };

            for (String service : services) {
                when(healthService.getServiceHealth(eq(service), eq("tenant-001")))
                        .thenReturn(healthResponse);

                mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", service))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should return DOWN status for unhealthy service")
        void shouldReturnDownStatusForUnhealthyService() throws Exception {
            ServiceHealthResponseDto downHealth = new ServiceHealthResponseDto(
                    "health-456",
                    "unhealthy-service",
                    "tenant-001",
                    ServiceHealth.HealthStatus.DOWN,
                    "Service is down",
                    Map.of(),
                    Instant.now(),
                    Instant.now(),
                    10L,
                    10L,
                    0.0
            );

            when(healthService.getServiceHealth(anyString(), eq("tenant-001")))
                    .thenReturn(downHealth);

            mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", "unhealthy-service"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("DOWN"));
        }

        @Test
        @DisplayName("Should return DEGRADED status for degraded service")
        void shouldReturnDegradedStatusForDegradedService() throws Exception {
            ServiceHealthResponseDto degradedHealth = new ServiceHealthResponseDto(
                    "health-789",
                    "degraded-service",
                    "tenant-001",
                    ServiceHealth.HealthStatus.DEGRADED,
                    "Service is degraded",
                    Map.of("errors", 5),
                    Instant.now(),
                    Instant.now(),
                    100L,
                    15L,
                    85.0
            );

            when(healthService.getServiceHealth(anyString(), eq("tenant-001")))
                    .thenReturn(degradedHealth);

            mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", "degraded-service"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("DEGRADED"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/monitoring/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/monitoring/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.message").value("AI Monitoring Service is running"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in service name")
        void shouldHandleSpecialCharactersInServiceName() throws Exception {
            String serviceName = "service-with_special.chars";

            when(healthService.getServiceHealth(eq(serviceName), eq("tenant-001")))
                    .thenReturn(healthResponse);

            mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", serviceName))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle UUID as alert ID")
        void shouldHandleUuidAsAlertId() throws Exception {
            String uuid = "550e8400-e29b-41d4-a716-446655440000";

            when(alertService.getRuleById(eq(uuid), eq("tenant-001")))
                    .thenReturn(alertResponse);

            mockMvc.perform(get("/api/v1/monitoring/alerts/{alertId}", uuid))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for alert response")
        void shouldReturnProperJsonStructureForAlertResponse() throws Exception {
            when(alertService.createRule(eq("tenant-001"), any()))
                    .thenReturn(alertResponse);

            mockMvc.perform(post("/api/v1/monitoring/alerts")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.alertId").exists())
                    .andExpect(jsonPath("$.tenantId").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.metric").exists())
                    .andExpect(jsonPath("$.condition").exists())
                    .andExpect(jsonPath("$.threshold").exists())
                    .andExpect(jsonPath("$.notificationChannels").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.minAlertInterval").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.updatedAt").exists());
        }

        @Test
        @DisplayName("Should return proper JSON structure for health response")
        void shouldReturnProperJsonStructureForHealthResponse() throws Exception {
            when(healthService.getServiceHealth(anyString(), eq("tenant-001")))
                    .thenReturn(healthResponse);

            mockMvc.perform(get("/api/v1/monitoring/health/{serviceName}", "test-service"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.serviceName").exists())
                    .andExpect(jsonPath("$.tenantId").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.metrics").exists())
                    .andExpect(jsonPath("$.lastCheckAt").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.totalChecks").exists())
                    .andExpect(jsonPath("$.failedChecks").exists())
                    .andExpect(jsonPath("$.successRate").exists());
        }
    }
}
