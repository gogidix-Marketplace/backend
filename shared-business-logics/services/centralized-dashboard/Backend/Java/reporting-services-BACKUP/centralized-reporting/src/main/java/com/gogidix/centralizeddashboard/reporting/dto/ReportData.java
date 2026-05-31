package com.gogidix.centralizeddashboard.reporting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for report data
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportData {
    private String reportId;
    private String reportName;
    private String dataSource;
    private List<String> headers;
    private List<Map<String, Object>> rows;
    private Map<String, Object> metadata;
    
    // The following fields/methods are needed by ExportService
    private List<String> columns;
    private List<Map<String, Object>> data;
    
    /**
     * Returns the data rows for this report
     * @return List of data rows as maps
     */
    public List<Map<String, Object>> getData() {
        return data != null ? data : rows;
    }
    
    /**
     * Returns the column names for this report
     * @return List of column names
     */
    public List<String> getColumns() {
        return columns != null ? columns : headers != null ? headers : new ArrayList<>();
    }

    // Manual setters (Lombok @Data not generating them)
    public void setReportId(String reportId) { this.reportId = reportId; }
    public void setReportName(String reportName) { this.reportName = reportName; }
    public void setDataSource(String dataSource) { this.dataSource = dataSource; }
    public void setHeaders(List<String> headers) { this.headers = headers; }
    public void setRows(List<Map<String, Object>> rows) { this.rows = rows; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    public void setColumns(List<String> columns) { this.columns = columns; }
    public void setData(List<Map<String, Object>> data) { this.data = data; }

    public static ReportDataBuilder builder() {
        return new ReportDataBuilder();
    }

    public static class ReportDataBuilder {
        private String reportId;
        private String reportName;
        private String dataSource;
        private List<String> headers;
        private List<Map<String, Object>> rows;
        private Map<String, Object> metadata;
        private List<String> columns;
        private List<Map<String, Object>> data;

        public ReportDataBuilder reportId(String reportId) {
            this.reportId = reportId;
            return this;
        }

        public ReportDataBuilder reportName(String reportName) {
            this.reportName = reportName;
            return this;
        }

        public ReportDataBuilder dataSource(String dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public ReportDataBuilder headers(List<String> headers) {
            this.headers = headers;
            this.columns = headers;
            return this;
        }

        public ReportDataBuilder rows(List<Map<String, Object>> rows) {
            this.rows = rows;
            this.data = rows;
            return this;
        }

        public ReportDataBuilder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public ReportDataBuilder columns(List<String> columns) {
            this.columns = columns;
            this.headers = columns;
            return this;
        }

        public ReportDataBuilder data(List<Map<String, Object>> data) {
            this.data = data;
            this.rows = data;
            return this;
        }

        public ReportData build() {
            ReportData reportData = new ReportData();
            reportData.setReportId(reportId);
            reportData.setReportName(reportName);
            reportData.setDataSource(dataSource);
            reportData.setHeaders(headers);
            reportData.setRows(rows);
            reportData.setMetadata(metadata);
            reportData.setColumns(columns);
            reportData.setData(data);
            return reportData;
        }
    }
}
