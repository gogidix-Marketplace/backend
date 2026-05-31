package com.gogidix.shared.audit.adapter.in.web;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.application.port.in.AuditEventUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock
    private AuditEventUseCase auditEventUseCase;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        AuditController controller = new AuditController(auditEventUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createAuditEvent_returnsOk() throws Exception {
        CreateAuditEventDTO request = CreateAuditEventDTO.builder()
                .userId("user1")
                .sessionId("session1")
                .action("CREATE")
                .build();

        AuditEventDTO response = AuditEventDTO.builder()
                .eventId("evt-1")
                .userId("user1")
                .action("CREATE")
                .build();

        when(auditEventUseCase.createAuditEvent(any(CreateAuditEventDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/audit/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("evt-1"))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void searchAuditEvents_returnsOk() throws Exception {
        AuditEventDTO dto = AuditEventDTO.builder()
                .eventId("evt-1")
                .userId("user1")
                .action("READ")
                .build();

        when(auditEventUseCase.searchAuditEvents(any(AuditSearchDTO.class)))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/audit/events")
                        .param("userId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventId").value("evt-1"));
    }

    @Test
    void searchAuditEvents_emptyResult() throws Exception {
        when(auditEventUseCase.searchAuditEvents(any(AuditSearchDTO.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/audit/events"))
                .andExpect(status().isOk());
    }

    @Test
    void getAuditEvent_returnsOk() throws Exception {
        AuditEventDTO dto = AuditEventDTO.builder()
                .eventId("evt-1")
                .userId("user1")
                .build();

        when(auditEventUseCase.getAuditEvent("evt-1")).thenReturn(dto);

        mockMvc.perform(get("/api/v1/audit/events/evt-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("evt-1"));
    }

    @Test
    void getAuditStatistics_returnsOk() throws Exception {
        AuditStatisticsDTO dto = AuditStatisticsDTO.builder()
                .totalEvents(100L)
                .generatedAt(LocalDateTime.now())
                .build();

        when(auditEventUseCase.getAuditStatistics()).thenReturn(dto);

        mockMvc.perform(get("/api/v1/audit/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEvents").value(100));
    }

    @Test
    void generateComplianceReport_returnsOk() throws Exception {
        ComplianceReportDTO dto = ComplianceReportDTO.builder()
                .reportId("rpt-1")
                .status("COMPLIANT")
                .generatedAt(LocalDateTime.now())
                .build();

        when(auditEventUseCase.generateComplianceReport()).thenReturn(dto);

        mockMvc.perform(get("/api/v1/audit/compliance/report"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value("rpt-1"))
                .andExpect(jsonPath("$.status").value("COMPLIANT"));
    }

    @Test
    void healthCheck_returnsOk() throws Exception {
        HealthCheckDTO dto = HealthCheckDTO.builder()
                .status("UP")
                .timestamp(LocalDateTime.now())
                .build();

        when(auditEventUseCase.performHealthCheck()).thenReturn(dto);

        mockMvc.perform(get("/api/v1/audit/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}