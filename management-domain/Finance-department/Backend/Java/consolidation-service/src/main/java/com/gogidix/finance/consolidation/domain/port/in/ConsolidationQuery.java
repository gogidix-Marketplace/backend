package com.gogidix.finance.consolidation.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Consolidation Queries (Input Port)
 * Defines the query operations for consolidation data
 */
public interface ConsolidationQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationRuleQuery {
        private String tenantId;

        private String ruleId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetActiveConsolidationRulesQuery {
        private String tenantId;

        private LocalDate effectiveDate;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationRule.RuleType ruleType;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationJobQuery {
        private String tenantId;

        private String jobId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationJobsQuery {
        private String tenantId;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationJob.JobStatus status;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationJob.JobType jobType;

        private LocalDate fromDate;

        private LocalDate toDate;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidatedBalancesQuery {
        private String tenantId;

        private String jobId;

        private LocalDate asOfDate;

        private String accountCode;

        private String subsidiaryId;

        private String departmentId;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationReportQuery {
        private String tenantId;

        private String reportId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationReportsQuery {
        private String tenantId;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationReport.ReportType reportType;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationReport.ReportStatus status;

        private LocalDate periodStart;

        private LocalDate periodEnd;

        private Integer page;

        private Integer size;

        private String sortBy;

        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetTrialBalanceQuery {
        private String tenantId;

        private LocalDate asOfDate;

        private String baseCurrency;

        private List<String> subsidiaryIds;

        private List<String> departmentIds;

        private Boolean includeIntercompany;

        private Boolean includeEliminations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetFinancialStatementsQuery {
        private String tenantId;

        private LocalDate periodEnd;

        private LocalDate periodStart;

        private String baseCurrency;

        private List<String> subsidiaryIds;

        private Boolean includeComparatives;

        private Integer comparativePeriods;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetIntercompanyTransactionsQuery {
        private String tenantId;

        private LocalDate periodStart;

        private LocalDate periodEnd;

        private String entity1Id;

        private String entity2Id;

        private Boolean onlyUneliminated;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetConsolidationSummaryQuery {
        private String tenantId;

        private LocalDate periodEnd;

        private String baseCurrency;

        private List<String> subsidiaryIds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchRulesQuery {
        private String tenantId;

        private String searchTerm;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationRule.RuleType ruleType;

        private Boolean active;

        private Integer page;

        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetJobMetricsQuery {
        private String tenantId;

        private String jobId;

        private Boolean includeSteps;

        private Boolean includeLogs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetJobHistoryQuery {
        private String tenantId;

        private LocalDate periodStart;

        private LocalDate periodEnd;

        private com.gogidix.finance.consolidation.domain.model.ConsolidationJob.JobType jobType;

        private Integer page;

        private Integer size;
    }
}
