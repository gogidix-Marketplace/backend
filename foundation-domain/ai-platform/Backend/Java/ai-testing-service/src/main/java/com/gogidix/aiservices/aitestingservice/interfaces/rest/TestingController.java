package com.gogidix.aiservices.aitestingservice.interfaces.rest;
import com.gogidix.aiservices.aitestingservice.application.service.TestSuiteService;
import com.gogidix.aiservices.aitestingservice.domain.model.TestRun;
import com.gogidix.aiservices.aitestingservice.domain.model.TestSuite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/v1/testing")
public class TestingController {
    private final TestSuiteService service;
    public TestingController(TestSuiteService service) { this.service = service; }
    @PostMapping("/suites")
    public ResponseEntity<TestSuite> createSuite(@RequestParam String tenantId, @RequestParam String name) {
        TestSuite suite = service.createSuite(tenantId, name);
        return ResponseEntity.created(URI.create("/api/v1/testing/suites/" + suite.getSuiteId())).body(suite);
    }
    @GetMapping("/suites/{suiteId}")
    public ResponseEntity<TestSuite> getSuite(@PathVariable String suiteId, @RequestParam String tenantId) {
        return ResponseEntity.ok(service.getSuiteById(suiteId, tenantId));
    }
    @GetMapping("/suites")
    public ResponseEntity<List<TestSuite>> listSuites(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getSuitesByTenant(tenantId));
    }
    @PostMapping("/suites/{suiteId}/run")
    public ResponseEntity<TestRun> runSuite(@PathVariable String suiteId, @RequestParam String tenantId) {
        return ResponseEntity.ok(service.runSuite(suiteId, tenantId));
    }
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "AI Testing Service is running"));
    }
    public record HealthResponse(String status, String message) {}
}
