package com.gogidix.aiservices.aifrauddetectionservice.application.dto;

import com.gogidix.aiservices.aifrauddetectionservice.application.query.GetComplianceReportQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for Compliance Report results.
 * Contains compliance and audit information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceReportDTO {

    private String reportId;

    private GetComplianceReportQuery.ReportType reportType;

    private Instant startDate;

    private Instant endDate;

    private Instant generatedAt;

    private String tenantId;

    private Summary summary;

    private List<ReportSection> sections;

    private Map<String, Object> metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private int totalAnalyses;
        private int fraudulentTransactions;
        private int suspiciousTransactions;
        private int legitimateTransactions;
        private double fraudRate;
        private double averageFraudScore;
        private int totalPatterns;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
        private String title;
        private String content;
        private Map<String, Object> data;
        private List<String> findings;
    }
}
