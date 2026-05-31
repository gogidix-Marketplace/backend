package com.gogidix.finance.accountsreceivable.domain.repository;

import com.gogidix.finance.accountsreceivable.domain.model.CreditMemo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Credit Memo Repository Interface (Port)
 * Defines the contract for credit memo persistence operations
 */
public interface CreditMemoRepository {

    CreditMemo save(CreditMemo creditMemo);

    List<CreditMemo> saveAll(List<CreditMemo> creditMemos);

    Optional<CreditMemo> findById(String id);

    Optional<CreditMemo> findByCreditMemoIdAndTenantId(String creditMemoId, String tenantId);

    Optional<CreditMemo> findByCreditMemoNumberAndTenantId(String creditMemoNumber, String tenantId);

    List<CreditMemo> findByTenantId(String tenantId);

    List<CreditMemo> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<CreditMemo> findByTenantIdAndStatus(String tenantId, CreditMemo.CreditMemoStatus status);

    List<CreditMemo> findByTenantIdAndCreditMemoType(String tenantId, CreditMemo.CreditMemoType creditMemoType);

    List<CreditMemo> findByTenantIdAndReferenceInvoiceId(String tenantId, String referenceInvoiceId);

    List<CreditMemo> findAvailableCreditMemosByTenantIdAndCustomerId(String tenantId, String customerId);

    List<CreditMemo> findExpiringCreditMemosByTenantId(String tenantId, LocalDate expirationDate);

    List<CreditMemo> findByTenantIdAndCreditMemoDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<CreditMemo> findByTenantIdAndTagsContaining(String tenantId, String tag);

    boolean existsByCreditMemoNumberAndTenantId(String creditMemoNumber, String tenantId);

    void deleteById(String id);

    void deleteByCreditMemoIdAndTenantId(String creditMemoId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CreditMemo.CreditMemoStatus status);

    BigDecimal sumBalanceRemainingByTenantIdAndCustomerId(String tenantId, String customerId);

    BigDecimal sumTotalAmountByTenantId(String tenantId);
}
