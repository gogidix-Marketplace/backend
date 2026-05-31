package com.gogidix.aiservices.aidatavalidation.infrastructure;

import com.gogidix.aiservices.aidatavalidation.application.port.out.ValidationResultRepository;
import com.gogidix.aiservices.aidatavalidation.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MongoDB implementation of ValidationResultRepository.
 */
@Repository
@RequiredArgsConstructor
public class ValidationResultRepositoryImpl implements ValidationResultRepository {

    private final MongoValidationResultDocumentRepository mongoRepository;

    @Override
    public ValidationResult save(ValidationResult result) {
        ValidationResultDocument document = toDocument(result);
        ValidationResultDocument saved = mongoRepository.save(document);
        return fromDocument(saved);
    }

    @Override
    public Optional<ValidationResult> findById(String validationId) {
        return mongoRepository.findByValidationId(validationId)
            .map(this::fromDocument);
    }

    @Override
    public void deleteById(String validationId) {
        mongoRepository.deleteByValidationId(validationId);
    }

    private ValidationResultDocument toDocument(ValidationResult result) {
        ValidationResultDocument document = new ValidationResultDocument();
        document.setId(UUID.randomUUID().toString());
        document.setValidationId(result.getValidationId());
        document.setValid(result.isValid());
        document.setErrors(result.getErrors().stream()
            .map(e -> new ValidationErrorDocument(e.getField(), e.getCode(), e.getMessage()))
            .toList());
        document.setWarnings(result.getWarnings().stream()
            .map(w -> new ValidationWarningDocument(w.getField(), w.getCode(), w.getMessage()))
            .toList());
        document.setStatistics(new ValidationStatisticsDocument(
            result.getStatistics().getTotalRecords(),
            result.getStatistics().getValidRecords(),
            result.getStatistics().getInvalidRecords(),
            result.getStatistics().getSkippedRecords()
        ));
        document.setValidatedAt(result.getValidatedAt());
        return document;
    }

    private ValidationResult fromDocument(ValidationResultDocument document) {
        List<ValidationError> errors = document.getErrors().stream()
            .map(e -> new ValidationError(e.getField(), e.getCode(), e.getMessage()))
            .toList();

        List<ValidationWarning> warnings = document.getWarnings().stream()
            .map(w -> new ValidationWarning(w.getField(), w.getCode(), w.getMessage()))
            .toList();

        ValidationStatistics statistics = new ValidationStatistics(
            document.getStatistics().getTotalRecords(),
            document.getStatistics().getValidRecords(),
            document.getStatistics().getInvalidRecords(),
            document.getStatistics().getSkippedRecords()
        );

        return new ValidationResult(
            document.getValidationId(),
            document.isValid(),
            errors,
            warnings,
            statistics,
            document.getValidatedAt()
        );
    }
}
