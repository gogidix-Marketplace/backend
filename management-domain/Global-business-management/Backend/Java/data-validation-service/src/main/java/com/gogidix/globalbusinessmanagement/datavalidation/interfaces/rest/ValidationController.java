package com.gogidix.globalbusinessmanagement.datavalidation.interfaces.rest;

import com.gogidix.globalbusinessmanagement.datavalidation.application.service.ValidationEngineService;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.BatchValidationRequestDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRequestDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * REST controller for data validation operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationEngineService validationEngineService;

    /**
     * Validate an entity against specified rules
     */
    @PostMapping
    public ResponseEntity<ValidationResponseDTO> validate(@Valid @RequestBody ValidationRequestDTO request) {
        log.info("POST /api/v1/validation - Validating entity: {} ({})",
                request.getEntityType(), request.getEntityId());

        if (request.isAsync()) {
            // Async validation
            CompletableFuture<ValidationResponseDTO> future = CompletableFuture.supplyAsync(() ->
                    validationEngineService.validate(request));
            return ResponseEntity.accepted().build();
        }

        // Sync validation
        ValidationResponseDTO response = validationEngineService.validate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Validate multiple entities in batch
     */
    @PostMapping("/batch")
    public ResponseEntity<List<ValidationResponseDTO>> validateBatch(@Valid @RequestBody BatchValidationRequestDTO request) {
        log.info("POST /api/v1/validation/batch - Validating batch of {} entities",
                request.getRequests().size());

        List<ValidationResponseDTO> responses = request.getRequests().stream()
                .map(validationEngineService::validate)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    /**
     * Quick validation with default rules for entity type
     */
    @PostMapping("/quick/{entityType}/{entityId}")
    public ResponseEntity<ValidationResponseDTO> quickValidate(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestBody String entityData,
            @RequestParam(defaultValue = "false") boolean async) {
        log.info("POST /api/v1/validation/quick/{}/{} - Quick validation", entityType, entityId);

        ValidationRequestDTO request = ValidationRequestDTO.builder()
                .entityType(entityType)
                .entityId(entityId)
                .entityData(entityData)
                .async(async)
                .build();

        ValidationResponseDTO response = validationEngineService.validate(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Validation service is healthy");
    }
}
