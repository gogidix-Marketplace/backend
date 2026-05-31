package com.gogidix.finance.budgetmanagement.application.service;

import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import com.gogidix.finance.budgetmanagement.domain.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository repository;

    public Budget create(Budget budget) { return repository.save(budget); }
    public Budget getById(String id) { return repository.findById(id).orElse(null); }
    public List<Budget> getAll() { return repository.findAll(); }
    public List<Budget> getByTenantId(String tenantId) { return repository.findByTenantId(tenantId); }
    public Budget update(Budget budget) { return repository.save(budget); }
    public void delete(String id) { repository.deleteById(id); }
}
