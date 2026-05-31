package com.gogidix.analytics.bi.application.service;

import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import com.gogidix.analytics.bi.domain.model.ReportExecution;
import com.gogidix.analytics.bi.domain.port.in.CreateReportCommand;
import com.gogidix.analytics.bi.domain.port.in.ExecuteReportQuery;
import com.gogidix.analytics.bi.domain.port.out.ReportNotificationGateway;
import com.gogidix.analytics.bi.domain.repository.ReportDefinitionRepository;
import com.gogidix.analytics.bi.domain.repository.ReportExecutionRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportCommandServiceTest {

    @Mock
    private ReportDefinitionRepository reportRepository;

    @Mock
    private ReportExecutionRepository executionRepository;

    @Mock
    private ReportNotificationGateway notificationGateway;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private ReportCommandService service;

    private MockedStatic<RequestContext> requestContextMock;

    @BeforeEach
    void setUp() {
        requestContextMock = mockStatic(RequestContext.class);
    }

    @AfterEach
    void tearDown() {
        requestContextMock.close();
    }

    private void setTenantId(String tenantId) {
        requestContextMock.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(tenantId);
    }

    private ReportDefinition createTestReport() {
        return ReportDefinition.builder()
            .id("report-1")
            .reportName("Test Report")
            .description("A test report")
            .reportType(ReportDefinition.ReportType.SUMMARY)
            .scheduleType(ReportDefinition.ScheduleType.DAILY)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .enabled(true)
            .executions(new ArrayList<>())
            .build();
    }

    private CreateReportCommand createTestCommand() {
        return CreateReportCommand.builder()
            .reportName("New Report")
            .description("A new report")
            .reportType(ReportDefinition.ReportType.DETAILED)
            .scheduleType(ReportDefinition.ScheduleType.WEEKLY)
            .scheduleConfig("{\"cron\":\"0 0 * * 1\"}")
            .dataSource("{\"db\":\"main\"}")
            .queryDefinition("SELECT * FROM data")
            .outputFormat(ReportDefinition.OutputFormat.EXCEL)
            .templateConfig("{\"template\":\"standard\"}")
            .recipients("admin@test.com")
            .ownerId("user-1")
            .enabled(true)
            .build();
    }

    @Nested
    @DisplayName("createReport Tests")
    class CreateReportTests {

        @Test
        void createsReportSuccessfully() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createTestCommand());

            assertNotNull(result);
            assertEquals("New Report", result.getReportName());
            assertEquals("tenant-1", result.getTenantId());
            verify(auditService).logEvent(eq("REPORT_DEFINITION_CREATED"), eq("ReportDefinition"), any(), contains("Created report"));
        }

        @Test
        void createsReportWithScheduleSetsNextRunTime() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setScheduleType(ReportDefinition.ScheduleType.HOURLY);
            cmd.setEnabled(true);

            ReportDefinition result = service.createReport(cmd);
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithManualScheduleDoesNotSetNextRunTime() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setScheduleType(null);

            ReportDefinition result = service.createReport(cmd);
            assertNull(result.getNextRunAt());
        }

        @Test
        void createsReportDisabledDoesNotSetNextRunTime() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setEnabled(false);

            ReportDefinition result = service.createReport(cmd);
            assertNull(result.getNextRunAt());
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.createReport(createTestCommand()));
        }
    }

    @Nested
    @DisplayName("updateReport Tests")
    class UpdateReportTests {

        @Test
        void updatesReportSuccessfully() {
            setTenantId("tenant-1");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));
            when(reportRepository.save(any(ReportDefinition.class))).thenReturn(existing);

            ReportDefinition result = service.updateReport("report-1", createTestCommand());

            assertNotNull(result);
            assertEquals("New Report", result.getReportName());
            verify(auditService).logEvent(eq("REPORT_DEFINITION_UPDATED"), eq("ReportDefinition"), eq("report-1"), anyString());
        }

        @Test
        void throwsWhenReportNotFound() {
            setTenantId("tenant-1");
            when(reportRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.updateReport("nonexistent", createTestCommand()));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class, () -> service.updateReport("report-1", createTestCommand()));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.updateReport("report-1", createTestCommand()));
        }

        @Test
        void updateWithScheduleAndEnabledSetsNextRunTime() {
            setTenantId("tenant-1");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setScheduleType(ReportDefinition.ScheduleType.MONTHLY);
            cmd.setEnabled(true);

            ReportDefinition result = service.updateReport("report-1", cmd);
            assertNotNull(result.getNextRunAt());
        }
    }

    @Nested
    @DisplayName("deleteReport Tests")
    class DeleteReportTests {

        @Test
        void deletesReportSuccessfully() {
            setTenantId("tenant-1");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));

            service.deleteReport("report-1");

            verify(reportRepository).delete(existing);
            verify(auditService).logEvent(eq("REPORT_DEFINITION_DELETED"), eq("ReportDefinition"), eq("report-1"), anyString());
        }

        @Test
        void throwsWhenReportNotFound() {
            setTenantId("tenant-1");
            when(reportRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.deleteReport("nonexistent"));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));

            assertThrows(ValidationException.class, () -> service.deleteReport("report-1"));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.deleteReport("report-1"));
        }
    }

    @Nested
    @DisplayName("executeReport Tests")
    class ExecuteReportTests {

        @Test
        void executesReportSynchronously() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(executionRepository.save(any(ReportExecution.class))).thenAnswer(inv -> inv.getArgument(0));

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(false)
                .requestedBy("user-1")
                .outputFormat(ReportDefinition.OutputFormat.PDF)
                .build();

            ReportExecution result = service.executeReport(query);

            assertNotNull(result);
            assertEquals(report, result.getReportDefinition());
        }

        @Test
        void executesReportAsynchronously() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(executionRepository.save(any(ReportExecution.class))).thenAnswer(inv -> inv.getArgument(0));

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(true)
                .requestedBy("user-1")
                .build();

            ReportExecution result = service.executeReport(query);
            assertNotNull(result);
        }

        @Test
        void throwsWhenReportNotFound() {
            setTenantId("tenant-1");
            when(reportRepository.findById("nonexistent")).thenReturn(Optional.empty());

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("nonexistent")
                .async(false)
                .build();

            assertThrows(NotFoundException.class, () -> service.executeReport(query));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            ReportDefinition report = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(false)
                .build();

            assertThrows(ValidationException.class, () -> service.executeReport(query));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(false)
                .build();

            assertThrows(ValidationException.class, () -> service.executeReport(query));
        }
    }

    @Nested
    @DisplayName("cancelExecution Tests")
    class CancelExecutionTests {

        @Test
        void cancelsPendingExecution() {
            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.PENDING)
                .reportDefinition(createTestReport())
                .build();
            when(executionRepository.findById("exec-1")).thenReturn(Optional.of(execution));
            when(executionRepository.save(any(ReportExecution.class))).thenReturn(execution);

            service.cancelExecution("exec-1");

            assertEquals(ReportExecution.ExecutionStatus.CANCELLED, execution.getStatus());
            assertNotNull(execution.getCompletedAt());
            verify(auditService).logEvent(eq("REPORT_EXECUTION_CANCELLED"), eq("ReportExecution"), eq("exec-1"), anyString());
        }

        @Test
        void cancelsRunningExecution() {
            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.RUNNING)
                .reportDefinition(createTestReport())
                .build();
            when(executionRepository.findById("exec-1")).thenReturn(Optional.of(execution));
            when(executionRepository.save(any(ReportExecution.class))).thenReturn(execution);

            service.cancelExecution("exec-1");

            assertEquals(ReportExecution.ExecutionStatus.CANCELLED, execution.getStatus());
        }

        @Test
        void doesNotCancelCompletedExecution() {
            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.COMPLETED)
                .reportDefinition(createTestReport())
                .build();
            when(executionRepository.findById("exec-1")).thenReturn(Optional.of(execution));

            service.cancelExecution("exec-1");

            assertEquals(ReportExecution.ExecutionStatus.COMPLETED, execution.getStatus());
            verify(executionRepository, never()).save(any(ReportExecution.class));
        }

        @Test
        void throwsWhenExecutionNotFound() {
            when(executionRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.cancelExecution("nonexistent"));
        }
    }

    @Nested
    @DisplayName("toggleReport Tests")
    class ToggleReportTests {

        @Test
        void enablesReportWithSchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.DAILY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);

            assertTrue(result.getEnabled());
            assertNotNull(result.getNextRunAt());
            verify(auditService).logEvent(eq("REPORT_DEFINITION_TOGGLED"), eq("ReportDefinition"), eq("report-1"), contains("Enabled"));
        }

        @Test
        void disablesReportClearsNextRunTime() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(true);
            report.setNextRunAt(LocalDateTime.now().plusDays(1));
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", false);

            assertFalse(result.getEnabled());
            assertNull(result.getNextRunAt());
            verify(auditService).logEvent(eq("REPORT_DEFINITION_TOGGLED"), eq("ReportDefinition"), eq("report-1"), contains("Disabled"));
        }

        @Test
        void enablesReportWithoutSchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(null);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);

            assertTrue(result.getEnabled());
        }

        @Test
        void throwsWhenReportNotFound() {
            setTenantId("tenant-1");
            when(reportRepository.findById("nonexistent")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.toggleReport("nonexistent", true));
        }

        @Test
        void throwsWhenTenantMismatch() {
            setTenantId("tenant-2");
            ReportDefinition report = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));

            assertThrows(ValidationException.class, () -> service.toggleReport("report-1", true));
        }

        @Test
        void throwsWhenTenantIdIsNull() {
            setTenantId(null);
            assertThrows(ValidationException.class, () -> service.toggleReport("report-1", true));
        }
    }

    @Nested
    @DisplayName("calculateNextRunTime Branch Tests")
    class CalculateNextRunTimeBranchTests {

        private CreateReportCommand createCommandWithSchedule(ReportDefinition.ScheduleType scheduleType) {
            return CreateReportCommand.builder()
                .reportName("Test")
                .reportType(ReportDefinition.ReportType.SUMMARY)
                .ownerId("user-1")
                .scheduleType(scheduleType)
                .enabled(true)
                .build();
        }

        @Test
        void createsReportWithHourlySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.HOURLY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithDailySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.DAILY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithWeeklySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.WEEKLY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithMonthlySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.MONTHLY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithQuarterlySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.QUARTERLY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithYearlySchedule() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.YEARLY));
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithManualScheduleReturnsNullNextRun() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.MANUAL));
            assertNull(result.getNextRunAt());
        }

        @Test
        void createsReportWithCustomScheduleReturnsNullNextRun() {
            setTenantId("tenant-1");
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.createReport(createCommandWithSchedule(ReportDefinition.ScheduleType.CUSTOM));
            assertNull(result.getNextRunAt());
        }
    }

    @Nested
    @DisplayName("executeReportSync Exception Path Tests")
    class ExecuteReportSyncExceptionTests {

        @Test
        void executeReportSyncHandlesInterruptedException() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setScheduleType(null);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));

            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.PENDING)
                .reportDefinition(report)
                .requestedBy("user-1")
                .build();

            when(executionRepository.save(any(ReportExecution.class))).thenAnswer(inv -> {
                ReportExecution e = inv.getArgument(0);
                if (e.getStatus() == ReportExecution.ExecutionStatus.RUNNING) {
                    Thread.currentThread().interrupt();
                }
                return e;
            });

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(false)
                .outputFormat(ReportDefinition.OutputFormat.PDF)
                .requestedBy("user-1")
                .build();

            service.executeReport(query);
        }

        @Test
        void executeReportAsyncWithNullExecution() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(executionRepository.save(any(ReportExecution.class))).thenAnswer(inv -> inv.getArgument(0));
            when(executionRepository.findById("exec-1")).thenReturn(Optional.empty());

            ExecuteReportQuery query = ExecuteReportQuery.builder()
                .reportId("report-1")
                .async(true)
                .requestedBy("user-1")
                .build();

            ReportExecution result = service.executeReport(query);
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("cancelExecution Additional Status Tests")
    class CancelExecutionAdditionalTests {

        @Test
        void doesNotCancelFailedExecution() {
            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.FAILED)
                .reportDefinition(createTestReport())
                .build();
            when(executionRepository.findById("exec-1")).thenReturn(Optional.of(execution));

            service.cancelExecution("exec-1");

            assertEquals(ReportExecution.ExecutionStatus.FAILED, execution.getStatus());
            verify(executionRepository, never()).save(any(ReportExecution.class));
        }

        @Test
        void doesNotCancelCancelledExecution() {
            ReportExecution execution = ReportExecution.builder()
                .id("exec-1")
                .status(ReportExecution.ExecutionStatus.CANCELLED)
                .reportDefinition(createTestReport())
                .build();
            when(executionRepository.findById("exec-1")).thenReturn(Optional.of(execution));

            service.cancelExecution("exec-1");

            assertEquals(ReportExecution.ExecutionStatus.CANCELLED, execution.getStatus());
            verify(executionRepository, never()).save(any(ReportExecution.class));
        }
    }

    @Nested
    @DisplayName("toggleReport Schedule Branch Tests")
    class ToggleReportScheduleTests {

        @Test
        void enableReportWithHourlySchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.HOURLY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);
            assertTrue(result.getEnabled());
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void enableReportWithWeeklySchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.WEEKLY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void enableReportWithMonthlySchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.MONTHLY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void enableReportWithQuarterlySchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.QUARTERLY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);
            assertNotNull(result.getNextRunAt());
        }

        @Test
        void enableReportWithYearlySchedule() {
            setTenantId("tenant-1");
            ReportDefinition report = createTestReport();
            report.setEnabled(false);
            report.setScheduleType(ReportDefinition.ScheduleType.YEARLY);
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(report));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            ReportDefinition result = service.toggleReport("report-1", true);
            assertNotNull(result.getNextRunAt());
        }
    }

    @Nested
    @DisplayName("updateReport Schedule Branch Tests")
    class UpdateReportScheduleTests {

        @Test
        void updateReportWithNullScheduleAndEnabled() {
            setTenantId("tenant-1");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setScheduleType(null);
            cmd.setEnabled(true);

            ReportDefinition result = service.updateReport("report-1", cmd);
            assertNull(result.getNextRunAt());
        }

        @Test
        void updateReportWithScheduleAndDisabled() {
            setTenantId("tenant-1");
            ReportDefinition existing = createTestReport();
            when(reportRepository.findById("report-1")).thenReturn(Optional.of(existing));
            when(reportRepository.save(any(ReportDefinition.class))).thenAnswer(inv -> inv.getArgument(0));

            CreateReportCommand cmd = createTestCommand();
            cmd.setScheduleType(ReportDefinition.ScheduleType.HOURLY);
            cmd.setEnabled(false);

            ReportDefinition result = service.updateReport("report-1", cmd);
            assertNull(result.getNextRunAt());
        }
    }
}
