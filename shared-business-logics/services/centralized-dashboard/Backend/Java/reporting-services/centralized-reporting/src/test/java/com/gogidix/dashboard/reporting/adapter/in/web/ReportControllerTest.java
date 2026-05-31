package com.gogidix.dashboard.reporting.adapter.in.web;

import com.gogidix.dashboard.reporting.adapter.in.web.dto.GenerateReportRequestDTO;
import com.gogidix.dashboard.reporting.adapter.in.web.dto.ScheduleReportRequestDTO;
import com.gogidix.dashboard.reporting.domain.model.*;
import com.gogidix.dashboard.reporting.domain.port.in.ReportManagementUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportManagementUseCase reportService;

    @InjectMocks
    private ReportController controller;

    @Test
    void health_returnsOk() {
        ResponseEntity<String> response = controller.health();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reporting Service is running", response.getBody());
    }

    @Test
    void generateReport_returnsOk() {
        GenerateReportRequestDTO request = new GenerateReportRequestDTO();
        request.setReportType(ReportType.KPI_ANALYTICS);
        request.setOutputFormat(OutputFormat.PDF);
        request.setDomain("order");

        Report mockReport = mock(Report.class);
        when(reportService.generateReport(any(ReportManagementUseCase.GenerateReportCommand.class)))
                .thenReturn(mockReport);

        ResponseEntity<Report> response = controller.generateReport(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getReport_found() {
        Report mockReport = mock(Report.class);
        when(reportService.getReport(any(ReportId.class)))
                .thenReturn(Optional.of(mockReport));

        ResponseEntity<Report> response = controller.getReport("report-123");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getReport_notFound() {
        when(reportService.getReport(any(ReportId.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<Report> response = controller.getReport("nonexistent");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getReportsByType_returnsOk() {
        when(reportService.getReportsByType(any(ReportType.class)))
                .thenReturn(List.of());

        ResponseEntity<List<Report>> response = controller.getReportsByType(ReportType.EXECUTIVE_SUMMARY);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getReportsByDomain_returnsOk() {
        when(reportService.getReportsByDomain("order"))
                .thenReturn(List.of());

        ResponseEntity<List<Report>> response = controller.getReportsByDomain("order");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void exportReport_returnsOk() {
        ReportExport mockExport = mock(ReportExport.class);
        when(reportService.exportReport(any(ReportId.class), any(ExportFormat.class)))
                .thenReturn(mockExport);

        ResponseEntity<ReportExport> response = controller.exportReport("r1", ExportFormat.PDF);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void scheduleReport_returnsOk() {
        ScheduleReportRequestDTO request = new ScheduleReportRequestDTO();
        request.setReportType(ReportType.KPI_ANALYTICS);
        request.setOutputFormat(OutputFormat.CSV);
        request.setCronExpression("0 0 * * *");

        ScheduledReport mockScheduled = mock(ScheduledReport.class);
        when(reportService.scheduleReport(any(ReportManagementUseCase.ScheduleReportCommand.class)))
                .thenReturn(mockScheduled);

        ResponseEntity<ScheduledReport> response = controller.scheduleReport(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getScheduledReports_returnsOk() {
        when(reportService.getScheduledReports()).thenReturn(List.of());
        ResponseEntity<List<ScheduledReport>> response = controller.getScheduledReports();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteReport_returnsOk() {
        doNothing().when(reportService).deleteReport(any(ReportId.class));
        ResponseEntity<Void> response = controller.deleteReport("r1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reportService).deleteReport(any(ReportId.class));
    }
}
