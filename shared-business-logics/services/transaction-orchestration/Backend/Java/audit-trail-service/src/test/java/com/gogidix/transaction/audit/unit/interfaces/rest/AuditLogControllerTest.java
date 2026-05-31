package com.gogidix.transaction.audit.unit.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.audit.application.dto.request.CreateAuditLogRequestDto;
import com.gogidix.transaction.audit.application.dto.response.AuditLogResponseDto;
import com.gogidix.transaction.audit.application.dto.response.PagedAuditLogsResponseDto;
import com.gogidix.transaction.audit.application.service.AuditLogCommandService;
import com.gogidix.transaction.audit.application.service.AuditLogQueryService;
import com.gogidix.transaction.audit.interfaces.rest.AuditLogController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuditLogController.class)
@Disabled("Requires Spring context with DataSource/Redis")
@DisplayName("AuditLogController Unit Tests")
class AuditLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuditLogCommandService commandService;

    @MockBean
    private AuditLogQueryService queryService;

    private UUID auditLogId;
    private AuditLogResponseDto auditLogResponseDto;
    private CreateAuditLogRequestDto createRequestDto;

    @BeforeEach
    void setUp() {
        auditLogId = UUID.randomUUID();

        auditLogResponseDto = AuditLogResponseDto.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .timestamp(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        createRequestDto = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .description("Transaction created")
                .build();
    }

    @Test
    @DisplayName("Should create audit log successfully")
    void shouldCreateAuditLogSuccessfully() throws Exception {
        // Given
        when(commandService.createAuditLog(any(CreateAuditLogRequestDto.class)))
                .thenReturn(auditLogResponseDto);

        // When & Then
        mockMvc.perform(post("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(auditLogId.toString()))
                .andExpect(jsonPath("$.tenantId").value("tenant-001"))
                .andExpect(jsonPath("$.entityType").value("Transaction"))
                .andExpect(jsonPath("$.entityId").value("txn-12345"))
                .andExpect(jsonPath("$.action").value("CREATE"));

        verify(commandService).createAuditLog(any(CreateAuditLogRequestDto.class));
    }

    @Test
    @DisplayName("Should return 400 for invalid create request")
    void shouldReturn400ForInvalidCreateRequest() throws Exception {
        // Given - invalid request missing required fields
        CreateAuditLogRequestDto invalidRequest = CreateAuditLogRequestDto.builder()
                .tenantId("") // Invalid: blank
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(commandService, never()).createAuditLog(any());
    }

    @Test
    @DisplayName("Should get audit log by ID")
    void shouldGetAuditLogById() throws Exception {
        // Given
        when(queryService.getAuditLog(any())).thenReturn(auditLogResponseDto);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/{auditLogId}", auditLogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(auditLogId.toString()))
                .andExpect(jsonPath("$.tenantId").value("tenant-001"))
                .andExpect(jsonPath("$.entityType").value("Transaction"));

        verify(queryService).getAuditLog(any());
    }

    @Test
    @DisplayName("Should search audit logs")
    void shouldSearchAuditLogs() throws Exception {
        // Given
        List<AuditLogResponseDto> auditLogs = Arrays.asList(auditLogResponseDto);
        PagedAuditLogsResponseDto pagedResponse = PagedAuditLogsResponseDto.builder()
                .auditLogs(auditLogs)
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(queryService.searchAuditLogs(any())).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs")
                        .param("tenantId", "tenant-001")
                        .param("entityType", "Transaction")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auditLogs").isArray())
                .andExpect(jsonPath("$.auditLogs.length()").value(1))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.hasNext").value(false))
                .andExpect(jsonPath("$.hasPrevious").value(false));

        verify(queryService).searchAuditLogs(any());
    }

    @Test
    @DisplayName("Should get audit logs by entity")
    void shouldGetAuditLogsByEntity() throws Exception {
        // Given
        List<AuditLogResponseDto> auditLogs = Arrays.asList(auditLogResponseDto);
        when(queryService.getAuditLogsByEntity("Transaction", "txn-12345"))
                .thenReturn(auditLogs);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/by-entity/{entityType}/{entityId}",
                        "Transaction", "txn-12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].entityType").value("Transaction"))
                .andExpect(jsonPath("$[0].entityId").value("txn-12345"));

        verify(queryService).getAuditLogsByEntity("Transaction", "txn-12345");
    }

    @Test
    @DisplayName("Should get audit logs by tenant and entity")
    void shouldGetAuditLogsByTenantAndEntity() throws Exception {
        // Given
        List<AuditLogResponseDto> auditLogs = Arrays.asList(auditLogResponseDto);
        when(queryService.getAuditLogsByTenantAndEntity("tenant-001", "Transaction", "txn-12345"))
                .thenReturn(auditLogs);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/by-tenant/{tenantId}/{entityType}/{entityId}",
                        "tenant-001", "Transaction", "txn-12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tenantId").value("tenant-001"))
                .andExpect(jsonPath("$[0].entityType").value("Transaction"))
                .andExpect(jsonPath("$[0].entityId").value("txn-12345"));

        verify(queryService).getAuditLogsByTenantAndEntity("tenant-001", "Transaction", "txn-12345");
    }

    @Test
    @DisplayName("Should get audit logs by correlation ID")
    void shouldGetAuditLogsByCorrelationId() throws Exception {
        // Given
        String correlationId = "corr-001";
        List<AuditLogResponseDto> auditLogs = Arrays.asList(auditLogResponseDto);
        when(queryService.getAuditLogsByCorrelationId(correlationId))
                .thenReturn(auditLogs);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/by-correlation/{correlationId}", correlationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(queryService).getAuditLogsByCorrelationId(correlationId);
    }

    @Test
    @DisplayName("Should get audit logs by actor")
    void shouldGetAuditLogsByActor() throws Exception {
        // Given
        String actorId = "user-001";
        List<AuditLogResponseDto> auditLogs = Arrays.asList(auditLogResponseDto);
        when(queryService.getAuditLogsByActor(actorId))
                .thenReturn(auditLogs);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/by-actor/{actorId}", actorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].actorId").value(actorId));

        verify(queryService).getAuditLogsByActor(actorId);
    }

    @Test
    @DisplayName("Should delete audit log")
    void shouldDeleteAuditLog() throws Exception {
        // Given
        doNothing().when(commandService).deleteAuditLog(auditLogId);

        // When & Then
        mockMvc.perform(delete("/api/v1/audit-logs/{auditLogId}", auditLogId))
                .andExpect(status().isNoContent());

        verify(commandService).deleteAuditLog(auditLogId);
    }

    @Test
    @DisplayName("Should return empty list when no audit logs found")
    void shouldReturnEmptyListWhenNoAuditLogsFound() throws Exception {
        // Given
        when(queryService.getAuditLogsByEntity("NonExistent", "id-999"))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs/by-entity/{entityType}/{entityId}",
                        "NonExistent", "id-999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(queryService).getAuditLogsByEntity("NonExistent", "id-999");
    }

    @Test
    @DisplayName("Should handle pagination parameters")
    void shouldHandlePaginationParameters() throws Exception {
        // Given
        PagedAuditLogsResponseDto pagedResponse = PagedAuditLogsResponseDto.builder()
                .auditLogs(Collections.emptyList())
                .page(1)
                .size(50)
                .totalElements(0L)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(queryService.searchAuditLogs(any())).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs")
                        .param("page", "1")
                        .param("size", "50")
                        .param("sortBy", "timestamp")
                        .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(50));

        verify(queryService).searchAuditLogs(any());
    }

    @Test
    @DisplayName("Should handle date range filters")
    void shouldHandleDateRangeFilters() throws Exception {
        // Given
        PagedAuditLogsResponseDto pagedResponse = PagedAuditLogsResponseDto.builder()
                .auditLogs(Collections.emptyList())
                .page(0)
                .size(20)
                .totalElements(0L)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(queryService.searchAuditLogs(any())).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs")
                        .param("startDate", "2024-01-01T00:00:00")
                        .param("endDate", "2024-12-31T23:59:59"))
                .andExpect(status().isOk());

        verify(queryService).searchAuditLogs(any());
    }

    @Test
    @DisplayName("Should handle all filter parameters")
    void shouldHandleAllFilterParameters() throws Exception {
        // Given
        PagedAuditLogsResponseDto pagedResponse = PagedAuditLogsResponseDto.builder()
                .auditLogs(Arrays.asList(auditLogResponseDto))
                .page(0)
                .size(20)
                .totalElements(1L)
                .totalPages(1)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(queryService.searchAuditLogs(any())).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs")
                        .param("tenantId", "tenant-001")
                        .param("entityType", "Transaction")
                        .param("entityId", "txn-12345")
                        .param("action", "CREATE")
                        .param("actorId", "user-001")
                        .param("severity", "INFO")
                        .param("category", "BUSINESS")
                        .param("status", "SUCCESS")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk());

        verify(queryService).searchAuditLogs(any());
    }

    @Test
    @DisplayName("Should use default pagination values")
    void shouldUseDefaultPaginationValues() throws Exception {
        // Given
        PagedAuditLogsResponseDto pagedResponse = PagedAuditLogsResponseDto.builder()
                .auditLogs(Collections.emptyList())
                .page(0)
                .size(20)
                .totalElements(0L)
                .totalPages(0)
                .hasNext(false)
                .hasPrevious(false)
                .build();

        when(queryService.searchAuditLogs(any())).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/audit-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(20));

        verify(queryService).searchAuditLogs(any());
    }

    @Test
    @DisplayName("Should create audit log with all optional fields")
    void shouldCreateAuditLogWithAllOptionalFields() throws Exception {
        // Given
        CreateAuditLogRequestDto fullRequest = CreateAuditLogRequestDto.builder()
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .actorId("system")
                .actorType("SERVICE")
                .ipAddress("10.0.0.1")
                .userAgent("PaymentService/1.0")
                .correlationId("corr-001")
                .oldState("{\"status\":\"PENDING\"}")
                .newState("{\"status\":\"COMPLETED\"}")
                .changedFields("[\"status\"]")
                .metadata("{\"source\":\"api\"}")
                .businessContext("{\"source\":\"API\"}")
                .severity("INFO")
                .category("BUSINESS")
                .description("Payment processed")
                .status("SUCCESS")
                .errorMessage(null)
                .sessionId("sess-001")
                .requestId("req-001")
                .build();

        AuditLogResponseDto fullResponse = AuditLogResponseDto.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .actorId("system")
                .actorType("SERVICE")
                .ipAddress("10.0.0.1")
                .userAgent("PaymentService/1.0")
                .correlationId("corr-001")
                .oldState("{\"status\":\"PENDING\"}")
                .newState("{\"status\":\"COMPLETED\"}")
                .changedFields("[\"status\"]")
                .metadata("{\"source\":\"api\"}")
                .businessContext("{\"source\":\"API\"}")
                .severity("INFO")
                .category("BUSINESS")
                .description("Payment processed")
                .status("SUCCESS")
                .errorMessage(null)
                .sessionId("sess-001")
                .requestId("req-001")
                .build();

        when(commandService.createAuditLog(any(CreateAuditLogRequestDto.class)))
                .thenReturn(fullResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fullRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.entityType").value("Payment"))
                .andExpect(jsonPath("$.correlationId").value("corr-001"))
                .andExpect(jsonPath("$.ipAddress").value("10.0.0.1"));

        verify(commandService).createAuditLog(any(CreateAuditLogRequestDto.class));
    }
}
