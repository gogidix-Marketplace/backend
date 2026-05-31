package com.gogidix.aiservices.aiworkflowautomationservice.interfaces.rest;

import com.gogidix.aiservices.aiworkflowautomationservice.application.service.AutomationService;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation.TriggerType;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.AutomationAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true",
    "spring.cloud.compatibility-verifier.enabled=false"
})
@WebMvcTest(controllers = AutomationController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@DisplayName("AutomationController REST API Tests")
class AutomationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutomationService automationService;

    private Automation automation;

    @BeforeEach
    void setUp() {
        automation = new Automation(
                "tenant-001",
                "Daily Data Backup",
                TriggerType.SCHEDULE,
                Collections.emptyList()
        );
        automation.setSchedule("0 0 * * *");
    }

    @Nested
    @DisplayName("POST /api/v1/automation/workflows - Create Automation")
    class CreateAutomationTests {

        @Test
        @DisplayName("Should create automation successfully")
        void shouldCreateAutomation() throws Exception {
            when(automationService.createAutomation(
                    eq("tenant-001"),
                    eq("Daily Data Backup"),
                    eq(TriggerType.SCHEDULE),
                    eq("0 0 * * *"),
                    eq(List.of())
            )).thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Daily Data Backup")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 0 * * *"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.automationId").exists())
                    .andExpect(jsonPath("$.tenantId").value("tenant-001"))
                    .andExpect(jsonPath("$.name").value("Daily Data Backup"))
                    .andExpect(jsonPath("$.triggerType").value("SCHEDULE"))
                    .andExpect(jsonPath("$.schedule").value("0 0 * * *"));

            verify(automationService).createAutomation("tenant-001", "Daily Data Backup",
                    TriggerType.SCHEDULE, "0 0 * * *", List.of());
        }

        @Test
        @DisplayName("Should accept SCHEDULE trigger type")
        void shouldAcceptScheduleTriggerType() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.SCHEDULE), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Scheduled Task")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 0 * * *"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept EVENT trigger type")
        void shouldAcceptEventTriggerType() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.EVENT), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Event Task")
                            .param("triggerType", "EVENT")
                            .param("schedule", "data.arrived"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept WEBHOOK trigger type")
        void shouldAcceptWebhookTriggerType() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.WEBHOOK), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Webhook Task")
                            .param("triggerType", "WEBHOOK")
                            .param("schedule", "https://example.com/webhook"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept EVENT trigger type")
        void shouldAcceptManualTriggerType() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.EVENT), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Manual Task")
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/automation/workflows/{automationId} - Get Automation")
    class GetAutomationTests {

        @Test
        @DisplayName("Should return automation by ID")
        void shouldReturnAutomationById() throws Exception {
            String automationId = automation.getAutomationId();

            when(automationService.getAutomationById(eq(automationId), eq("tenant-001")))
                    .thenReturn(automation);

            mockMvc.perform(get("/api/v1/automation/workflows/{automationId}", automationId)
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.automationId").exists())
                    .andExpect(jsonPath("$.name").value("Daily Data Backup"));

            verify(automationService).getAutomationById(automationId, "tenant-001");
        }

        @Test
        @DisplayName("Should handle UUID as automation ID")
        void shouldHandleUuidAsAutomationId() throws Exception {
            String uuid = UUID.randomUUID().toString();
            when(automationService.getAutomationById(eq(uuid), eq("tenant-001")))
                    .thenReturn(automation);

            mockMvc.perform(get("/api/v1/automation/workflows/{automationId}", uuid)
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle special characters in automation ID")
        void shouldHandleSpecialCharactersInAutomationId() throws Exception {
            when(automationService.getAutomationById(anyString(), eq("tenant-001")))
                    .thenReturn(automation);

            mockMvc.perform(get("/api/v1/automation/workflows/{automationId}", "automation-with_special.chars")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/automation/workflows - List Automations")
    class ListAutomationsTests {

        @Test
        @DisplayName("Should return list of automations")
        void shouldReturnListOfAutomations() throws Exception {
            List<Automation> automations = Arrays.asList(automation);

            when(automationService.getAutomationsByTenant("tenant-001"))
                    .thenReturn(automations);

            mockMvc.perform(get("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].automationId").exists())
                    .andExpect(jsonPath("$[0].name").value("Daily Data Backup"));

            verify(automationService).getAutomationsByTenant("tenant-001");
        }

        @Test
        @DisplayName("Should return empty list when no automations")
        void shouldReturnEmptyList() throws Exception {
            when(automationService.getAutomationsByTenant("tenant-001"))
                    .thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should return multiple automations")
        void shouldReturnMultipleAutomations() throws Exception {
            Automation automation2 = new Automation(
                    "tenant-001",
                    "Weekly Report",
                    TriggerType.SCHEDULE,
                    Collections.emptyList()
            );

            List<Automation> automations = Arrays.asList(automation, automation2);

            when(automationService.getAutomationsByTenant("tenant-001"))
                    .thenReturn(automations);

            mockMvc.perform(get("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/automation/workflows/{automationId}/trigger - Trigger Automation")
    class TriggerAutomationTests {

        @Test
        @DisplayName("Should trigger automation successfully")
        void shouldTriggerAutomation() throws Exception {
            doNothing().when(automationService).triggerAutomation(eq("auto-123"), eq("tenant-001"));

            mockMvc.perform(post("/api/v1/automation/workflows/{automationId}/trigger", "auto-123")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isNoContent());

            verify(automationService).triggerAutomation("auto-123", "tenant-001");
        }

        @Test
        @DisplayName("Should trigger with UUID")
        void shouldTriggerWithUuid() throws Exception {
            String uuid = UUID.randomUUID().toString();
            doNothing().when(automationService).triggerAutomation(eq(uuid), eq("tenant-001"));

            mockMvc.perform(post("/api/v1/automation/workflows/{automationId}/trigger", uuid)
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isNoContent());

            verify(automationService).triggerAutomation(uuid, "tenant-001");
        }

        @Test
        @DisplayName("Should trigger SCHEDULE automation")
        void shouldTriggerScheduleAutomation() throws Exception {
            Automation scheduledAutomation = new Automation(
                    "tenant-001",
                    "Scheduled Task",
                    TriggerType.SCHEDULE,
                    Collections.emptyList()
            );

            doNothing().when(automationService).triggerAutomation(anyString(), anyString());

            mockMvc.perform(post("/api/v1/automation/workflows/{automationId}/trigger", "auto-123")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should trigger EVENT automation")
        void shouldTriggerEventAutomation() throws Exception {
            Automation eventAutomation = new Automation(
                    "tenant-001",
                    "Event Task",
                    TriggerType.EVENT,
                    Collections.emptyList()
            );

            doNothing().when(automationService).triggerAutomation(anyString(), anyString());

            mockMvc.perform(post("/api/v1/automation/workflows/{automationId}/trigger", "auto-456")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should trigger EVENT automation")
        void shouldTriggerManualAutomation() throws Exception {
            Automation manualAutomation = new Automation(
                    "tenant-001",
                    "Manual Task",
                    TriggerType.EVENT,
                    Collections.emptyList()
            );

            doNothing().when(automationService).triggerAutomation(anyString(), anyString());

            mockMvc.perform(post("/api/v1/automation/workflows/{automationId}/trigger", "auto-789")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/automation/health - Health Check")
    class HealthCheckTests {

        @Test
        @DisplayName("Should return health status")
        void shouldReturnHealthStatus() throws Exception {
            mockMvc.perform(get("/api/v1/automation/health"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("UP"))
                    .andExpect(jsonPath("$.message").value("AI Workflow Automation Service is running"));
        }
    }

    @Nested
    @DisplayName("TriggerType Enum Tests")
    class TriggerTypeTests {

        @ParameterizedTest
        @EnumSource(TriggerType.class)
        @DisplayName("Should handle all TriggerType values")
        void shouldHandleAllTriggerTypeValues(TriggerType triggerType) throws Exception {
            when(automationService.createAutomation(
                    eq("tenant-001"),
                    anyString(),
                    eq(triggerType),
                    anyString(),
                    eq(List.of())
            )).thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Test Automation")
                            .param("triggerType", triggerType.name())
                            .param("schedule", "schedule"))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Schedule Format Tests")
    class ScheduleFormatTests {

        @Test
        @DisplayName("Should accept daily cron schedule")
        void shouldAcceptDailyCronSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), eq("0 0 * * *"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Daily Task")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 0 * * *"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept hourly cron schedule")
        void shouldAcceptHourlyCronSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), eq("0 * * * *"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Hourly Task")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 * * * *"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept weekly cron schedule")
        void shouldAcceptWeeklyCronSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), eq("0 9 * * 1"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Weekly Task")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 9 * * 1"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept monthly cron schedule")
        void shouldAcceptMonthlyCronSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), eq("0 0 1 * *"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Monthly Task")
                            .param("triggerType", "SCHEDULE")
                            .param("schedule", "0 0 1 * *"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept event-based schedule")
        void shouldAcceptEventBasedSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.EVENT), eq("user.registered"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Event Task")
                            .param("triggerType", "EVENT")
                            .param("schedule", "user.registered"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept webhook URL")
        void shouldAcceptWebhookUrl() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.WEBHOOK), eq("https://example.com/webhook"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Webhook Task")
                            .param("triggerType", "WEBHOOK")
                            .param("schedule", "https://example.com/webhook"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should accept empty schedule for EVENT trigger")
        void shouldAcceptEmptyScheduleForManual() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.EVENT), eq(""), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Manual Task")
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in automation name")
        void shouldHandleUnicodeInAutomationName() throws Exception {
            when(automationService.createAutomation(anyString(), eq("自動化"), any(TriggerType.class), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "自動化")
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle very long automation name")
        void shouldHandleVeryLongAutomationName() throws Exception {
            String longName = "a".repeat(200);

            when(automationService.createAutomation(anyString(), eq(longName), any(TriggerType.class), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", longName)
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle special characters in schedule")
        void shouldHandleSpecialCharactersInSchedule() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), eq("event:file.uploaded"), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Event Task")
                            .param("triggerType", "EVENT")
                            .param("schedule", "event:file.uploaded"))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Should handle URL in schedule for webhook")
        void shouldHandleUrlInScheduleForWebhook() throws Exception {
            String webhookUrl = "https://api.example.com/webhook/automation";

            when(automationService.createAutomation(anyString(), anyString(), eq(TriggerType.WEBHOOK), eq(webhookUrl), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Webhook Task")
                            .param("triggerType", "WEBHOOK")
                            .param("schedule", webhookUrl))
                    .andExpect(status().isCreated());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for automation")
        void shouldReturnProperJsonStructureForAutomation() throws Exception {
            when(automationService.createAutomation(anyString(), anyString(), any(TriggerType.class), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001")
                            .param("name", "Test Automation")
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.automationId").exists())
                    .andExpect(jsonPath("$.tenantId").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.triggerType").exists())
                    .andExpect(jsonPath("$.schedule").exists())
                    .andExpect(jsonPath("$.actions").exists());
        }

        @Test
        @DisplayName("Should return proper JSON structure for automation list")
        void shouldReturnProperJsonStructureForList() throws Exception {
            when(automationService.getAutomationsByTenant(anyString()))
                    .thenReturn(Arrays.asList(automation));

            mockMvc.perform(get("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].automationId").exists())
                    .andExpect(jsonPath("$[0].tenantId").exists())
                    .andExpect(jsonPath("$[0].name").exists());
        }
    }

    @Nested
    @DisplayName("Tenant ID Tests")
    class TenantIdTests {

        @Test
        @DisplayName("Should accept tenant ID in create")
        void shouldAcceptTenantIdInCreate() throws Exception {
            when(automationService.createAutomation(eq("tenant-xyz"), anyString(), any(TriggerType.class), anyString(), eq(List.of())))
                    .thenReturn(automation);

            mockMvc.perform(post("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-xyz")
                            .param("name", "Test")
                            .param("triggerType", "EVENT")
                            .param("schedule", ""))
                    .andExpect(status().isCreated());

            verify(automationService).createAutomation("tenant-xyz", "Test", TriggerType.EVENT, "", List.of());
        }

        @Test
        @DisplayName("Should accept tenant ID in get")
        void shouldAcceptTenantIdInGet() throws Exception {
            when(automationService.getAutomationById(anyString(), eq("tenant-xyz")))
                    .thenReturn(automation);

            mockMvc.perform(get("/api/v1/automation/workflows/{automationId}", "auto-123")
                            .param("tenantId", "tenant-xyz"))
                    .andExpect(status().isOk());

            verify(automationService).getAutomationById("auto-123", "tenant-xyz");
        }

        @Test
        @DisplayName("Should accept tenant ID in list")
        void shouldAcceptTenantIdInList() throws Exception {
            when(automationService.getAutomationsByTenant(eq("tenant-xyz")))
                    .thenReturn(Arrays.asList(automation));

            mockMvc.perform(get("/api/v1/automation/workflows")
                            .param("tenantId", "tenant-xyz"))
                    .andExpect(status().isOk());

            verify(automationService).getAutomationsByTenant("tenant-xyz");
        }
    }
}
