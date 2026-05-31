package com.gogidix.aiservices.performanceoptimizationservice.interfaces.rest;
import com.gogidix.aiservices.performanceoptimizationservice.application.service.PerformanceAnalysisService;
import com.gogidix.aiservices.performanceoptimizationservice.domain.model.PerformanceAnalysis;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/v1/optimization")
public class OptimizationController {
    private final PerformanceAnalysisService service;
    public OptimizationController(PerformanceAnalysisService service) { this.service = service; }
    @PostMapping("/analyze")
    public ResponseEntity<PerformanceAnalysis> analyze(@RequestParam String tenantId, @RequestParam String serviceName) {
        PerformanceAnalysis analysis = service.analyze(tenantId, serviceName, List.of("cpu", "memory", "responseTime"));
        return ResponseEntity.created(URI.create("/api/v1/optimization/analyses/" + analysis.getAnalysisId())).body(analysis);
    }
    @GetMapping("/analyses/{analysisId}")
    public ResponseEntity<PerformanceAnalysis> getAnalysis(@PathVariable String analysisId, @RequestParam String tenantId) {
        return ResponseEntity.ok(service.getAnalysisById(analysisId, tenantId));
    }
    @DeleteMapping("/cache/{pattern}")
    public ResponseEntity<Void> clearCache(@PathVariable String pattern) {
        service.clearCache(pattern);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "Performance Optimization Service is running"));
    }
    public record HealthResponse(String status, String message) {}
}
