package com.gogidix.analytics.bi.domain.port.in;

import com.gogidix.analytics.bi.domain.model.ReportDefinition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateReportCommandTest {

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        void builderCreatesCommandWithAllFields() {
            CreateReportCommand cmd = CreateReportCommand.builder()
                .reportName("Revenue Report")
                .description("Monthly revenue analysis")
                .reportType(ReportDefinition.ReportType.DETAILED)
                .scheduleType(ReportDefinition.ScheduleType.MONTHLY)
                .scheduleConfig("{\"cron\":\"0 0 1 * *\"}")
                .dataSource("{\"db\":\"analytics\"}")
                .queryDefinition("SELECT * FROM revenue")
                .outputFormat(ReportDefinition.OutputFormat.EXCEL)
                .templateConfig("{\"template\":\"standard\"}")
                .recipients("admin@test.com")
                .ownerId("user-1")
                .enabled(true)
                .build();

            assertEquals("Revenue Report", cmd.getReportName());
            assertEquals("Monthly revenue analysis", cmd.getDescription());
            assertEquals(ReportDefinition.ReportType.DETAILED, cmd.getReportType());
            assertEquals(ReportDefinition.ScheduleType.MONTHLY, cmd.getScheduleType());
            assertEquals("{\"cron\":\"0 0 1 * *\"}", cmd.getScheduleConfig());
            assertEquals("{\"db\":\"analytics\"}", cmd.getDataSource());
            assertEquals("SELECT * FROM revenue", cmd.getQueryDefinition());
            assertEquals(ReportDefinition.OutputFormat.EXCEL, cmd.getOutputFormat());
            assertEquals("{\"template\":\"standard\"}", cmd.getTemplateConfig());
            assertEquals("admin@test.com", cmd.getRecipients());
            assertEquals("user-1", cmd.getOwnerId());
            assertTrue(cmd.getEnabled());
        }

        @Test
        void builderDefaults() {
            CreateReportCommand cmd = CreateReportCommand.builder()
                .reportName("Test")
                .reportType(ReportDefinition.ReportType.SUMMARY)
                .ownerId("owner")
                .build();

            assertEquals(ReportDefinition.OutputFormat.PDF, cmd.getOutputFormat());
            assertTrue(cmd.getEnabled());
        }
    }

    @Test
    void noArgsConstructor() {
        CreateReportCommand cmd = new CreateReportCommand();
        assertNotNull(cmd);
        assertNull(cmd.getReportName());
    }

    @Test
    void allArgsConstructor() {
        CreateReportCommand cmd = new CreateReportCommand("name", "desc",
            ReportDefinition.ReportType.SUMMARY, ReportDefinition.ScheduleType.DAILY,
            "schedCfg", "ds", "query", ReportDefinition.OutputFormat.CSV,
            "tmplCfg", "recipients", "owner", false);
        assertEquals("name", cmd.getReportName());
        assertFalse(cmd.getEnabled());
    }

    @Test
    void gettersAndSetters() {
        CreateReportCommand cmd = new CreateReportCommand();

        cmd.setReportName("New Name");
        assertEquals("New Name", cmd.getReportName());

        cmd.setDescription("Desc");
        assertEquals("Desc", cmd.getDescription());

        cmd.setReportType(ReportDefinition.ReportType.TREND);
        assertEquals(ReportDefinition.ReportType.TREND, cmd.getReportType());

        cmd.setScheduleType(ReportDefinition.ScheduleType.WEEKLY);
        assertEquals(ReportDefinition.ScheduleType.WEEKLY, cmd.getScheduleType());

        cmd.setScheduleConfig("cfg");
        assertEquals("cfg", cmd.getScheduleConfig());

        cmd.setDataSource("ds");
        assertEquals("ds", cmd.getDataSource());

        cmd.setQueryDefinition("query");
        assertEquals("query", cmd.getQueryDefinition());

        cmd.setOutputFormat(ReportDefinition.OutputFormat.HTML);
        assertEquals(ReportDefinition.OutputFormat.HTML, cmd.getOutputFormat());

        cmd.setTemplateConfig("tmpl");
        assertEquals("tmpl", cmd.getTemplateConfig());

        cmd.setRecipients("a@b.com");
        assertEquals("a@b.com", cmd.getRecipients());

        cmd.setOwnerId("owner-2");
        assertEquals("owner-2", cmd.getOwnerId());

        cmd.setEnabled(false);
        assertFalse(cmd.getEnabled());
    }

    @Nested
    @DisplayName("Equals Branch Coverage Tests")
    class EqualsBranchTests {

        private CreateReportCommand createFullyPopulated() {
            return CreateReportCommand.builder()
                .reportName("Name")
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
                .enabled(true)
                .build();
        }

        @Test
        void equalsSameInstance() {
            CreateReportCommand c = createFullyPopulated();
            assertEquals(c, c);
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
        void equalsDifferentReportName() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setReportName("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDescription() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setDescription("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentReportType() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setReportType(ReportDefinition.ReportType.DETAILED);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentScheduleType() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setScheduleType(ReportDefinition.ScheduleType.WEEKLY);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentScheduleConfig() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setScheduleConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentDataSource() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setDataSource("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentQueryDefinition() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setQueryDefinition("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOutputFormat() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setOutputFormat(ReportDefinition.OutputFormat.CSV);
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentTemplateConfig() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setTemplateConfig("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentRecipients() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setRecipients("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentOwnerId() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setOwnerId("different");
            assertNotEquals(a, b);
        }

        @Test
        void equalsDifferentEnabled() {
            CreateReportCommand a = createFullyPopulated();
            CreateReportCommand b = createFullyPopulated();
            b.setEnabled(false);
            assertNotEquals(a, b);
        }

        @Test
        void equalsWithNullFields() {
            CreateReportCommand a = new CreateReportCommand();
            CreateReportCommand b = new CreateReportCommand();
            assertEquals(a, b);
        }

        @Test
        void hashCodeConsistency() {
            CreateReportCommand c = createFullyPopulated();
            int h1 = c.hashCode();
            int h2 = c.hashCode();
            assertEquals(h1, h2);
        }

        @Test
        void hashCodeEqualObjects() {
            assertEquals(createFullyPopulated().hashCode(), createFullyPopulated().hashCode());
        }

        @Test
        void hashCodeWithNullFields() {
            CreateReportCommand c = new CreateReportCommand();
            assertNotNull(c.hashCode());
        }

        @Test
        void toStringReturnsNonNull() {
            assertNotNull(createFullyPopulated().toString());
        }

        @Test
        void toStringWithNullFields() {
            assertNotNull(new CreateReportCommand().toString());
        }
    }
}
