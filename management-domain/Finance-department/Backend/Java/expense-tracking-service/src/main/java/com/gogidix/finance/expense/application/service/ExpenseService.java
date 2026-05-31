package com.gogidix.finance.expense.application.service;

import com.gogidix.finance.expense.domain.model.Expense;
import com.gogidix.finance.expense.domain.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository repository;

    public Expense create(Expense expense) { return repository.save(expense); }
    public Expense getById(String id) { return repository.findById(id).orElse(null); }
    public List<Expense> getAll() { return repository.findAll(); }
    public List<Expense> getByTenantId(String tenantId) { return repository.findByTenantId(tenantId); }
    public Expense update(Expense expense) { return repository.save(expense); }
    public void delete(String id) { repository.deleteById(id); }
}
