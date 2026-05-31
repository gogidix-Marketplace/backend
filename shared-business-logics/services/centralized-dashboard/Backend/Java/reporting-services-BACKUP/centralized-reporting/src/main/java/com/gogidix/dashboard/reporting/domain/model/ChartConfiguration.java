package com.gogidix.dashboard.reporting.domain.model;

/**
 * Chart Configuration Value Object
 */
public class ChartConfiguration {
    private final String chartType;
    private final int width;
    private final int height;

    public ChartConfiguration(String chartType, int width, int height) {
        this.chartType = chartType;
        this.width = width;
        this.height = height;
    }

    public String getChartType() {
        return chartType;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Static builder factory method
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for ChartConfiguration
     */
    public static class Builder {
        private String chartType;
        private int width;
        private int height;

        public Builder withChartType(String chartType) {
            this.chartType = chartType;
            return this;
        }

        public Builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public ChartConfiguration build() {
            return new ChartConfiguration(
                chartType != null ? chartType : "line",
                width > 0 ? width : 800,
                height > 0 ? height : 400
            );
        }
    }
}
