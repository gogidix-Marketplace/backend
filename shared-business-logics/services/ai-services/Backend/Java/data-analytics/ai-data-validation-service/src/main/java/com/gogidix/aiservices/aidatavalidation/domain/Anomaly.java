package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Value;

/**
 * Domain value object representing an anomaly detected in data.
 */
@Value
public class Anomaly {
    String fieldName;
    double value;
    double deviation;
}
