package com.gogidix.aiservices.aiorchestrationservice.interfaces.rest;

import com.gogidix.aiservices.aiorchestrationservice.application.dto.CreateWorkflowRequestDto;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.WorkflowExecutionResponseDto;
import com.gogidix.aiservices.aiorchestrationservice.application.dto.WorkflowResponseDto;
import com.gogidix.aiservices.aiorchestrationservice.application.service.WorkflowApplicationService;
import com.gogidix.aiservices.aiorchestrationservice.domain.model.WorkflowExecution;
import com.gogidix.aiservices.aiorchestrationservice.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orchestration")
@Tag(name = "Workflow Orchestration", description = "APIs for workflow orchestration")
public class OrchestrationController {
    private final WorkflowApplicationService service;

    public OrchestrationController(WorkflowApplicationService service) {
        this.service = service;
    }

    @PostMapping("/workflows")
    public ResponseEntity<WorkflowResponseDto> createWorkflow(@Valid @RequestBody CreateWorkflowRequestDto request) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        WorkflowResponseDto response = service.createWorkflow(tenantId, request);
        return ResponseEntity.created(URI.create("/api/v1/orchestration/workflows/" + response.workflowId())).body(response);
    }

    @GetMapping("/workflows/{workflowId}")
    public ResponseEntity<WorkflowResponseDto> getWorkflow(@PathVariable String workflowId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        return ResponseEntity.ok(service.getWorkflowById(workflowId, tenantId));
    }

    @GetMapping("/workflows")
    public ResponseEntity<List<WorkflowResponseDto>> listWorkflows() {
        String tenantId = RequestContextHolder.getContext().tenantId();
        return ResponseEntity.ok(service.getWorkflowsByTenant(tenantId));
    }

    @DeleteMapping("/workflows/{workflowId}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable String workflowId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        service.deleteWorkflow(workflowId, tenantId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/workflows/{workflowId}/execute")
    public ResponseEntity<WorkflowExecutionResponseDto> executeWorkflow(@PathVariable String workflowId) {
        String tenantId = RequestContextHolder.getContext().tenantId();
        WorkflowExecution execution = service.executeWorkflow(workflowId, tenantId);
        return ResponseEntity.ok(WorkflowExecutionResponseDto.from(execution));
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("UP", "AI Orchestration Service is running"));
    }

    public record HealthResponse(String status, String message) {}
}
