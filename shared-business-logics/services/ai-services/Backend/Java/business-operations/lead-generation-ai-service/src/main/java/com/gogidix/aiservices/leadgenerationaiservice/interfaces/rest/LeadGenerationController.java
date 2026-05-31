package com.gogidix.aiservices.leadgenerationaiservice.interfaces.rest;

import com.gogidix.aiservices.leadgenerationaiservice.application.dto.request.*;
import com.gogidix.aiservices.leadgenerationaiservice.application.dto.response.*;
import com.gogidix.aiservices.leadgenerationaiservice.application.service.LeadGenerationService;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.LeadStatus;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadGenerationException;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/leads")
@RequiredArgsConstructor
@Validated
public class LeadGenerationController {

    private final LeadGenerationService leadGenerationService;

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
        LeadResponse response = leadGenerationService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadResponse> getLead(@PathVariable String id) {
        LeadResponse response = leadGenerationService.getLead(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> listLeads(
            @RequestParam(required = false) LeadStatus status,
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) Integer top) {

        if (status != null) {
            return ResponseEntity.ok(leadGenerationService.getLeadsByStatus(status));
        } else if (ownerId != null) {
            return ResponseEntity.ok(leadGenerationService.getLeadsByOwner(ownerId));
        } else if (top != null) {
            return ResponseEntity.ok(leadGenerationService.getTopLeads(top));
        }

        return ResponseEntity.ok(List.of());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<LeadResponse> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateLeadStatusRequest request) {
        // Override lead ID in request with path variable
        request.setLeadId(id);
        LeadResponse response = leadGenerationService.updateStatus(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<LeadResponse> assignLead(
            @PathVariable String id,
            @Valid @RequestBody AssignLeadRequest request) {
        request.setLeadId(id);
        LeadResponse response = leadGenerationService.assignLead(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/qualification")
    public ResponseEntity<LeadResponse> addQualification(
            @PathVariable String id,
            @Valid @RequestBody AddQualificationRequest request) {
        request.setLeadId(id);
        LeadResponse response = leadGenerationService.addQualification(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<ActivityResponse> addActivity(
            @PathVariable String id,
            @Valid @RequestBody AddActivityRequest request) {
        request.setLeadId(id);
        ActivityResponse response = leadGenerationService.addActivity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<List<ActivityResponse>> getActivities(@PathVariable String id) {
        return ResponseEntity.ok(leadGenerationService.getActivities(id));
    }

    @PostMapping("/{id}/score")
    public ResponseEntity<ScoreResponse> recalculateScore(@PathVariable String id) {
        ScoreResponse response = leadGenerationService.recalculateScore(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<ConversionResponse> convertLead(
            @PathVariable String id,
            @Valid @RequestBody ConvertLeadRequest request) {
        request.setLeadId(id);
        ConversionResponse response = leadGenerationService.convertLead(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable String id) {
        leadGenerationService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(LeadNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLeadNotFound(LeadNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(LeadGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleLeadGenerationException(LeadGenerationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
