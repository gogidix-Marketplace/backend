package com.gogidix.finance.budgetmanagement.interfaces.rest;

import com.gogidix.finance.budgetmanagement.application.service.BudgetService;
import com.gogidix.finance.budgetmanagement.domain.model.Budget;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService service;

    @PostMapping
    public ResponseEntity<Budget> create(@RequestBody Budget entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getById(@PathVariable String id) {
        Budget result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> update(@PathVariable String id, @RequestBody Budget entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Budget>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getByTenantId(tenantId));
    }
}
