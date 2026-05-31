package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;
import lombok.Builder;
import lombok.NonNull;
import lombok.AllArgsConstructor;

/**
 * Value object containing validation statistics.
 */
@Value
@Builder
@AllArgsConstructor
public class ValidationStatistics {

    @NonNull
    Integer totalRecords;

    @NonNull
    Integer validRecords;

    @NonNull
    Integer invalidRecords;

    @NonNull
    Integer skippedRecords;

    /**
     * Calculates the validity rate (valid records / total records).
     */
    public double getValidityRate() {
        if (totalRecords == null || totalRecords == 0) {
            return 0.0;
        }
        return (double) validRecords / totalRecords;
    }

    /**
     * Calculates the invalidity rate (invalid records / total records).
     */
    public double getInvalidityRate() {
        if (totalRecords == null || totalRecords == 0) {
            return 0.0;
        }
        return (double) invalidRecords / totalRecords;
    }

    /**
     * Calculates the completeness rate (total - skipped) / total.
     */
    public double getCompletenessRate() {
        if (totalRecords == null || totalRecords == 0) {
            return 0.0;
        }
        return (double) (totalRecords - skippedRecords) / totalRecords;
    }
}
