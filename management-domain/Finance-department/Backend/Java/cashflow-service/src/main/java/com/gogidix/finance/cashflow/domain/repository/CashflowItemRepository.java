package com.gogidix.finance.cashflow.domain.repository;

import com.gogidix.finance.cashflow.domain.model.CashflowItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Cashflow Item Repository Interface (Port)
 * Defines the contract for cashflow item persistence operations
 */
public interface CashflowItemRepository {

    CashflowItem save(CashflowItem item);

    List<CashflowItem> saveAll(List<CashflowItem> items);

    Optional<CashflowItem> findById(String id);

    Optional<CashflowItem> findByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId);

    List<CashflowItem> findByTenantId(String tenantId);

    List<CashflowItem> findByTenantIdAndType(String tenantId, CashflowItem.CashflowType type);

    List<CashflowItem> findByTenantIdAndCategory(String tenantId, CashflowItem.CashflowCategory category);

    List<CashflowItem> findByTenantIdAndStatus(String tenantId, CashflowItem.ItemStatus status);

    List<CashflowItem> findByTenantIdAndTransactionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<CashflowItem> findByTenantIdAndExpectedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<CashflowItem> findByTenantIdAndSettledDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<CashflowItem> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    List<CashflowItem> findByTenantIdAndProjectId(String tenantId, String projectId);

    List<CashflowItem> findByTenantIdAndAccount(String tenantId, String account);

    List<CashflowItem> findByTenantIdAndRecurringTrue(String tenantId);

    List<CashflowItem> findByTenantIdAndParentRecurringItemId(String tenantId, String parentRecurringItemId);

    List<CashflowItem> findByTenantIdAndStatusAndExpectedDateBefore(
            String tenantId, CashflowItem.ItemStatus status, LocalDate date);

    List<CashflowItem> findByTenantIdAndTypeIn(String tenantId, List<CashflowItem.CashflowType> types);

    List<CashflowItem> findByTenantIdAndCategoryIn(String tenantId, List<CashflowItem.CashflowCategory> categories);

    List<CashflowItem> findByTenantIdAndReference(String tenantId, String reference);

    List<CashflowItem> findByTenantIdAndCounterparty(String tenantId, String counterparty);

    List<CashflowItem> findByTenantIdAndLinkedExpenseId(String tenantId, String linkedExpenseId);

    List<CashflowItem> findByTenantIdAndLinkedRevenueId(String tenantId, String linkedRevenueId);

    boolean existsByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId);

    void deleteById(String id);

    void deleteByCashflowItemIdAndTenantId(String cashflowItemId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CashflowItem.ItemStatus status);

    long countByTenantIdAndType(String tenantId, CashflowItem.CashflowType type);

    BigDecimal sumAmountByTenantIdAndTypeAndStatus(
            String tenantId, CashflowItem.CashflowType type, CashflowItem.ItemStatus status);

    BigDecimal sumAmountByTenantIdAndDateRange(
            String tenantId, LocalDate startDate, LocalDate endDate, CashflowItem.CashflowType type);

    List<CashflowItem> findByTenantIdAndTagsContaining(String tenantId, String tag);
}
