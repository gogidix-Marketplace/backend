package com.gogidix.aiservices.aisecurityanalysisservice.domain.model;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class SecurityReport {
    private final String reportId;
    private final String scanId;
    private final int riskScore;
    private final List<Vulnerability> vulnerabilities;
    private final Instant generatedAt;
}
