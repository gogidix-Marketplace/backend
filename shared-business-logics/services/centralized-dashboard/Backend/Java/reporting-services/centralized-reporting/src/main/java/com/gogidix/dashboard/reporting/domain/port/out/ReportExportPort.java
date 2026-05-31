package com.gogidix.dashboard.reporting.domain.port.out;

import com.gogidix.dashboard.reporting.domain.model.*;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Report Export Port
 * 
 * Output port for report export operations
 * Defines contract for converting reports to different formats
 */
public interface ReportExportPort {

    /**
     * Export report to specified format
     */
    ReportExport exportReport(Report report, ExportFormat format, ExportOptions options);

    /**
     * Export report to PDF
     */
    ReportExport exportToPdf(Report report, PdfOptions options);

    /**
     * Export report to Excel
     */
    ReportExport exportToExcel(Report report, ExcelOptions options);

    /**
     * Export report to CSV
     */
    ReportExport exportToCsv(Report report, CsvOptions options);

    /**
     * Export report to JSON
     */
    ReportExport exportToJson(Report report);

    /**
     * Get export as stream
     */
    InputStream getExportStream(ReportExport export);

    /**
     * Get supported export formats for report type
     */
    List<ExportFormat> getSupportedFormats(ReportType reportType);

    /**
     * Validate export format compatibility
     */
    boolean isFormatSupported(ReportType reportType, ExportFormat format);

    /**
     * Get export file size estimate
     */
    long estimateExportSize(Report report, ExportFormat format);

    /**
     * Batch export multiple reports
     */
    List<ReportExport> batchExport(List<Report> reports, ExportFormat format, ExportOptions options);

    /**
     * Create export archive (ZIP) containing multiple reports
     */
    ReportExport createExportArchive(List<Report> reports, String archiveName);

    /**
     * Clean up temporary export files
     */
    void cleanupExports(List<ReportExport> exports);

    // Export Options classes
    interface ExportOptions {
        Map<String, Object> getOptions();
    }

    class PdfOptions implements ExportOptions {
        private final boolean includeCharts;
        private final boolean includeMetadata;
        private final String pageSize;
        private final String orientation;

        public PdfOptions(boolean includeCharts, boolean includeMetadata, String pageSize, String orientation) {
            this.includeCharts = includeCharts;
            this.includeMetadata = includeMetadata;
            this.pageSize = pageSize;
            this.orientation = orientation;
        }

        public boolean isIncludeCharts() { return includeCharts; }
        public boolean isIncludeMetadata() { return includeMetadata; }
        public String getPageSize() { return pageSize; }
        public String getOrientation() { return orientation; }

        @Override
        public Map<String, Object> getOptions() {
            return Map.of(
                "includeCharts", includeCharts,
                "includeMetadata", includeMetadata,
                "pageSize", pageSize,
                "orientation", orientation
            );
        }
    }

    class ExcelOptions implements ExportOptions {
        private final boolean separateSheets;
        private final boolean includeCharts;
        private final boolean includeFormulas;

        public ExcelOptions(boolean separateSheets, boolean includeCharts, boolean includeFormulas) {
            this.separateSheets = separateSheets;
            this.includeCharts = includeCharts;
            this.includeFormulas = includeFormulas;
        }

        public boolean isSeparateSheets() { return separateSheets; }
        public boolean isIncludeCharts() { return includeCharts; }
        public boolean isIncludeFormulas() { return includeFormulas; }

        @Override
        public Map<String, Object> getOptions() {
            return Map.of(
                "separateSheets", separateSheets,
                "includeCharts", includeCharts,
                "includeFormulas", includeFormulas
            );
        }
    }

    class CsvOptions implements ExportOptions {
        private final String delimiter;
        private final boolean includeHeaders;
        private final String encoding;

        public CsvOptions(String delimiter, boolean includeHeaders, String encoding) {
            this.delimiter = delimiter;
            this.includeHeaders = includeHeaders;
            this.encoding = encoding;
        }

        public String getDelimiter() { return delimiter; }
        public boolean isIncludeHeaders() { return includeHeaders; }
        public String getEncoding() { return encoding; }

        @Override
        public Map<String, Object> getOptions() {
            return Map.of(
                "delimiter", delimiter,
                "includeHeaders", includeHeaders,
                "encoding", encoding
            );
        }
    }
}