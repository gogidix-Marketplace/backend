package com.gogidix.hr.performancereview.interfaces.rest;

import com.gogidix.hr.performancereview.application.service.PerformanceReviewService;
import com.gogidix.hr.performancereview.domain.model.PerformanceReview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/performance-reviews")
@RequiredArgsConstructor
public class PerformanceReviewController {
    private final PerformanceReviewService service;

    @PostMapping
    public ResponseEntity<PerformanceReview> create(@RequestBody PerformanceReview entity) {
        return ResponseEntity.ok(service.create(entity));
    }

    @GetMapping
    public ResponseEntity<List<PerformanceReview>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PerformanceReview> getById(@PathVariable String id) {
        PerformanceReview result = service.getById(id);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PerformanceReview> update(@PathVariable String id, @RequestBody PerformanceReview entity) {
        return ResponseEntity.ok(service.update(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
