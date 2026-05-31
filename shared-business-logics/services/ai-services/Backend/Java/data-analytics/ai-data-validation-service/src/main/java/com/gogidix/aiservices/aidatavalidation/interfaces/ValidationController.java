package com.gogidix.aiservices.aidatavalidation.interfaces;

import com.gogidix.aiservices.aidatavalidation.application.*;
import com.gogidix.aiservices.aidatavalidation.application.port.in.ValidateDatasetCommand;
import com.gogidix.aiservices.aidatavalidation.application.port.in.ValidateDatasetUseCase;
import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceNotFoundException;
import com.gogidix.aiservices.aidatavalidation.application.port.out.ValidationTimeoutException;
import com.gogidix.aiservices.aidatavalidation.domain.ValidationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

/**
 * REST controller for data validation operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/validation")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ValidationController {

    private final ValidateDatasetUseCase validateDatasetUseCase;

    @PostMapping("/datasets/validate")
    public ResponseEntity<Map<String, Object>> validateDataset(
            @Valid @RequestBody ValidateDatasetRequest request) {
        log.info("Received validation request for data source: {}", request.dataSource());

        ValidateDatasetCommand command = new ValidateDatasetCommand(
            request.dataSource(),
            request.schema(),
            request.validationRules()
        );

        ValidationResult result = validateDatasetUseCase.validateDataset(command);
        ValidationResultDto dto = ValidationResultDto.fromDomain(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(dto.toResponseJson());
    }

    @GetMapping("/results/{validationId}")
    public ResponseEntity<Map<String, Object>> getValidationResult(
            @PathVariable String validationId) {
        log.info("Fetching validation result for ID: {}", validationId);

        return validateDatasetUseCase.getValidationResult(validationId)
            .map(result -> {
                ValidationResultDto dto = ValidationResultDto.fromDomain(result);
                return ResponseEntity.ok(dto.toResponseJson());
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @ExceptionHandler(DataSourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleDataSourceNotFound(DataSourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(ValidationTimeoutException.class)
    public ResponseEntity<Map<String, String>> handleValidationTimeout(ValidationTimeoutException ex) {
        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
            .body(Map.of("message", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "An unexpected error occurred"));
    }

    public record ValidateDatasetRequest(
        String dataSource,
        Map<String, Object> schema,
        java.util.List<String> validationRules
    ) {}
}
