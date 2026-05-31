package com.gogidix.analytics.bi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportExecutionTest {

    private ReportExecution execution;
    private ReportDefinition reportDefinition;

    @BeforeEach
    void setUp() {
        reportDefinition = ReportDefinition.builder()
            .id("report-1")
            .reportName("Test Report")
            .reportType(ReportDefinition.ReportType.SUMMARY)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .build();

        execution = ReportExecution.builder()
            .id("exec-1")
            .status(ReportExecution.ExecutionStatus.PENDING)
            .reportDefinition(reportDefinition)
            .requestedBy("user-1")
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesExecutionWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            ReportExecution re = ReportExecution.builder()
                .id("id-1")
                .status(ReportExecution.ExecutionStatus.RUNNING)
                .startedAt(now)
                .completedAt(now)
                .filePath("/reports/test.pdf")
                .fileSizeBytes(2048L)
                .recordCount(500)
                .errorMessage(null)
                .requestedBy("user-1")
                .createdAt(now)
                .reportDefinition(reportDefinition)
                .build();

            assertEquals("id-1", re.getId());
            assertEquals(ReportExecution.ExecutionStatus.RUNNING, re.getStatus());
            assertEquals(now, re.getStartedAt());
            assertEquals("/reports/test.pdf", re.getFilePath());
            assertEquals(2048L, re.getFileSizeBytes());
            assertEquals(500, re.getRecordCount());
            assertEquals("user-1", re.getRequestedBy());
        }

        @Test
        void builderDefaults() {
            ReportExecution re = ReportExecution.builder()
                .reportDefinition(reportDefinition)
                .build();

            assertEquals(ReportExecution.ExecutionStatus.PENDING, re.getStatus());
        }
    }

    @Test
    void noArgsConstructor() {
        ReportExecution re = new ReportExecution();
        assertNotNull(re);
        assertNull(re.getId());
    }

    @Test
    void allArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ReportExecution re = new ReportExecution("id", ReportExecution.ExecutionStatus.COMPLETED,
            now, now, "/path", 1024L, 100, null, "user", now, reportDefinition);
        assertEquals("id", re.getId());
        assertEquals(ReportExecution.ExecutionStatus.COMPLETED, re.getStatus());
    }

    @Test
    void gettersAndSetters() {
        execution.setId("new-id");
        assertEquals("new-id", execution.getId());

        execution.setStatus(ReportExecution.ExecutionStatus.RUNNING);
        assertEquals(ReportExecution.ExecutionStatus.RUNNING, execution.getStatus());

        LocalDateTime now = LocalDateTime.now();
        execution.setStartedAt(now);
        assertEquals(now, execution.getStartedAt());

        execution.setCompletedAt(now);
        assertEquals(now, execution.getCompletedAt());

        execution.setFilePath("/reports/new.pdf");
        assertEquals("/reports/new.pdf", execution.getFilePath());

        execution.setFileSizeBytes(4096L);
        assertEquals(4096L, execution.getFileSizeBytes());

        execution.setRecordCount(200);
        assertEquals(200, execution.getRecordCount());

        execution.setErrorMessage("Something went wrong");
        assertEquals("Something went wrong", execution.getErrorMessage());

        execution.setRequestedBy("user-2");
        assertEquals("user-2", execution.getRequestedBy());

        execution.setCreatedAt(now);
        assertEquals(now, execution.getCreatedAt());

        execution.setReportDefinition(reportDefinition);
        assertEquals(reportDefinition, execution.getReportDefinition());
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateSetsCreatedAt() {
            assertNull(execution.getCreatedAt());
            execution.onCreate();
            assertNotNull(execution.getCreatedAt());
        }

        @Test
        void onCreateGeneratesIdWhenNull() {
            execution.setId(null);
            execution.onCreate();
            assertNotNull(execution.getId());
        }

        @Test
        void onCreatePreservesExistingId() {
            execution.setId("existing-id");
            execution.onCreate();
            assertEquals("existing-id", execution.getId());
        }

        @Test
        void onCreateSetsStartedAtWhenStatusIsRunning() {
            execution.setStatus(ReportExecution.ExecutionStatus.RUNNING);
            assertNull(execution.getStartedAt());
            execution.onCreate();
            assertNotNull(execution.getStartedAt());
        }

        @Test
        void onCreateDoesNotSetStartedAtWhenStatusIsNotRunning() {
            execution.setStatus(ReportExecution.ExecutionStatus.PENDING);
            execution.onCreate();
            assertNull(execution.getStartedAt());
        }
    }

    @Nested
    @DisplayName("Business Method Tests")
    class BusinessMethodTests {

        @Test
        void markAsRunning() {
            execution.markAsRunning();
            assertEquals(ReportExecution.ExecutionStatus.RUNNING, execution.getStatus());
            assertNotNull(execution.getStartedAt());
        }

        @Test
        void markAsCompleted() {
            execution.markAsCompleted("/reports/output.pdf", 4096L, 250);

            assertEquals(ReportExecution.ExecutionStatus.COMPLETED, execution.getStatus());
            assertNotNull(execution.getCompletedAt());
            assertEquals("/reports/output.pdf", execution.getFilePath());
            assertEquals(4096L, execution.getFileSizeBytes());
            assertEquals(250, execution.getRecordCount());
        }

        @Test
        void markAsFailed() {
            execution.markAsFailed("Database connection failed");

            assertEquals(ReportExecution.ExecutionStatus.FAILED, execution.getStatus());
            assertNotNull(execution.getCompletedAt());
            assertEquals("Database connection failed", execution.getErrorMessage());
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    class EnumTests {

        @Test
        void executionStatusValues() {
            ReportExecution.ExecutionStatus[] statuses = ReportExecution.ExecutionStatus.values();
            assertEquals(5, statuses.length);
            assertEquals(ReportExecution.ExecutionStatus.PENDING, ReportExecution.ExecutionStatus.valueOf("PENDING"));
            assertEquals(ReportExecution.ExecutionStatus.RUNNING, ReportExecution.ExecutionStatus.valueOf("RUNNING"));
            assertEquals(ReportExecution.ExecutionStatus.COMPLETED, ReportExecution.ExecutionStatus.valueOf("COMPLETED"));
            assertEquals(ReportExecution.ExecutionStatus.FAILED, ReportExecution.ExecutionStatus.valueOf("FAILED"));
            assertEquals(ReportExecution.ExecutionStatus.CANCELLED, ReportExecution.ExecutionStatus.valueOf("CANCELLED"));
        }
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private ReportExecution createFullyPopulated() {
            LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30);
            ReportDefinition rd = ReportDefinition.builder()
                .id("rd-1").reportName("R").reportType(ReportDefinition.ReportType.SUMMARY)
                .ownerId("o").tenantId("t").build();
            return ReportExecution.builder()
                .id("id-1")
                .status(ReportExecution.ExecutionStatus.RUNNING)
                .startedAt(now)
                .completedAt(now)
                .filePath("/reports/out.pdf")
                .fileSizeBytes(1024L)
                .recordCount(50)
                .errorMessage("err")
                .requestedBy("user-1")
                .createdAt(now)
                .reportDefinition(rd)
                .build();
        }

        @Test
        void equalsSameInstance() {
            ReportExecution e = createFullyPopulated();
            assertEquals(e, e);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), new Object());
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentId() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentStatus() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setStatus(ReportExecution.ExecutionStatus.PENDING);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentStartedAt() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setStartedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCompletedAt() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setCompletedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentFilePath() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setFilePath("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentFileSizeBytes() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setFileSizeBytes(9999L);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRecordCount() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setRecordCount(9999);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentErrorMessage() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setErrorMessage("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRequestedBy() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setRequestedBy("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCreatedAt() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentReportDefinition() {
            ReportExecution a = createFullyPopulated();
            ReportExecution b = createFullyPopulated();
            b.setReportDefinition(ReportDefinition.builder().id("other").reportName("X")
                .reportType(ReportDefinition.ReportType.CUSTOM).ownerId("o").tenantId("t").build());
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            ReportExecution a = new ReportExecution();
            ReportExecution b = new ReportExecution();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            ReportExecution e = createFullyPopulated();
            int h1 = e.hashCode();
            int h2 = e.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            ReportExecution e = new ReportExecution();
            assertNotNull(e.hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            ReportExecution e = createFullyPopulated();
            assertNotNull(e.toString());
            assertTrue(e.toString().contains("RUNNING"));
        }

        @Test
        void toStringWithNullFields() {
            ReportExecution e = new ReportExecution();
            assertNotNull(e.toString());
        }
    }
}
