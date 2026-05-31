package com.gogidix.aiservices.aidatavalidation.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Embedded document for validation warnings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationWarningDocument {

    private String field;
    private String code;
    private String message;
}
