package com.gogidix.aiservices.aidatavalidation.application;

import com.gogidix.aiservices.aidatavalidation.domain.*;
import com.gogidix.aiservices.aidatavalidation.application.port.in.ValidateDatasetCommand;
import com.gogidix.aiservices.aidatavalidation.application.port.in.ValidateDatasetUseCase;
import com.gogidix.aiservices.aidatavalidation.application.port.out.ValidationResultRepository;
import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service for data validation operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService implements ValidateDatasetUseCase {

    private final ValidationResultRepository resultRepository;
    private final DataSourceAdapter dataSourceAdapter;
    private final DataQualityService dataQualityService;

    @Override
    public ValidationResult validateDataset(ValidateDatasetCommand command) {
        log.info("Starting validation for data source: {}", command.dataSource());

        // Fetch data from source
        List<Map<String, Object>> dataRecords = dataSourceAdapter.fetchData(command.dataSource());

        // Create validation result
        String validationId = UUID.randomUUID().toString();
        boolean isValid = true;
        List<ValidationError> errors = new java.util.ArrayList<>();
        List<ValidationWarning> warnings = new java.util.ArrayList<>();

        // Analyze data quality
        List<DataQualityIssue> qualityIssues = dataQualityService.analyzeQualityBatch(dataRecords);
        for (DataQualityIssue issue : qualityIssues) {
            if (issue.getSeverity() == QualitySeverity.HIGH || issue.getSeverity() == QualitySeverity.CRITICAL) {
                errors.add(new ValidationError(
                    issue.getFieldName(),
                    "QUALITY_" + issue.getSeverity().name(),
                    issue.getDescription()
                ));
                isValid = false;
            } else {
                warnings.add(new ValidationWarning(
                    issue.getFieldName(),
                    "QUALITY_" + issue.getSeverity().name(),
                    issue.getDescription()
                ));
            }
        }

        // Detect anomalies
        List<Anomaly> anomalies = dataQualityService.detectAnomaliesInBatch(dataRecords);
        for (Anomaly anomaly : anomalies) {
            warnings.add(new ValidationWarning(
                anomaly.getFieldName(),
                "ANOMALY_DETECTED",
                String.format("Anomalous value detected: %.2f (deviation: %.2f)",
                    anomaly.getValue(), anomaly.getDeviation())
            ));
        }

        // Calculate statistics
        int totalRecords = dataRecords.size();
        int validRecords = (int) (totalRecords * dataQualityService.calculateCompleteness(dataRecords));
        int invalidRecords = totalRecords - validRecords;

        ValidationStatistics statistics = new ValidationStatistics(
            totalRecords,
            validRecords,
            invalidRecords,
            0
        );

        ValidationResult result = new ValidationResult(
            validationId,
            isValid,
            errors,
            warnings,
            statistics,
            LocalDateTime.now()
        );

        // Save result
        resultRepository.save(result);
        log.info("Validation completed with ID: {}, Valid: {}", validationId, isValid);

        return result;
    }

    @Override
    public Optional<ValidationResult> getValidationResult(String validationId) {
        return resultRepository.findById(validationId);
    }

    @Override
    public boolean applyCustomRules(List<String> customRules) {
        // Implementation for applying custom validation rules
        log.info("Applying {} custom validation rules", customRules.size());
        return true;
    }
}
