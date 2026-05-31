package com.gogidix.aiservices.supplychainoptimizationservice.interfaces.rest;

import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.request.CreateOptimizationRequest;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response.OptimizationRequestResponse;
import com.gogidix.aiservices.supplychainoptimizationservice.application.dto.response.OptimizationResultResponse;
import com.gogidix.aiservices.supplychainoptimizationservice.application.service.SupplyChainOptimizationService;
import com.gogidix.aiservices.supplychainoptimizationservice.domain.model.OptimizationStatus;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.OptimizationRequestNotFoundException;
import com.gogidix.aiservices.supplychainoptimizationservice.shared.exception.SupplyChainException;
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
@RequestMapping("/api/v1/optimization")
@RequiredArgsConstructor
@Validated
public class SupplyChainOptimizationController {

    private final SupplyChainOptimizationService optimizationService;

    @PostMapping
    public ResponseEntity<OptimizationRequestResponse> createRequest(
            @Valid @RequestBody CreateOptimizationRequest request) {
        OptimizationRequestResponse response = optimizationService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OptimizationRequestResponse> getRequest(@PathVariable String id) {
        OptimizationRequestResponse response = optimizationService.getRequest(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OptimizationRequestResponse>> listRequests(
            @RequestParam(required = false) String tenantId,
            @RequestParam(required = false) OptimizationStatus status) {

        if (tenantId != null) {
            return ResponseEntity.ok(optimizationService.getRequestsByTenant(tenantId));
        } else if (status != null) {
            return ResponseEntity.ok(optimizationService.getRequestsByStatus(status));
        }

        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/{id}/process")
    public ResponseEntity<OptimizationRequestResponse> processRequest(@PathVariable String id) {
        OptimizationRequestResponse response = optimizationService.processRequest(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<OptimizationResultResponse> getResult(@PathVariable String id) {
        OptimizationResultResponse response = optimizationService.getResult(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable String id) {
        optimizationService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OptimizationRequestResponse> cancelRequest(@PathVariable String id) {
        optimizationService.cancelRequest(id);
        return ResponseEntity.ok(optimizationService.getRequest(id));
    }

    @ExceptionHandler(OptimizationRequestNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(OptimizationRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(SupplyChainException.class)
    public ResponseEntity<Map<String, Object>> handleSupplyChainException(SupplyChainException ex) {
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
