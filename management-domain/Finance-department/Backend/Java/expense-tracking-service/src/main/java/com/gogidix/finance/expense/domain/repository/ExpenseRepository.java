package com.gogidix.finance.expense.domain.repository;

import com.gogidix.finance.expense.domain.model.Expense;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByTenantId(String tenantId);
    List<Expense> findByTenantIdAndStatus(String tenantId, String status);
    List<Expense> findByTenantIdAndCategory(String tenantId, String category);
    List<Expense> findBySubmittedBy(String submittedBy);
}
