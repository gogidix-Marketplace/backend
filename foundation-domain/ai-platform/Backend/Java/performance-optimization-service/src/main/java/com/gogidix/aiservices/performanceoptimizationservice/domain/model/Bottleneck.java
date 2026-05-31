package com.gogidix.aiservices.performanceoptimizationservice.domain.model;
public record Bottleneck(
    String component,
    String severity,
    String description,
    double impactScore
) {}
