package com.gogidix.digitalmarketing.budgetmanagement.domain.repository;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.Budget;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends MongoRepository<Budget, String> {
    List<Budget> findByTenantId(String tenantId);
}