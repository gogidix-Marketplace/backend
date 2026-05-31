package com.gogidix.dashboard.reporting.domain.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ReportSectionTest {

    @Test
    void constructor_setsFields() {
        ReportSection section = new ReportSection("Sales Data", SectionType.DATA_TABLE,
                List.of(Map.of("product", "A", "revenue", 1000)));
        assertEquals("Sales Data", section.getTitle());
        assertEquals(SectionType.DATA_TABLE, section.getType());
        assertEquals(1, section.getData().size());
    }

    @Test
    void getId_returnsSnakeCase() {
        ReportSection section = new ReportSection("Sales Report", SectionType.CHART, List.of());
        assertEquals("sales_report", section.getId());
    }

    @Test
    void hasSorting_returnsFalse() {
        ReportSection section = new ReportSection("Test", SectionType.DATA_TABLE, List.of());
        assertFalse(section.hasSorting());
    }

    @Test
    void hasFiltering_returnsFalse() {
        ReportSection section = new ReportSection("Test", SectionType.DATA_TABLE, List.of());
        assertFalse(section.hasFiltering());
    }

    @Test
    void applySorting_returnsSameData() {
        List<Map<String, Object>> data = List.of(Map.of("a", 1));
        ReportSection section = new ReportSection("T", SectionType.DATA_TABLE, data);
        assertEquals(data, section.applySorting(data));
    }

    @Test
    void applyFiltering_returnsSameData() {
        List<Map<String, Object>> data = List.of(Map.of("a", 1));
        ReportSection section = new ReportSection("T", SectionType.DATA_TABLE, data);
        assertEquals(data, section.applyFiltering(data));
    }

    @Test
    void getChartConfiguration_returnsConfig() {
        ReportSection section = new ReportSection("Chart", SectionType.CHART, List.of());
        ChartConfiguration config = section.getChartConfiguration();
        assertNotNull(config);
    }

    @Test
    void builder_createsSection() {
        ReportSection section = ReportSection.builder()
                .title("Test Section")
                .type(SectionType.EXECUTIVE_SUMMARY)
                .data(List.of(Map.of("k", "v")))
                .build();

        assertEquals("Test Section", section.getTitle());
        assertEquals(SectionType.EXECUTIVE_SUMMARY, section.getType());
    }
}
