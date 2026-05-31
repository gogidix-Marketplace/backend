package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.application.dto.WorkflowDTO;
import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import com.gogidix.corporatecms.domain.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for workflow management.
 */
@Tag(name = "Workflow", description = "Workflow and approval APIs")
@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowService workflowService;

    @Operation(summary = "Initiate workflow", description = "Start a new approval workflow for content")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PostMapping("/content/{contentId}")
    public ResponseEntity<ApiResponse<WorkflowDTO>> initiateWorkflow(
            @Parameter(description = "Content ID") @PathVariable String contentId,
            @Parameter(description = "Comment") @RequestParam(required = false) String comment) {
        String userId = getCurrentUserId();
        WorkflowDTO workflow = workflowService.initiateWorkflow(contentId, userId, comment);
        return ResponseEntity.ok(ApiResponse.created(workflow));
    }

    @Operation(summary = "Get workflow by content ID", description = "Retrieve the workflow for a specific content item")
    @GetMapping("/content/{contentId}")
    @PreAuthorize("hasAuthority('CONTENT_READ')")
    public ResponseEntity<ApiResponse<WorkflowDTO>> getWorkflowByContentId(
            @Parameter(description = "Content ID") @PathVariable String contentId) {
        WorkflowDTO workflow = workflowService.getWorkflowByContentId(contentId);
        return ResponseEntity.ok(ApiResponse.success(workflow));
    }

    @Operation(summary = "Get workflows by status", description = "Retrieve workflows filtered by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('CONTENT_READ')")
    public ResponseEntity<ApiResponse<PageResponse<WorkflowDTO>>> getWorkflowsByStatus(
            @Parameter(description = "Workflow status") @PathVariable WorkflowStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<WorkflowDTO> workflows = workflowService.getWorkflowsByStatus(status, page, size);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    @Operation(summary = "Get pending approvals", description = "Get workflows pending approval for the current user")
    @GetMapping("/pending-approvals")
    @PreAuthorize("hasAuthority('CONTENT_APPROVE')")
    public ResponseEntity<ApiResponse<List<WorkflowDTO>>> getPendingApprovals() {
        String userId = getCurrentUserId();
        List<WorkflowDTO> workflows = workflowService.getPendingApprovalsForUser(userId);
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    @Operation(summary = "Approve workflow", description = "Approve a workflow step")
    @PreAuthorize("hasAuthority('CONTENT_APPROVE')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<WorkflowDTO>> approveWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable String id,
            @Parameter(description = "Comment") @RequestParam(required = false) String comment) {
        String userId = getCurrentUserId();
        WorkflowDTO workflow = workflowService.approveWorkflow(id, userId, comment);
        return ResponseEntity.ok(ApiResponse.success("Workflow approved", workflow));
    }

    @Operation(summary = "Reject workflow", description = "Reject a workflow")
    @PreAuthorize("hasAuthority('CONTENT_APPROVE')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<WorkflowDTO>> rejectWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable String id,
            @Parameter(description = "Comment") @RequestParam String comment) {
        String userId = getCurrentUserId();
        WorkflowDTO workflow = workflowService.rejectWorkflow(id, userId, comment);
        return ResponseEntity.ok(ApiResponse.success("Workflow rejected", workflow));
    }

    @Operation(summary = "Cancel workflow", description = "Cancel a workflow")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<WorkflowDTO>> cancelWorkflow(
            @Parameter(description = "Workflow ID") @PathVariable String id,
            @Parameter(description = "Comment") @RequestParam(required = false) String comment) {
        String userId = getCurrentUserId();
        WorkflowDTO workflow = workflowService.cancelWorkflow(id, userId, comment);
        return ResponseEntity.ok(ApiResponse.success("Workflow cancelled", workflow));
    }

    @Operation(summary = "Get overdue workflows", description = "Get all overdue workflows")
    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('CONTENT_APPROVE')")
    public ResponseEntity<ApiResponse<List<WorkflowDTO>>> getOverdueWorkflows() {
        List<WorkflowDTO> workflows = workflowService.getOverdueWorkflows();
        return ResponseEntity.ok(ApiResponse.success(workflows));
    }

    private String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }
}
