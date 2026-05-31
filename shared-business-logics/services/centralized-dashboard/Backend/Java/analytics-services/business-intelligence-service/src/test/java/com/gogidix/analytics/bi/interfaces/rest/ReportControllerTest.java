package com.gogidix.analytics.bi.interfaces.rest;

import com.gogidix.analytics.bi.application.service.ReportCommandService;
import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import com.gogidix.analytics.bi.domain.model.ReportExecution;
import com.gogidix.analytics.bi.domain.port.in.CreateReportCommand;
import com.gogidix.analytics.bi.domain.port.in.ExecuteReportQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Mock
    private ReportCommandService commandService;

    @InjectMocks
    private ReportController controller;

    private ReportDefinition testReport;
    private ReportExecution testExecution;
    private CreateReportCommand testCommand;

    @BeforeEach
    void setUp() {
        testReport = ReportDefinition.builder()
            .id("report-1")
            .reportName("Test Report")
            .reportType(ReportDefinition.ReportType.SUMMARY)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .build();

        testExecution = ReportExecution.builder()
            .id("exec-1")
            .status(ReportExecution.ExecutionStatus.PENDING)
            .reportDefinition(testReport)
            .build();

        testCommand = CreateReportCommand.builder()
            .reportName("Test Report")
            .reportType(ReportDefinition.ReportType.SUMMARY)
            .ownerId("user-1")
            .build();
    }

    @Nested
    @DisplayName("POST /api/v1/reports")
    class CreateReportTests {

        @Test
        void createsReportAndReturns201() {
            when(commandService.createReport(any(CreateReportCommand.class))).thenReturn(testReport);

            ResponseEntity<ReportDefinition> response = controller.createReport(testCommand);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("report-1", response.getBody().getId());
            verify(commandService).createReport(testCommand);
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/reports/{reportId}")
    class UpdateReportTests {

        @Test
        void updatesReportAndReturns200() {
            when(commandService.updateReport(eq("report-1"), any(CreateReportCommand.class))).thenReturn(testReport);

            ResponseEntity<ReportDefinition> response = controller.updateReport("report-1", testCommand);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("report-1", response.getBody().getId());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/reports/{reportId}")
    class DeleteReportTests {

        @Test
        void deletesReportAndReturnsNoContent() {
            controller.deleteReport("report-1");
            verify(commandService).deleteReport("report-1");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/reports/{reportId}/execute")
    class ExecuteReportTests {

        @Test
        void executesReportAndReturns200() {
            when(commandService.executeReport(any(ExecuteReportQuery.class))).thenReturn(testExecution);

            ResponseEntity<ReportExecution> response = controller.executeReport("report-1", null, null, true);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals("exec-1", response.getBody().getId());
        }

        @Test
        void executesReportWithDateParams() {
            when(commandService.executeReport(any(ExecuteReportQuery.class))).thenReturn(testExecution);

            java.time.LocalDateTime start = java.time.LocalDateTime.now().minusDays(7);
            java.time.LocalDateTime end = java.time.LocalDateTime.now();

            ResponseEntity<ReportExecution> response = controller.executeReport("report-1", start, end, false);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        void executesReportWithDefaultAsync() {
            when(commandService.executeReport(any(ExecuteReportQuery.class))).thenReturn(testExecution);

            ResponseEntity<ReportExecution> response = controller.executeReport("report-1", null, null, true);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/reports/executions/{executionId}/cancel")
    class CancelExecutionTests {

        @Test
        void cancelsExecution() {
            controller.cancelExecution("exec-1");
            verify(commandService).cancelExecution("exec-1");
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/reports/{reportId}/toggle")
    class ToggleReportTests {

        @Test
        void togglesReportAndReturns200() {
            when(commandService.toggleReport("report-1", true)).thenReturn(testReport);

            ResponseEntity<ReportDefinition> response = controller.toggleReport("report-1", true);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        void disablesReport() {
            testReport.setEnabled(false);
            when(commandService.toggleReport("report-1", false)).thenReturn(testReport);

            ResponseEntity<ReportDefinition> response = controller.toggleReport("report-1", false);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertFalse(response.getBody().getEnabled());
        }
    }
}
