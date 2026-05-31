package com.gogidix.dashboard.reporting.domain.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Report Output Value Object
 */
public class ReportOutput {
    private final ReportId reportId;
    private final String title;
    private final List<SectionOutput> sections;
    private final LocalDateTime generatedAt;

    public ReportOutput(ReportId reportId, String title, List<SectionOutput> sections, LocalDateTime generatedAt) {
        this.reportId = reportId;
        this.title = title;
        this.sections = sections;
        this.generatedAt = generatedAt;
    }

    public ReportId getReportId() {
        return reportId;
    }

    public ReportId getId() {
        return reportId;
    }

    public String getTitle() {
        return title;
    }

    public OutputFormat getOutputFormat() {
        return OutputFormat.PDF;
    }

    public List<SectionOutput> getSections() {
        return sections;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Get file path for the generated report
     */
    public String getFilePath() {
        return "/tmp/reports/" + reportId + ".pdf";
    }

    /**
     * Static builder factory method
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for ReportOutput
     */
    public static class Builder {
        private ReportId reportId;
        private String title;
        private OutputFormat format;
        private LocalDateTime generatedAt;
        private List<SectionOutput> sectionOutputs = new java.util.ArrayList<>();
        private int totalPages;
        private long fileSize;

        public Builder withReportId(ReportId reportId) {
            this.reportId = reportId;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withFormat(OutputFormat format) {
            this.format = format;
            return this;
        }

        public Builder withGeneratedAt(LocalDateTime generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder addSectionOutput(SectionOutput sectionOutput) {
            this.sectionOutputs.add(sectionOutput);
            return this;
        }

        public Builder withTotalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder withFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public ReportOutput build() {
            String defaultTitle = "Report " + (reportId != null ? reportId.toString() : "");
            return new ReportOutput(
                reportId,
                title != null ? title : defaultTitle,
                sectionOutputs,
                generatedAt != null ? generatedAt : LocalDateTime.now()
            );
        }
    }
}
