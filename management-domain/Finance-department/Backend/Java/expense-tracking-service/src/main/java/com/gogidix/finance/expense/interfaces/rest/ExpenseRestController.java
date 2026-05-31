package com.gogidix.finance.expense.interfaces.rest;

import com.gogidix.finance.expense.application.service.ExpenseService;
import com.gogidix.finance.expense.domain.model.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseRestController {
    private final ExpenseService service;

    @PostMapping
    public ResponseEntity<Expense> create(@RequestBody Expense entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getById(@PathVariable String id) {
        Expense result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Expense> update(@PathVariable String id, @RequestBody Expense entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Expense>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getByTenantId(tenantId));
    }
}
