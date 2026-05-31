package com.gogidix.finance.bankreconciliation.domain.repository;

import com.gogidix.finance.bankreconciliation.domain.model.BankTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Bank Transaction Repository Interface (Port)
 * Defines the contract for bank transaction persistence operations
 */
public interface BankTransactionRepository {

    BankTransaction save(BankTransaction transaction);

    List<BankTransaction> saveAll(List<BankTransaction> transactions);

    Optional<BankTransaction> findById(String id);

    Optional<BankTransaction> findByTransactionIdAndTenantId(String transactionId, String tenantId);

    List<BankTransaction> findByTenantId(String tenantId);

    List<BankTransaction> findByTenantIdAndAccountId(String tenantId, String accountId);

    List<BankTransaction> findByTenantIdAndAccountNumber(String tenantId, String accountNumber);

    List<BankTransaction> findByTenantIdAndStatementId(String tenantId, String statementId);

    List<BankTransaction> findByTenantIdAndTransactionDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<BankTransaction> findByTenantIdAndValueDateBetween(
            String tenantId, LocalDate startDate, LocalDate endDate);

    List<BankTransaction> findByTenantIdAndTransactionType(
            String tenantId, BankTransaction.TransactionType transactionType);

    List<BankTransaction> findByTenantIdAndIsReconciled(String tenantId, Boolean isReconciled);

    List<BankTransaction> findUnreconciledByTenantIdAndAccountId(String tenantId, String accountId);

    List<BankTransaction> findByTenantIdAndAccountIdAndTransactionDateBetween(
            String tenantId, String accountId, LocalDate startDate, LocalDate endDate);

    List<BankTransaction> findByTenantIdAndAmountBetween(
            String tenantId, BigDecimal minAmount, BigDecimal maxAmount);

    List<BankTransaction> findByTenantIdAndReferenceContaining(String tenantId, String reference);

    List<BankTransaction> findByTenantIdAndDescriptionContaining(
            String tenantId, String description);

    List<BankTransaction> findByTenantIdAndCounterpartyName(
            String tenantId, String counterpartyName);

    List<BankTransaction> findByReconciliationId(String reconciliationId);

    List<BankTransaction> findByReconciliationLineId(String reconciliationLineId);

    Optional<BankTransaction> findByTenantIdAndCheckNumber(
            String tenantId, String checkNumber);

    List<BankTransaction> findByTenantIdAndStatus(
            String tenantId, BankTransaction.TransactionStatus status);

    boolean existsByTransactionIdAndTenantId(String transactionId, String tenantId);

    void deleteById(String id);

    void deleteByTransactionIdAndTenantId(String transactionId, String tenantId);

    void deleteByStatementId(String statementId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndAccountId(String tenantId, String accountId);

    long countByTenantIdAndStatementId(String tenantId, String statementId);

    long countByTenantIdAndIsReconciled(String tenantId, Boolean isReconciled);

    BigDecimal sumAmountByTenantIdAndTransactionTypeAndTransactionDateBetween(
            String tenantId, BankTransaction.TransactionType transactionType,
            LocalDate startDate, LocalDate endDate);

    List<BankTransaction> findByTenantIdAndTagsContaining(String tenantId, String tag);

    List<BankTransaction> findByOriginalTransactionId(String originalTransactionId);
}
