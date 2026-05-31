package com.gogidix.aiservices.performanceoptimizationservice.domain.model;
public record Recommendation(
    String type,
    String description,
    int priority,
    String action
) {}
