package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportEnumsTest {

    @Test
    void reportType_values() {
        assertEquals(4, ReportType.values().length);
        assertNotNull(ReportType.EXECUTIVE_SUMMARY.getDescription());
        assertNotNull(ReportType.PERFORMANCE_DASHBOARD.getDescription());
        assertNotNull(ReportType.KPI_ANALYTICS.getDescription());
        assertNotNull(ReportType.OPERATIONAL_METRICS.getDescription());
    }

    @Test
    void reportStatus_values() {
        assertEquals(4, ReportStatus.values().length);
    }

    @Test
    void exportFormat_values() {
        assertEquals(4, ExportFormat.values().length);
        assertEquals("application/pdf", ExportFormat.PDF.getMimeType());
        assertEquals(".pdf", ExportFormat.PDF.getFileExtension());
        assertEquals("text/csv", ExportFormat.CSV.getMimeType());
        assertEquals(".json", ExportFormat.JSON.getFileExtension());
    }

    @Test
    void outputFormat_values() {
        assertEquals(4, OutputFormat.values().length);
    }

    @Test
    void sectionType_values() {
        assertEquals(4, SectionType.values().length);
    }

    @Test
    void metricStatus_values() {
        assertEquals(7, MetricStatus.values().length);
    }

    @Test
    void scheduleStatus_values() {
        assertEquals(4, ScheduledReport.ScheduleStatus.values().length);
    }

    @Test
    void exportStatus_values() {
        assertEquals(5, ReportExport.ExportStatus.values().length);
    }
}
