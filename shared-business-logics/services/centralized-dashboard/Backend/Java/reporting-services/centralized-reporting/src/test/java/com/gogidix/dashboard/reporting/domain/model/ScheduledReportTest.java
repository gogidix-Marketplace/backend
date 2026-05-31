package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduledReportTest {

    @Test
    void builder_createsReport() {
        ScheduledReport sr = ScheduledReport.builder()
                .withScheduleId("s1")
                .withReportName("Daily Report")
                .withType(ReportType.EXECUTIVE_SUMMARY)
                .withDomain("order")
                .withCronExpression("0 0 * * *")
                .withConfiguration(new ReportConfiguration(OutputFormat.PDF, true, true))
                .withCreatedBy("admin")
                .withCreatedAt(LocalDateTime.now())
                .withNextExecutionTime(LocalDateTime.now().plusHours(24))
                .build();

        assertEquals("s1", sr.getScheduleId());
        assertEquals("Daily Report", sr.getReportName());
        assertEquals(ReportType.EXECUTIVE_SUMMARY, sr.getType());
        assertEquals("order", sr.getDomain());
        assertEquals("0 0 * * *", sr.getCronExpression());
        assertEquals("admin", sr.getCreatedBy());
        assertEquals(ScheduledReport.ScheduleStatus.ACTIVE, sr.getStatus());
    }

    @Test
    void builder_generatesDefaultScheduleId() {
        ScheduledReport sr = ScheduledReport.builder()
                .withReportName("Test").withType(ReportType.KPI_ANALYTICS)
                .withDomain("d").withCronExpression("0 0 * * *")
                .withConfiguration(new ReportConfiguration(OutputFormat.CSV, false, true))
                .withCreatedBy("user").build();

        assertNotNull(sr.getScheduleId());
    }

    @Test
    void builder_defaultStatus() {
        ScheduledReport sr = ScheduledReport.builder()
                .withReportName("R").withType(ReportType.OPERATIONAL_METRICS)
                .withDomain("d").withCronExpression("cron")
                .withConfiguration(new ReportConfiguration(OutputFormat.JSON, true, true))
                .withCreatedBy("u").build();

        assertEquals(ScheduledReport.ScheduleStatus.ACTIVE, sr.getStatus());
    }

    @Test
    void builder_withCustomStatus() {
        ScheduledReport sr = ScheduledReport.builder()
                .withReportName("R").withType(ReportType.OPERATIONAL_METRICS)
                .withDomain("d").withCronExpression("cron")
                .withConfiguration(new ReportConfiguration(OutputFormat.JSON, true, true))
                .withCreatedBy("u").withStatus(ScheduledReport.ScheduleStatus.PAUSED)
                .build();

        assertEquals(ScheduledReport.ScheduleStatus.PAUSED, sr.getStatus());
    }

    @Test
    void builder_withErrorMessage() {
        ScheduledReport sr = ScheduledReport.builder()
                .withReportName("R").withType(ReportType.OPERATIONAL_METRICS)
                .withDomain("d").withCronExpression("cron")
                .withConfiguration(new ReportConfiguration(OutputFormat.JSON, true, true))
                .withCreatedBy("u").withErrorMessage("Schedule failed")
                .build();

        assertEquals("Schedule failed", sr.getErrorMessage());
    }

    @Test
    void builder_nullChecks() {
        assertThrows(NullPointerException.class, () -> ScheduledReport.builder().build());
    }
}
