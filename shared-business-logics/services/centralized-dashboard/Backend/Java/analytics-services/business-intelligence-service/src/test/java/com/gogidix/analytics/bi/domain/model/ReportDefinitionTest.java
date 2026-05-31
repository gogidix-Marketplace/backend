package com.gogidix.analytics.bi.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportDefinitionTest {

    private ReportDefinition report;

    @BeforeEach
    void setUp() {
        report = ReportDefinition.builder()
            .reportName("Monthly Revenue")
            .description("Monthly revenue report")
            .reportType(ReportDefinition.ReportType.SUMMARY)
            .ownerId("user-1")
            .tenantId("tenant-1")
            .build();
    }

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesReportWithAllFields() {
            LocalDateTime now = LocalDateTime.now();
            ReportDefinition rd = ReportDefinition.builder()
                .id("id-1")
                .reportName("Test Report")
                .description("Desc")
                .reportType(ReportDefinition.ReportType.DETAILED)
                .scheduleType(ReportDefinition.ScheduleType.DAILY)
                .scheduleConfig("{\"cron\":\"0 0 * * *\"}")
                .dataSource("{\"db\":\"main\"}")
                .queryDefinition("SELECT * FROM orders")
                .outputFormat(ReportDefinition.OutputFormat.EXCEL)
                .templateConfig("{\"template\":\"standard\"}")
                .recipients("admin@test.com,user@test.com")
                .ownerId("owner-1")
                .tenantId("tenant-1")
                .enabled(false)
                .lastRunAt(now)
                .nextRunAt(now.plusDays(1))
                .createdAt(now)
                .updatedAt(now)
                .executions(new ArrayList<>())
                .build();

            assertEquals("id-1", rd.getId());
            assertEquals("Test Report", rd.getReportName());
            assertEquals(ReportDefinition.ReportType.DETAILED, rd.getReportType());
            assertEquals(ReportDefinition.ScheduleType.DAILY, rd.getScheduleType());
            assertEquals(ReportDefinition.OutputFormat.EXCEL, rd.getOutputFormat());
            assertFalse(rd.getEnabled());
        }

        @Test
        void builderDefaults() {
            ReportDefinition rd = ReportDefinition.builder()
                .reportName("Test")
                .reportType(ReportDefinition.ReportType.SUMMARY)
                .ownerId("o")
                .tenantId("t")
                .build();

            assertEquals(ReportDefinition.OutputFormat.PDF, rd.getOutputFormat());
            assertTrue(rd.getEnabled());
            assertNotNull(rd.getExecutions());
            assertTrue(rd.getExecutions().isEmpty());
        }
    }

    @Test
    void noArgsConstructorCreatesEmptyReport() {
        ReportDefinition rd = new ReportDefinition();
        assertNotNull(rd);
        assertNull(rd.getReportName());
    }

    @Test
    void allArgsConstructor() {
        ReportDefinition rd = new ReportDefinition("id", "name", "desc",
            ReportDefinition.ReportType.SUMMARY, ReportDefinition.ScheduleType.WEEKLY,
            "schedCfg", "ds", "query", ReportDefinition.OutputFormat.CSV,
            "tmplCfg", "recipients", "owner", "tenant", true,
            null, null, null, null, new ArrayList<>());
        assertEquals("id", rd.getId());
        assertEquals("name", rd.getReportName());
    }

    @Test
    void gettersAndSetters() {
        report.setId("new-id");
        assertEquals("new-id", report.getId());

        report.setReportName("New Name");
        assertEquals("New Name", report.getReportName());

        report.setDescription("New Desc");
        assertEquals("New Desc", report.getDescription());

        report.setReportType(ReportDefinition.ReportType.TREND);
        assertEquals(ReportDefinition.ReportType.TREND, report.getReportType());

        report.setScheduleType(ReportDefinition.ScheduleType.MONTHLY);
        assertEquals(ReportDefinition.ScheduleType.MONTHLY, report.getScheduleType());

        report.setScheduleConfig("new-cfg");
        assertEquals("new-cfg", report.getScheduleConfig());

        report.setDataSource("new-ds");
        assertEquals("new-ds", report.getDataSource());

        report.setQueryDefinition("SELECT 1");
        assertEquals("SELECT 1", report.getQueryDefinition());

        report.setOutputFormat(ReportDefinition.OutputFormat.HTML);
        assertEquals(ReportDefinition.OutputFormat.HTML, report.getOutputFormat());

        report.setTemplateConfig("new-tmpl");
        assertEquals("new-tmpl", report.getTemplateConfig());

        report.setRecipients("a@b.com");
        assertEquals("a@b.com", report.getRecipients());

        report.setOwnerId("new-owner");
        assertEquals("new-owner", report.getOwnerId());

        report.setTenantId("new-tenant");
        assertEquals("new-tenant", report.getTenantId());

        report.setEnabled(false);
        assertFalse(report.getEnabled());

        LocalDateTime now = LocalDateTime.now();
        report.setLastRunAt(now);
        assertEquals(now, report.getLastRunAt());

        report.setNextRunAt(now);
        assertEquals(now, report.getNextRunAt());

        report.setCreatedAt(now);
        assertEquals(now, report.getCreatedAt());

        report.setUpdatedAt(now);
        assertEquals(now, report.getUpdatedAt());
    }

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleTests {

        @Test
        void onCreateSetsTimestamps() {
            assertNull(report.getCreatedAt());
            assertNull(report.getUpdatedAt());
            report.onCreate();
            assertNotNull(report.getCreatedAt());
            assertNotNull(report.getUpdatedAt());
        }

        @Test
        void onCreateGeneratesIdWhenNull() {
            report.setId(null);
            report.onCreate();
            assertNotNull(report.getId());
        }

        @Test
        void onCreatePreservesExistingId() {
            report.setId("existing-id");
            report.onCreate();
            assertEquals("existing-id", report.getId());
        }

        @Test
        void onUpdateSetsUpdatedAt() {
            report.onUpdate();
            assertNotNull(report.getUpdatedAt());
        }
    }

    @Nested
    @DisplayName("Enum Tests")
    class EnumTests {

        @Test
        void reportTypeValues() {
            ReportDefinition.ReportType[] types = ReportDefinition.ReportType.values();
            assertEquals(5, types.length);
            assertEquals(ReportDefinition.ReportType.SUMMARY, ReportDefinition.ReportType.valueOf("SUMMARY"));
            assertEquals(ReportDefinition.ReportType.DETAILED, ReportDefinition.ReportType.valueOf("DETAILED"));
            assertEquals(ReportDefinition.ReportType.TREND, ReportDefinition.ReportType.valueOf("TREND"));
            assertEquals(ReportDefinition.ReportType.COMPARISON, ReportDefinition.ReportType.valueOf("COMPARISON"));
            assertEquals(ReportDefinition.ReportType.CUSTOM, ReportDefinition.ReportType.valueOf("CUSTOM"));
        }

        @Test
        void scheduleTypeValues() {
            ReportDefinition.ScheduleType[] types = ReportDefinition.ScheduleType.values();
            assertEquals(8, types.length);
            assertEquals(ReportDefinition.ScheduleType.MANUAL, ReportDefinition.ScheduleType.valueOf("MANUAL"));
            assertEquals(ReportDefinition.ScheduleType.HOURLY, ReportDefinition.ScheduleType.valueOf("HOURLY"));
            assertEquals(ReportDefinition.ScheduleType.DAILY, ReportDefinition.ScheduleType.valueOf("DAILY"));
            assertEquals(ReportDefinition.ScheduleType.WEEKLY, ReportDefinition.ScheduleType.valueOf("WEEKLY"));
            assertEquals(ReportDefinition.ScheduleType.MONTHLY, ReportDefinition.ScheduleType.valueOf("MONTHLY"));
            assertEquals(ReportDefinition.ScheduleType.QUARTERLY, ReportDefinition.ScheduleType.valueOf("QUARTERLY"));
            assertEquals(ReportDefinition.ScheduleType.YEARLY, ReportDefinition.ScheduleType.valueOf("YEARLY"));
            assertEquals(ReportDefinition.ScheduleType.CUSTOM, ReportDefinition.ScheduleType.valueOf("CUSTOM"));
        }

        @Test
        void outputFormatValues() {
            ReportDefinition.OutputFormat[] formats = ReportDefinition.OutputFormat.values();
            assertEquals(5, formats.length);
            assertEquals(ReportDefinition.OutputFormat.PDF, ReportDefinition.OutputFormat.valueOf("PDF"));
            assertEquals(ReportDefinition.OutputFormat.EXCEL, ReportDefinition.OutputFormat.valueOf("EXCEL"));
            assertEquals(ReportDefinition.OutputFormat.CSV, ReportDefinition.OutputFormat.valueOf("CSV"));
            assertEquals(ReportDefinition.OutputFormat.HTML, ReportDefinition.OutputFormat.valueOf("HTML"));
            assertEquals(ReportDefinition.OutputFormat.JSON, ReportDefinition.OutputFormat.valueOf("JSON"));
        }
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private ReportDefinition createFullyPopulated() {
            LocalDateTime now = LocalDateTime.of(2024, 1, 15, 10, 30);
            return ReportDefinition.builder()
                .id("id-1")
                .reportName("Report")
                .description("Desc")
                .reportType(ReportDefinition.ReportType.SUMMARY)
                .scheduleType(ReportDefinition.ScheduleType.DAILY)
                .scheduleConfig("schedCfg")
                .dataSource("ds")
                .queryDefinition("query")
                .outputFormat(ReportDefinition.OutputFormat.PDF)
                .templateConfig("tmplCfg")
                .recipients("recip")
                .ownerId("owner-1")
                .tenantId("tenant-1")
                .enabled(true)
                .lastRunAt(now)
                .nextRunAt(now)
                .createdAt(now)
                .updatedAt(now)
                .executions(new ArrayList<>())
                .build();
        }

        @Test
        void equalsSameInstance() {
            ReportDefinition r = createFullyPopulated();
            assertEquals(r, r);
        }

        @Test
        void equalsNull() {
            assertNotEquals(createFullyPopulated(), null);
        }

        @Test
        void equalsDifferentType() {
            assertNotEquals(createFullyPopulated(), 42);
        }

        @Test
        void equalsIdenticalObjects() {
            assertEquals(createFullyPopulated(), createFullyPopulated());
        }

        @Test
        void equalsDifferentId() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentReportName() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setReportName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDescription() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentReportType() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setReportType(ReportDefinition.ReportType.DETAILED);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentScheduleType() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setScheduleType(ReportDefinition.ScheduleType.WEEKLY);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentScheduleConfig() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setScheduleConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDataSource() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentQueryDefinition() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOutputFormat() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setOutputFormat(ReportDefinition.OutputFormat.CSV);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTemplateConfig() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setTemplateConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRecipients() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setRecipients("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOwnerId() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTenantId() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setTenantId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentEnabled() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setEnabled(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentLastRunAt() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setLastRunAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentNextRunAt() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setNextRunAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentCreatedAt() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setCreatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentUpdatedAt() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            b.setUpdatedAt(LocalDateTime.of(2099, 1, 1, 0, 0));
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentExecutions() {
            ReportDefinition a = createFullyPopulated();
            ReportDefinition b = createFullyPopulated();
            List<ReportExecution> execs = new ArrayList<>();
            execs.add(ReportExecution.builder().id("e1").reportDefinition(a).build());
            b.setExecutions(execs);
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            ReportDefinition a = new ReportDefinition();
            ReportDefinition b = new ReportDefinition();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            ReportDefinition r = createFullyPopulated();
            int h1 = r.hashCode();
            int h2 = r.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            ReportDefinition r = new ReportDefinition();
            assertNotNull(r.hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            ReportDefinition r = createFullyPopulated();
            assertNotNull(r.toString());
            assertTrue(r.toString().contains("Report"));
        }

        @Test
        void toStringWithNullFields() {
            ReportDefinition r = new ReportDefinition();
            assertNotNull(r.toString());
        }
    }
}
