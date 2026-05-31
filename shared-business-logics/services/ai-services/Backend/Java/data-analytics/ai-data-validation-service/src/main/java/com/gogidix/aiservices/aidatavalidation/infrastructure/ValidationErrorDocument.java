package com.gogidix.aiservices.aidatavalidation.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Embedded document for validation errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorDocument {

    private String field;
    private String code;
    private String message;
}
