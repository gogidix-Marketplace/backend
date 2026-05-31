package com.gogidix.aiservices.aitestingservice.domain.model;

import java.time.Instant;

public record TestCaseResult(
    String caseId, String name, TestStatus status, String output, Instant startedAt, Instant completedAt
) {}
