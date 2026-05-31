package com.gogidix.finance.revenuetracking.interfaces.rest;

import com.gogidix.finance.revenuetracking.application.service.RevenueTrackingService;
import com.gogidix.finance.revenue.domain.model.Revenue;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/revenues")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueTrackingService service;

    @PostMapping
    public ResponseEntity<Revenue> create(@RequestBody Revenue entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<Revenue>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Revenue> getById(@PathVariable String id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<Revenue>> getByTenantId(@PathVariable String tenantId) {
        return ResponseEntity.ok(service.getByTenantId(tenantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
