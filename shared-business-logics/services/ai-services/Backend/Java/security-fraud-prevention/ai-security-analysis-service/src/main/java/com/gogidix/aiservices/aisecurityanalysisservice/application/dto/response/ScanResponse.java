package com.gogidix.aiservices.aisecurityanalysisservice.application.dto.response;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanType;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanStatus;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.Vulnerability;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.VulnerabilityScan;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class ScanResponse {
    private String scanId;
    private String target;
    private ScanType scanType;
    private ScanStatus status;
    private int riskScore;
    private List<Vulnerability> vulnerabilities;
    private Instant startTime;
    private Instant estimatedCompletion;

    public static ScanResponse fromDomain(VulnerabilityScan scan) {
        ScanResponse response = new ScanResponse();
        response.setScanId(scan.getScanId());
        response.setTarget(scan.getTarget());
        response.setScanType(scan.getScanType());
        response.setStatus(scan.getStatus());
        response.setRiskScore(scan.getRiskScore());
        response.setVulnerabilities(scan.getVulnerabilities());
        response.setStartTime(scan.getStartTime());
        response.setEstimatedCompletion(scan.getEstimatedCompletion());
        return response;
    }
}
