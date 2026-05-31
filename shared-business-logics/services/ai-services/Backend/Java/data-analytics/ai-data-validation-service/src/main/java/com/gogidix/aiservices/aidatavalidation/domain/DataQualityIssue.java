package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;

/**
 * Domain value object representing a data quality issue.
 */
@Value
public class DataQualityIssue {
    String fieldName;
    QualitySeverity severity;
    String description;
}
