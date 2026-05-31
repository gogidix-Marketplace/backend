package com.gogidix.dashboard.performance.domain.model;

import java.util.List;

public class SLACompliance {
    private final String domain;
    private final String service;
    private final double compliancePercentage;
    private final int totalViolations;
    private final List<PerformanceMetric> violations;

    public SLACompliance(String domain, String service, double compliancePercentage,
                         int totalViolations, List<PerformanceMetric> violations) {
        this.domain = domain;
        this.service = service;
        this.compliancePercentage = compliancePercentage;
        this.totalViolations = totalViolations;
        this.violations = violations;
    }

    public String getDomain() { return domain; }
    public String getService() { return service; }
    public double getCompliancePercentage() { return compliancePercentage; }
    public int getTotalViolations() { return totalViolations; }
    public List<PerformanceMetric> getViolations() { return violations; }
}
