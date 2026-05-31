package com.gogidix.dashboard.reporting.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Report Section Value Object
 */
public class ReportSection {
    private final String title;
    private final SectionType type;
    private final List<Map<String, Object>> data;

    public ReportSection(String title, SectionType type, List<Map<String, Object>> data) {
        this.title = title;
        this.type = type;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public SectionType getType() {
        return type;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    /**
     * Get section ID (using title as ID)
     */
    public String getId() {
        return title.toLowerCase().replaceAll("\\s+", "_");
    }

    /**
     * Check if section has sorting configured
     */
    public boolean hasSorting() {
        return false; // Default implementation
    }

    /**
     * Check if section has filtering configured
     */
    public boolean hasFiltering() {
        return false; // Default implementation
    }

    /**
     * Apply sorting to data
     */
    public List<Map<String, Object>> applySorting(List<Map<String, Object>> tableData) {
        return tableData; // Default implementation - no sorting
    }

    /**
     * Apply filtering to data
     */
    public List<Map<String, Object>> applyFiltering(List<Map<String, Object>> tableData) {
        return tableData; // Default implementation - no filtering
    }

    /**
     * Get chart configuration for this section
     */
    public ChartConfiguration getChartConfiguration() {
        return ChartConfiguration.builder()
            .withChartType("line")
            .withWidth(800)
            .withHeight(400)
            .build();
    }

    public static ReportSectionBuilder builder() {
        return new ReportSectionBuilder();
    }

    public static class ReportSectionBuilder {
        private String title;
        private SectionType type;
        private List<Map<String, Object>> data;

        public ReportSectionBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ReportSectionBuilder type(SectionType type) {
            this.type = type;
            return this;
        }

        public ReportSectionBuilder withType(SectionType type) {
            this.type = type;
            return this;
        }

        public ReportSectionBuilder data(List<Map<String, Object>> data) {
            this.data = data;
            return this;
        }

        public ReportSection build() {
            return new ReportSection(title, type, data);
        }
    }
}
