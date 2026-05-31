package com.gogidix.aiservices.aiworkflowautomationservice.interfaces.rest;
import com.gogidix.aiservices.aiworkflowautomationservice.application.service.AutomationService;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.AutomationAction;
import com.gogidix.aiservices.aiworkflowautomationservice.domain.model.Automation.TriggerType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/api/v1/automation")
public class AutomationController {
    private final AutomationService service;
    public AutomationController(AutomationService service) { this.service = service; }
    @PostMapping("/workflows")
    public ResponseEntity<Automation> createAutomation(@RequestParam String tenantId, @RequestParam String name,
                                                         @RequestParam TriggerType triggerType, @RequestParam String schedule) {
        Automation automation = service.createAutomation(tenantId, name, triggerType, schedule, List.of());
        return ResponseEntity.created(URI.create("/api/v1/automation/workflows/" + automation.getAutomationId())).body(automation);
    }
    @GetMapping("/workflows/{automationId}")
    public ResponseEntity<Automation> getAutomation(@PathVariable String automationId, @RequestParam String tenantId) {
        return ResponseEntity.ok(service.getAutomationById(automationId, tenantId));
    }
    @GetMapping("/workflows")
    public ResponseEntity<List<Automation>> listAutomations(@RequestParam String tenantId) {
        return ResponseEntity.ok(service.getAutomationsByTenant(tenantId));
    }
    @PostMapping("/workflows/{automationId}/trigger")
    public ResponseEntity<Void> triggerAutomation(@PathVariable String automationId, @RequestParam String tenantId) {
        service.triggerAutomation(automationId, tenantId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "AI Workflow Automation Service is running"));
    }
    public record HealthResponse(String status, String message) {}
}
