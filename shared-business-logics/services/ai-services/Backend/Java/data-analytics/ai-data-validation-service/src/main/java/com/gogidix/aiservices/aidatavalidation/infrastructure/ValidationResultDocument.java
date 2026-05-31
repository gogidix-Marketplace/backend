package com.gogidix.aiservices.aidatavalidation.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB document for storing validation results.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "validation_results")
public class ValidationResultDocument {

    @Id
    private String id;

    private String validationId;
    private boolean valid;

    @Builder.Default
    private List<ValidationErrorDocument> errors = List.of();

    @Builder.Default
    private List<ValidationWarningDocument> warnings = List.of();

    private ValidationStatisticsDocument statistics;
    private LocalDateTime validatedAt;
}
