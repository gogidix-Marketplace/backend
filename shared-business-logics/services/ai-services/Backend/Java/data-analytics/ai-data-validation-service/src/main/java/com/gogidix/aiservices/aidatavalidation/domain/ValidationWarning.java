package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;
import lombok.Builder;
import lombok.NonNull;
import lombok.AllArgsConstructor;

/**
 * Value object representing a validation warning.
 * Warnings do not cause validation to fail.
 */
@Value
@Builder
@AllArgsConstructor
public class ValidationWarning {

    @NonNull
    String field;

    @NonNull
    String code;

    @NonNull
    String message;
}
