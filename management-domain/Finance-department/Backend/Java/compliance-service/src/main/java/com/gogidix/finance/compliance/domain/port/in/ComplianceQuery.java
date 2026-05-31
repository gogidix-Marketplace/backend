package com.gogidix.finance.compliance.domain.port.in;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceReport;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Compliance Queries (Input Port)
 * Defines the query operations for compliance data
 */
public interface ComplianceQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceRuleQuery {
        private String tenantId;

        private String ruleId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceRulesQuery {
        private String tenantId;

        private ComplianceRule.RuleType ruleType;

        private ComplianceRule.RuleCategory category;

        private ComplianceRule.RuleStatus status;

        private Boolean enabled;

        private String department;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceCheckQuery {
        private String tenantId;

        private String checkId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceChecksQuery {
        private String tenantId;

        private String ruleId;

        private String entityType;

        private String entityId;

        private ComplianceCheck.CheckResult result;

        private ComplianceCheck.SeverityLevel severity;

        private Boolean requiresAction;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceReportQuery {
        private String tenantId;

        private String reportId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceReportsQuery {
        private String tenantId;

        private ComplianceReport.ReportType reportType;

        private ComplianceReport.ReportStatus status;

        private LocalDate periodStart;

        private LocalDate periodEnd;

        private Boolean isArchived;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetComplianceSummaryQuery {
        private String tenantId;

        private LocalDate startDate;

        private LocalDate endDate;

        private String department;

        private String costCenter;

        private List<String> ruleIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetViolationsQuery {
        private String tenantId;

        private String ruleId;

        private ComplianceCheck.SeverityLevel minSeverity;

        private Boolean includeWaived;

        private Boolean includeRemediated;

        private LocalDate startDate;

        private LocalDate endDate;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingRemediationQuery {
        private String tenantId;

        private String assignedTo;

        private LocalDate dueBefore;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchRulesQuery {
        private String tenantId;

        private String searchTerm;

        private ComplianceRule.RuleType ruleType;

        private ComplianceRule.RuleCategory category;

        private ComplianceRule.SeverityLevel severity;

        private List<String> tags;

        private Integer page;

        private Integer size;
    }
}
