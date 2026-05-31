package com.gogidix.centralconfiguration.configauditservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gogidix.centralconfiguration.configauditservice.application.service.ConfigAuditService;
import com.gogidix.centralconfiguration.configauditservice.domain.model.AuditAction;
import com.gogidix.centralconfiguration.configauditservice.domain.model.ConfigAuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigAuditController Tests")
class ConfigAuditControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Mock
    private ConfigAuditService auditService;

    @InjectMocks
    private ConfigAuditController configAuditController;

    private ConfigAuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(configAuditController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        testAuditLog = ConfigAuditLog.builder()
                .id(1L)
                .tenantId("tenant-1")
                .entityType("CONFIGURATION")
                .entityId("config-123")
                .action(AuditAction.UPDATE)
                .oldValue("old-value")
                .newValue("new-value")
                .changedBy("admin")
                .userId("user-1")
                .userName("Admin User")
                .userEmail("admin@example.com")
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .changeReason("Configuration update")
                .metadata("{\"key\":\"value\"}")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs - Should get all audit logs")
    void getAuditLogs_Success() throws Exception {
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("FEATURE_FLAG")
                        .action(AuditAction.CREATE)
                        .build()
        );

        when(auditService.getAuditLogs("tenant-1")).thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].entityType").value("CONFIGURATION"))
                .andExpect(jsonPath("$[1].entityType").value("FEATURE_FLAG"));

        verify(auditService).getAuditLogs("tenant-1");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs - Should use default tenant when header not provided")
    void getAuditLogs_NoTenantHeader_UsesDefault() throws Exception {
        when(auditService.getAuditLogs("default")).thenReturn(List.of(testAuditLog));

        mockMvc.perform(get("/api/v1/audit-logs"))
                .andExpect(status().isOk());

        verify(auditService).getAuditLogs("default");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-entity-type - Should get logs by entity type")
    void getAuditLogsByEntityType_Success() throws Exception {
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("CONFIGURATION")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditService.getAuditLogsByEntityType("tenant-1", "CONFIGURATION")).thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/by-entity-type")
                        .param("entityType", "CONFIGURATION")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].entityType").value("CONFIGURATION"))
                .andExpect(jsonPath("$[1].entityType").value("CONFIGURATION"));

        verify(auditService).getAuditLogsByEntityType("tenant-1", "CONFIGURATION");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-entity - Should get logs by entity")
    void getAuditLogsByEntity_Success() throws Exception {
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .entityType("CONFIGURATION")
                        .entityId("config-123")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditService.getAuditLogsByEntity("tenant-1", "CONFIGURATION", "config-123"))
                .thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/by-entity")
                        .param("entityType", "CONFIGURATION")
                        .param("entityId", "config-123")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].entityId").value("config-123"))
                .andExpect(jsonPath("$[1].entityId").value("config-123"));

        verify(auditService).getAuditLogsByEntity("tenant-1", "CONFIGURATION", "config-123");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-date-range - Should get logs by date range")
    void getAuditLogsByDateRange_Success() throws Exception {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .action(AuditAction.CREATE)
                        .build()
        );

        when(auditService.getAuditLogsByDateRange(eq("tenant-1"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/by-date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString())
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(auditService).getAuditLogsByDateRange(eq("tenant-1"), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-user - Should get logs by user")
    void getAuditLogsByUser_Success() throws Exception {
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-2")
                        .changedBy("admin")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditService.getAuditLogsByUser("admin")).thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/by-user")
                        .param("userId", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].changedBy").value("admin"))
                .andExpect(jsonPath("$[1].changedBy").value("admin"));

        verify(auditService).getAuditLogsByUser("admin");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-entity-type - Should handle different entity types")
    void getAuditLogsByEntityType_DifferentTypes() throws Exception {
        when(auditService.getAuditLogsByEntityType("tenant-1", "FEATURE_FLAG"))
                .thenReturn(List.of(
                        ConfigAuditLog.builder()
                                .id(2L)
                                .tenantId("tenant-1")
                                .entityType("FEATURE_FLAG")
                                .action(AuditAction.ROLLOUT)
                                .build()
                ));

        mockMvc.perform(get("/api/v1/audit-logs/by-entity-type")
                        .param("entityType", "FEATURE_FLAG")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entityType").value("FEATURE_FLAG"));

        verify(auditService).getAuditLogsByEntityType("tenant-1", "FEATURE_FLAG");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs - Should return empty list when no logs")
    void getAuditLogs_EmptyList() throws Exception {
        when(auditService.getAuditLogs("tenant-1")).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/audit-logs")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs - Should include all audit log fields")
    void getAuditLogs_IncludesAllFields() throws Exception {
        when(auditService.getAuditLogs("tenant-1")).thenReturn(List.of(testAuditLog));

        mockMvc.perform(get("/api/v1/audit-logs")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tenantId").value("tenant-1"))
                .andExpect(jsonPath("$[0].entityType").value("CONFIGURATION"))
                .andExpect(jsonPath("$[0].entityId").value("config-123"))
                .andExpect(jsonPath("$[0].action").value("UPDATE"))
                .andExpect(jsonPath("$[0].oldValue").value("old-value"))
                .andExpect(jsonPath("$[0].newValue").value("new-value"))
                .andExpect(jsonPath("$[0].changedBy").value("admin"))
                .andExpect(jsonPath("$[0].userId").value("user-1"))
                .andExpect(jsonPath("$[0].userName").value("Admin User"))
                .andExpect(jsonPath("$[0].userEmail").value("admin@example.com"))
                .andExpect(jsonPath("$[0].ipAddress").value("192.168.1.1"))
                .andExpect(jsonPath("$[0].changeReason").value("Configuration update"));
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-entity - Should validate entity type and ID params")
    void getAuditLogsByEntity_RequiresParams() throws Exception {
        mockMvc.perform(get("/api/v1/audit-logs/by-entity")
                        .param("entityId", "config-123")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-user - Should return logs across tenants")
    void getAuditLogsByUser_MultipleTenants() throws Exception {
        List<ConfigAuditLog> logs = Arrays.asList(
                testAuditLog,
                ConfigAuditLog.builder()
                        .id(2L)
                        .tenantId("tenant-2")
                        .changedBy("admin")
                        .action(AuditAction.CREATE)
                        .build(),
                ConfigAuditLog.builder()
                        .id(3L)
                        .tenantId("tenant-3")
                        .changedBy("admin")
                        .action(AuditAction.DELETE)
                        .build()
        );

        when(auditService.getAuditLogsByUser("admin")).thenReturn(logs);

        mockMvc.perform(get("/api/v1/audit-logs/by-user")
                        .param("userId", "admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        verify(auditService).getAuditLogsByUser("admin");
    }

    @Test
    @DisplayName("GET /api/v1/audit-logs/by-entity-type - Should support ENVIRONMENT entity type")
    void getAuditLogsByEntityType_EnvironmentType() throws Exception {
        when(auditService.getAuditLogsByEntityType("tenant-1", "ENVIRONMENT"))
                .thenReturn(List.of(
                        ConfigAuditLog.builder()
                                .id(3L)
                                .tenantId("tenant-1")
                                .entityType("ENVIRONMENT")
                                .action(AuditAction.UPDATE)
                                .build()
                ));

        mockMvc.perform(get("/api/v1/audit-logs/by-entity-type")
                        .param("entityType", "ENVIRONMENT")
                        .header("X-Tenant-ID", "tenant-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].entityType").value("ENVIRONMENT"));
    }
}
