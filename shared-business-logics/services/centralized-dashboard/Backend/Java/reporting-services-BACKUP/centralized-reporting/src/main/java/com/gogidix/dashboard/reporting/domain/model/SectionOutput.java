package com.gogidix.dashboard.reporting.domain.model;

import java.util.List;
import java.util.Map;

/**
 * Section Output Value Object
 */
public class SectionOutput {
    private final String sectionId;
    private final String title;
    private final List<Map<String, Object>> content;

    public SectionOutput(String sectionId, String title, List<Map<String, Object>> content) {
        this.sectionId = sectionId;
        this.title = title;
        this.content = content;
    }

    public String getSectionId() {
        return sectionId;
    }

    public String getTitle() {
        return title;
    }

    public List<Map<String, Object>> getContent() {
        return content;
    }

    /**
     * Static builder factory method
     */
    public static SectionOutputBuilder builder() {
        return new SectionOutputBuilder();
    }

    /**
     * Builder for SectionOutput
     */
    public static class SectionOutputBuilder {
        private String sectionId;
        private String content;
        private List<String> keyInsights;
        private Map<String, Double> kpis;
        private List<Map<String, Object>> tableData;
        private int rowCount;
        private String chartImagePath;
        private Map<String, Object> chartData;
        private Map<String, MetricDisplay> metrics;

        public SectionOutputBuilder withSectionId(String sectionId) {
            this.sectionId = sectionId;
            return this;
        }

        public SectionOutputBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public SectionOutputBuilder withKeyInsights(List<String> keyInsights) {
            this.keyInsights = keyInsights;
            return this;
        }

        public SectionOutputBuilder withKpis(Map<String, Double> kpis) {
            this.kpis = kpis;
            return this;
        }

        public SectionOutputBuilder withTableData(List<Map<String, Object>> tableData) {
            this.tableData = tableData;
            return this;
        }

        public SectionOutputBuilder withRowCount(int rowCount) {
            this.rowCount = rowCount;
            return this;
        }

        public SectionOutputBuilder withChartImagePath(String chartImagePath) {
            this.chartImagePath = chartImagePath;
            return this;
        }

        public SectionOutputBuilder withChartData(Map<String, Object> chartData) {
            this.chartData = chartData;
            return this;
        }

        public SectionOutputBuilder withMetrics(Map<String, MetricDisplay> metrics) {
            this.metrics = metrics;
            return this;
        }

        public SectionOutput build() {
            List<Map<String, Object>> contentList = List.of(
                Map.of("content", content != null ? content : "",
                       "insights", keyInsights != null ? keyInsights : List.of(),
                       "kpis", kpis != null ? kpis : Map.of(),
                       "tableData", tableData != null ? tableData : List.of(),
                       "chartImagePath", chartImagePath != null ? chartImagePath : "",
                       "chartData", chartData != null ? chartData : Map.of(),
                       "metrics", metrics != null ? metrics : Map.of())
            );
            return new SectionOutput(sectionId, "Section " + sectionId, contentList);
        }
    }
}
