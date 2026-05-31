package com.gogidix.sysadmin.performancemetrics.interfaces.rest;
import com.gogidix.sysadmin.performancemetrics.application.service.PerformanceMetricService;
import com.gogidix.sysadmin.performancemetrics.domain.model.PerformanceMetric;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/performance-metrics")
@RequiredArgsConstructor
public class PerformanceMetricController {
    private final PerformanceMetricService service;
    @PostMapping
    public ResponseEntity<PerformanceMetric> create(@RequestBody PerformanceMetric entity) { return ResponseEntity.ok(service.create(entity)); }
    @GetMapping
    public ResponseEntity<List<PerformanceMetric>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @GetMapping(("/{id}"))
    public ResponseEntity<PerformanceMetric> getById(@PathVariable String id) { PerformanceMetric result = service.getById(id); return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build(); }
    @DeleteMapping(("/{id}"))
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
