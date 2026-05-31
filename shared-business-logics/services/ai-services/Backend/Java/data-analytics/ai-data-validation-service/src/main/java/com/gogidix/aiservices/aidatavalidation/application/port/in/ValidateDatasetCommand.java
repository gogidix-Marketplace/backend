package com.gogidix.aiservices.aidatavalidation.application.port.in;

import java.util.List;
import java.util.Map;

/**
 * Command for validating a dataset.
 */
public record ValidateDatasetCommand(
    String dataSource,
    Map<String, Object> schema,
    List<String> validationRules
) {
    public ValidateDatasetCommand {
        if (dataSource == null || dataSource.trim().isEmpty()) {
            throw new IllegalArgumentException("dataSource cannot be null or empty");
        }
        if (schema == null) {
            throw new IllegalArgumentException("schema cannot be null");
        }
    }
}
