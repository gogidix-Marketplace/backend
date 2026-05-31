package com.gogidix.aiservices.aireporting.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aireporting.application.dto.request.GenerateReportRequest;
import com.gogidix.aiservices.aireporting.application.dto.response.ReportResponse;
import com.gogidix.aiservices.aireporting.application.service.ReportService;
import com.gogidix.aiservices.aireporting.domain.model.ExportFormat;
import com.gogidix.aiservices.aireporting.domain.model.ReportType;
import com.gogidix.aiservices.aireporting.shared.exception.ReportNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@DisplayName("Report Controller Interface Tests")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @Test
    @DisplayName("POST /api/v1/reports/generate - Should generate report")
    void shouldGenerateReport() throws Exception {
        GenerateReportRequest request = GenerateReportRequest.builder()
                .type(ReportType.SUMMARY)
                .format(ExportFormat.PDF)
                .build();

        ReportResponse response = ReportResponse.builder()
                .reportId("report-1")
                .status("PROCESSING")
                .build();

        when(reportService.generateReport(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/reports/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.reportId").exists());

        verify(reportService).generateReport(any());
    }

    @Test
    @DisplayName("GET /api/v1/reports/{id}/status - Should get report status")
    void shouldGetReportStatus() throws Exception {
        when(reportService.getReportStatus("report-1")).thenReturn("PROCESSING");

        mockMvc.perform(get("/api/v1/reports/{id}/status", "report-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

    @Test
    @DisplayName("Should handle report not found")
    void shouldHandleNotFound() throws Exception {
        when(reportService.getReport("report-1"))
                .thenThrow(new ReportNotFoundException("report-1"));

        mockMvc.perform(get("/api/v1/reports/{id}", "report-1"))
                .andExpect(status().isNotFound());
    }
}
