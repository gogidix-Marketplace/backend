package com.gogidix.transaction.onboarding.controller;

import com.gogidix.transaction.onboarding.domain.entity.OnboardingTracker;
import com.gogidix.transaction.onboarding.dto.*;
import com.gogidix.transaction.onboarding.service.OnboardingTrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
@Slf4j
public class OnboardingTrackerController {

    private final OnboardingTrackerService onboardingTrackerService;

    @PostMapping
    public ResponseEntity<OnboardingTrackerResponse> createOnboarding(@Valid @RequestBody OnboardingCreateRequest request) {
        log.info("Creating onboarding for entity: {}", request.getEntityId());
        OnboardingTrackerResponse response = onboardingTrackerService.createOnboarding(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OnboardingTrackerResponse> getOnboardingById(@PathVariable UUID id) {
        log.info("Fetching onboarding by ID: {}", id);
        OnboardingTrackerResponse response = onboardingTrackerService.getOnboardingById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<OnboardingTrackerResponse> getOnboardingByTransactionId(@PathVariable UUID transactionId) {
        log.info("Fetching onboarding for transaction: {}", transactionId);
        OnboardingTrackerResponse response = onboardingTrackerService.getOnboardingByTransactionId(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    public ResponseEntity<OnboardingTrackerResponse> getOnboardingByEntity(
            @PathVariable OnboardingTracker.EntityType entityType,
            @PathVariable String entityId) {
        log.info("Fetching onboarding for entity: {} - {}", entityType, entityId);
        OnboardingTrackerResponse response = onboardingTrackerService.getOnboardingByEntity(entityType, entityId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/transition")
    public ResponseEntity<OnboardingTrackerResponse> transitionToNextStage(
            @PathVariable UUID id,
            @Valid @RequestBody StageTransitionRequest request) {
        log.info("Transitioning onboarding {} to stage: {}", id, request.getNextStage());
        OnboardingTrackerResponse response = onboardingTrackerService.transitionToNextStage(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/data")
    public ResponseEntity<OnboardingTrackerResponse> updateOnboardingData(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> data) {
        log.info("Updating onboarding data for: {}", id);
        OnboardingTrackerResponse response = onboardingTrackerService.updateOnboardingData(id, data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<Page<OnboardingTrackerResponse>> searchOnboardings(
            @RequestBody OnboardingSearchRequest searchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("Searching onboardings");
        Page<OnboardingTrackerResponse> response = onboardingTrackerService.searchOnboardings(searchRequest, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<OnboardingStageHistoryResponse>> getStageHistory(@PathVariable UUID id) {
        log.info("Fetching stage history for onboarding: {}", id);
        List<OnboardingStageHistoryResponse> response = onboardingTrackerService.getStageHistory(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stuck")
    public ResponseEntity<List<OnboardingTrackerResponse>> getStuckOnboardings(
            @RequestParam(defaultValue = "24") int hoursThreshold) {
        log.info("Fetching stuck onboardings from last {} hours", hoursThreshold);
        List<OnboardingTrackerResponse> response = onboardingTrackerService.getStuckOnboardings(hoursThreshold);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OnboardingTrackerResponse>> getPendingOnboardings() {
        log.info("Fetching pending onboardings");
        List<OnboardingTrackerResponse> response = onboardingTrackerService.getPendingOnboardings();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/high-priority")
    public ResponseEntity<List<OnboardingTrackerResponse>> getHighPriorityOnboardings() {
        log.info("Fetching high priority onboardings");
        List<OnboardingTrackerResponse> response = onboardingTrackerService.getHighPriorityOnboardings();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<OnboardingTrackerResponse> assignToUser(
            @PathVariable UUID id,
            @RequestParam String userId) {
        log.info("Assigning onboarding {} to user: {}", id, userId);
        OnboardingTrackerResponse response = onboardingTrackerService.assignToUser(id, userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<OnboardingTrackerResponse> updatePriority(
            @PathVariable UUID id,
            @RequestParam OnboardingTracker.Priority priority) {
        log.info("Updating priority for onboarding {} to: {}", id, priority);
        OnboardingTrackerResponse response = onboardingTrackerService.updatePriority(id, priority);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOnboarding(
            @PathVariable UUID id,
            @RequestParam String reason) {
        log.info("Cancelling onboarding {} reason: {}", id, reason);
        onboardingTrackerService.cancelOnboarding(id, reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Onboarding Tracker Service is running");
    }
}
