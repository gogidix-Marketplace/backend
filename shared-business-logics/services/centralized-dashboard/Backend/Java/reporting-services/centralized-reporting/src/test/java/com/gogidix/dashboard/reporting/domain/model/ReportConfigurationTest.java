package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportConfigurationTest {

    @Test
    void constructor_setsFields() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, false);
        assertEquals(OutputFormat.PDF, config.getFormat());
        assertEquals(OutputFormat.PDF, config.getOutputFormat());
        assertTrue(config.isIncludeCharts());
        assertFalse(config.isIncludeSummary());
    }

    @Test
    void getSectionsPerPage_returnsDefault() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.CSV, true, true);
        assertEquals(10, config.getSectionsPerPage());
    }

    @Test
    void getMetricStatus_excellent() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, true);
        assertEquals(MetricStatus.EXCELLENT, config.getMetricStatus("test", 95.0));
    }

    @Test
    void getMetricStatus_good() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, true);
        assertEquals(MetricStatus.GOOD, config.getMetricStatus("test", 75.0));
    }

    @Test
    void getMetricStatus_fair() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, true);
        assertEquals(MetricStatus.FAIR, config.getMetricStatus("test", 55.0));
    }

    @Test
    void getMetricStatus_poor() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, true);
        assertEquals(MetricStatus.POOR, config.getMetricStatus("test", 30.0));
    }

    @Test
    void calculateTrend_returnsDefault() {
        ReportConfiguration config = new ReportConfiguration(OutputFormat.PDF, true, true);
        assertEquals("↑", config.calculateTrend("metric", 50.0));
    }
}
