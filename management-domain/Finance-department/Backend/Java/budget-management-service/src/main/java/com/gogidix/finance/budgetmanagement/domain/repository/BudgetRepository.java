package com.gogidix.finance.budgetmanagement.domain.repository;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByTenantId(String tenantId);
    List<Budget> findByTenantIdAndStatus(String tenantId, Budget.BudgetStatus status);
    List<Budget> findByTenantIdAndFiscalYear(String tenantId, String fiscalYear);
    List<Budget> findByTenantIdAndDepartment(String tenantId, String department);
    long countByTenantIdAndStatus(String tenantId, Budget.BudgetStatus status);
    List<Budget> findActiveByTenantIdAndDateBetween(String tenantId, Instant start, Instant end);
}
