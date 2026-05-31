package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.LeadDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.domain.enums.LeadStatus;
import com.gogidix.corporatecms.domain.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for lead management.
 */
@Tag(name = "Leads", description = "Lead management APIs")
@RestController
@RequestMapping("/leads")
@RequiredArgsConstructor
public class LeadController {

    private final LeadService leadService;

    @Operation(summary = "Create lead", description = "Create a new lead")
    @PostMapping
    public ResponseEntity<ApiResponse<LeadDTO>> createLead(@Valid @RequestBody LeadDTO dto) {
        LeadDTO lead = leadService.createLead(dto);
        return ResponseEntity.status(201).body(ApiResponse.created(lead));
    }

    @Operation(summary = "Update lead", description = "Update an existing lead")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadDTO>> updateLead(
            @Parameter(description = "Lead ID") @PathVariable String id,
            @Valid @RequestBody LeadDTO dto) {
        LeadDTO lead = leadService.updateLead(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Lead updated successfully", lead));
    }

    @Operation(summary = "Get lead by ID", description = "Retrieve a lead by ID")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeadDTO>> getLeadById(
            @Parameter(description = "Lead ID") @PathVariable String id) {
        LeadDTO lead = leadService.getLeadById(id);
        return ResponseEntity.ok(ApiResponse.success(lead));
    }

    @Operation(summary = "Get leads by status", description = "Retrieve leads filtered by status")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<PageResponse<LeadDTO>>> getLeadsByStatus(
            @Parameter(description = "Lead status") @PathVariable LeadStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<LeadDTO> leads = leadService.getLeadsByStatus(status, page, size);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @Operation(summary = "Get leads by assigned user", description = "Retrieve leads assigned to a user")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/assigned/{assignedTo}")
    public ResponseEntity<ApiResponse<PageResponse<LeadDTO>>> getLeadsByAssignedUser(
            @Parameter(description = "Assigned user ID") @PathVariable String assignedTo,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<LeadDTO> leads = leadService.getLeadsByAssignedUser(assignedTo, page, size);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @Operation(summary = "Search leads", description = "Search leads by keyword")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<LeadDTO>>> searchLeads(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<LeadDTO> leads = leadService.searchLeads(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @Operation(summary = "Update lead status", description = "Change the status of a lead")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<LeadDTO>> updateStatus(
            @Parameter(description = "Lead ID") @PathVariable String id,
            @Parameter(description = "New status") @RequestParam LeadStatus status) {
        String userId = getCurrentUserId();
        LeadDTO lead = leadService.updateStatus(id, status, userId);
        return ResponseEntity.ok(ApiResponse.success("Lead status updated", lead));
    }

    @Operation(summary = "Assign lead", description = "Assign a lead to a user")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @PostMapping("/{id}/assign")
    public ResponseEntity<ApiResponse<LeadDTO>> assignLead(
            @Parameter(description = "Lead ID") @PathVariable String id,
            @Parameter(description = "Assigned user ID") @RequestParam String assignedTo,
            @Parameter(description = "Assigned user name") @RequestParam String assignedByName) {
        LeadDTO lead = leadService.assignLead(id, assignedTo, assignedByName);
        return ResponseEntity.ok(ApiResponse.success("Lead assigned successfully", lead));
    }

    @Operation(summary = "Update lead score", description = "Update the score of a lead")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @PatchMapping("/{id}/score")
    public ResponseEntity<ApiResponse<LeadDTO>> updateScore(
            @Parameter(description = "Lead ID") @PathVariable String id,
            @Parameter(description = "Score delta") @RequestParam Integer delta) {
        LeadDTO lead = leadService.updateScore(id, delta);
        return ResponseEntity.ok(ApiResponse.success("Lead score updated", lead));
    }

    @Operation(summary = "Get new leads", description = "Retrieve all new leads")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/new")
    public ResponseEntity<ApiResponse<List<LeadDTO>>> getNewLeads() {
        List<LeadDTO> leads = leadService.getNewLeads();
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @Operation(summary = "Get converted leads", description = "Retrieve converted leads within a date range")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/converted")
    public ResponseEntity<ApiResponse<List<LeadDTO>>> getConvertedLeads(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<LeadDTO> leads = leadService.getConvertedLeads(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(leads));
    }

    @Operation(summary = "Count leads by status", description = "Count leads by status")
    @PreAuthorize("hasAuthority('LEAD_MANAGE')")
    @GetMapping("/count/{status}")
    public ResponseEntity<ApiResponse<Long>> countLeadsByStatus(
            @Parameter(description = "Lead status") @PathVariable LeadStatus status) {
        Long count = leadService.countLeadsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    private String getCurrentUserId() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }
}
