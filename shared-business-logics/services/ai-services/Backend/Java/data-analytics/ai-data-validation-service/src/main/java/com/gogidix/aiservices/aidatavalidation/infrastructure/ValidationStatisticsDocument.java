package com.gogidix.aiservices.aidatavalidation.infrastructure;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Embedded document for validation statistics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationStatisticsDocument {

    private Integer totalRecords;
    private Integer validRecords;
    private Integer invalidRecords;
    private Integer skippedRecords;
}
