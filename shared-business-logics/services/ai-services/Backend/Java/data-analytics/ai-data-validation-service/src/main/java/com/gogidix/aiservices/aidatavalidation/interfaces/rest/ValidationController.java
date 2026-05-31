package com.gogidix.aiservices.aidatavalidation.interfaces.rest;

import com.gogidix.aiservices.aidatavalidation.application.dto.request.ValidateDatasetRequest;
import com.gogidix.aiservices.aidatavalidation.application.dto.response.ValidationResponse;
import com.gogidix.aiservices.aidatavalidation.application.service.ValidationService;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationType;
import com.gogidix.aiservices.aidatavalidation.shared.exception.ValidationException;
import com.gogidix.aiservices.aidatavalidation.shared.exception.ValidationNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping("/datasets/validate")
    public ResponseEntity<ValidationResponse> validateDataset(
            @Valid @RequestBody ValidateDatasetRequest request) {
        ValidationResponse response = validationService.validateDataset(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/results/{id}")
    public ResponseEntity<ValidationResponse> getValidationResult(@PathVariable String id) {
        ValidationResponse response = validationService.getValidationResult(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<ValidationResponse>> getPendingValidations() {
        List<ValidationResponse> responses = validationService.getPendingValidations();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/rules")
    public ResponseEntity<Void> addValidationRule(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String typeStr = (String) request.get("type");
        @SuppressWarnings("unchecked")
        Map<String, Object> configuration = (Map<String, Object>) request.get("configuration");

        ValidationType type = ValidationType.valueOf(typeStr);
        validationService.addValidationRule(name, type, configuration);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/rules")
    public ResponseEntity<List<Map<String, Object>>> getRulesByType(
            @RequestParam(required = false) String type) {
        ValidationType validationType = type != null ? ValidationType.valueOf(type) : ValidationType.SCHEMA;
        var rules = validationService.getRulesByType(validationType);
        return ResponseEntity.ok(rules.stream()
                .map(r -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> result = Map.of(
                        "ruleId", r.getRuleId(),
                        "name", r.getName(),
                        "type", r.getType().name(),
                        "enabled", r.isEnabled()
                    );
                    return result;
                })
                .toList());
    }

    @PutMapping("/rules/{id}/enabled")
    public ResponseEntity<Void> setRuleEnabled(
            @PathVariable String id,
            @RequestParam boolean enabled) {
        validationService.setRuleEnabled(id, enabled);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<Void> deleteValidationRule(@PathVariable String id) {
        validationService.deleteValidationRule(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelValidation(@PathVariable String id) {
        validationService.cancelValidation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = validationService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    @ExceptionHandler(ValidationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ValidationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
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
