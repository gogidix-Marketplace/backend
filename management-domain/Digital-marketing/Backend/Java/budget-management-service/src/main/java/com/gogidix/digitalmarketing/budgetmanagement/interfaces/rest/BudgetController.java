package com.gogidix.digitalmarketing.budgetmanagement.interfaces.rest;

import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetRequestDto;
import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetResponseDto;
import com.gogidix.digitalmarketing.budgetmanagement.application.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@Tag(name = "Budget Management", description = "Budget Management")
public class BudgetController {

    private final BudgetService service;

    @PostMapping
    @Operation(summary = "Create a new Budget")
    public ResponseEntity<BudgetResponseDto> create(@RequestBody BudgetRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Budget by ID")
    public ResponseEntity<BudgetResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all Budgets")
    public ResponseEntity<List<BudgetResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Budget")
    public ResponseEntity<BudgetResponseDto> update(@PathVariable String id, @RequestBody BudgetRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Budget")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}