package com.gogidix.finance.bankreconciliation.domain.port.in;

import com.gogidix.finance.bankreconciliation.domain.model.BankAccount;
import com.gogidix.finance.bankreconciliation.domain.model.BankStatement;
import com.gogidix.finance.bankreconciliation.domain.model.Reconciliation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Reconciliation Queries (Input Port)
 * Defines the query operations for reconciliation data
 */
public interface ReconciliationQuery {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBankAccountQuery {
        private String tenantId;
        private String accountId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBankAccountsByTenantQuery {
        private String tenantId;
        private BankAccount.AccountStatus status;
        private BankAccount.AccountType accountType;
        private Boolean isPrimary;
        private Integer page;
        private Integer size;
        private String sortBy;
        private String sortDirection;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBankStatementQuery {
        private String tenantId;
        private String statementId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetBankStatementsByAccountQuery {
        private String tenantId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
        private BankStatement.ImportStatus importStatus;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationQuery {
        private String tenantId;
        private String reconciliationId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationsByAccountQuery {
        private String tenantId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
        private Reconciliation.ReconciliationStatus status;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationsByStatusQuery {
        private String tenantId;
        private Reconciliation.ReconciliationStatus status;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetPendingReconciliationsQuery {
        private String tenantId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationLinesQuery {
        private String tenantId;
        private String reconciliationId;
        private com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.MatchStatus matchStatus;
        private Boolean requiresManualReview;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetUnreconciledTransactionsQuery {
        private String tenantId;
        private String statementId;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationSummaryQuery {
        private String tenantId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class SearchBankTransactionsQuery {
        private String tenantId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
        private String searchTerm;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private BankTransactionType transactionType;
        private Boolean isReconciled;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetDiscrepanciesQuery {
        private String tenantId;
        private String reconciliationId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
        private com.gogidix.finance.bankreconciliation.domain.model.ReconciliationLine.DiscrepancyCategory discrepancyCategory;
        private Integer page;
        private Integer size;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class GetReconciliationStatisticsQuery {
        private String tenantId;
        private String accountId;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    enum BankTransactionType {
        DEBIT,
        CREDIT,
        TRANSFER_IN,
        TRANSFER_OUT,
        ALL
    }
}
