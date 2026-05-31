package com.gogidix.transaction.progress.controller;

import com.gogidix.transaction.progress.dto.*;
import com.gogidix.transaction.progress.service.ProgressStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/progress-steps")
@RequiredArgsConstructor
@Slf4j
public class ProgressStepController {

    private final ProgressStepService progressStepService;

    @PostMapping
    public ResponseEntity<ProgressStepResponse> createStep(@Valid @RequestBody ProgressStepCreateRequest request) {
        log.info("Creating progress step: {}", request.getStepName());
        ProgressStepResponse response = progressStepService.createStep(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<ProgressStepResponse>> createSteps(
            @RequestParam UUID transactionId,
            @Valid @RequestBody List<ProgressStepCreateRequest> requests) {
        log.info("Creating {} steps for transaction: {}", requests.size(), transactionId);
        List<ProgressStepResponse> response = progressStepService.createSteps(transactionId, requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ProgressStepResponse> startStep(@PathVariable UUID id) {
        log.info("Starting step: {}", id);
        ProgressStepResponse response = progressStepService.startStep(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ProgressStepResponse> completeStep(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> outputData) {
        log.info("Completing step: {}", id);
        ProgressStepResponse response = progressStepService.completeStep(id, outputData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/fail")
    public ResponseEntity<ProgressStepResponse> failStep(
            @PathVariable UUID id,
            @RequestParam String errorMessage) {
        log.info("Failing step: {}", id);
        ProgressStepResponse response = progressStepService.failStep(id, errorMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/skip")
    public ResponseEntity<ProgressStepResponse> skipStep(
            @PathVariable UUID id,
            @RequestParam String reason) {
        log.info("Skipping step: {}", id);
        ProgressStepResponse response = progressStepService.skipStep(id, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<List<ProgressStepResponse>> getStepsByTransactionId(@PathVariable UUID transactionId) {
        log.info("Fetching steps for transaction: {}", transactionId);
        List<ProgressStepResponse> response = progressStepService.getStepsByTransactionId(transactionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressStepResponse> getStepById(@PathVariable UUID id) {
        log.info("Fetching step: {}", id);
        ProgressStepResponse response = progressStepService.getStepById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transaction/{transactionId}/summary")
    public ResponseEntity<StepExecutionSummary> getExecutionSummary(@PathVariable UUID transactionId) {
        log.info("Fetching execution summary for transaction: {}", transactionId);
        StepExecutionSummary response = progressStepService.getExecutionSummary(transactionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/compensate")
    public ResponseEntity<Void> executeCompensation(@PathVariable UUID id) {
        log.info("Executing compensation for step: {}", id);
        progressStepService.executeCompensation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Progress Step Service is running");
    }
}
