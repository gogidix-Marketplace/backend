package com.gogidix.dashboard.performance.adapter.in.web.dto;

import com.gogidix.dashboard.performance.domain.model.SLACompliance;

import java.util.List;
import java.util.ArrayList;

/**
 * Data Transfer Object for SLA Compliance
 */
public class SLAComplianceDTO {
    private String domain;
    private String service;
    private double compliancePercentage;
    private int totalViolations;
    private List<PerformanceMetricDTO> violations;

    public SLAComplianceDTO() {
    }

    public SLAComplianceDTO(String domain, String service, double compliancePercentage,
                           int totalViolations, List<PerformanceMetricDTO> violations) {
        this.domain = domain;
        this.service = service;
        this.compliancePercentage = compliancePercentage;
        this.totalViolations = totalViolations;
        this.violations = violations;
    }

    public static SLAComplianceDTO fromDomain(SLACompliance domain) {
        return new SLAComplianceDTO(
            domain.getDomain(),
            domain.getService(),
            domain.getCompliancePercentage(),
            domain.getTotalViolations(),
            new ArrayList<>()
        );
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getCompliancePercentage() {
        return compliancePercentage;
    }

    public void setCompliancePercentage(double compliancePercentage) {
        this.compliancePercentage = compliancePercentage;
    }

    public int getTotalViolations() {
        return totalViolations;
    }

    public void setTotalViolations(int totalViolations) {
        this.totalViolations = totalViolations;
    }

    public List<PerformanceMetricDTO> getViolations() {
        return violations;
    }

    public void setViolations(List<PerformanceMetricDTO> violations) {
        this.violations = violations;
    }
}
