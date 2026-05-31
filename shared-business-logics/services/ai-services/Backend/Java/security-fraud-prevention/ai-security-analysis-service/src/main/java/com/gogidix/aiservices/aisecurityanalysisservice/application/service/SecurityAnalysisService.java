package com.gogidix.aiservices.aisecurityanalysisservice.application.service;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.*;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.port.out.SecurityAnalysisRepository;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.port.out.VulnerabilityScannerPort;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.policy.SecurityAnalysisPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityAnalysisService {

    private final SecurityAnalysisRepository repository;
    private final VulnerabilityScannerPort scannerPort;
    private final SecurityAnalysisPolicy policy;

    public VulnerabilityScan initiateScan(String target, ScanType scanType) {
        policy.validateTarget(target);
        policy.checkConcurrentScanLimit();

        String scanId = UUID.randomUUID().toString();
        Instant estimatedCompletion = policy.estimateCompletionTime(scanType);

        VulnerabilityScan scan = VulnerabilityScan.builder()
                .scanId(scanId)
                .target(target)
                .scanType(scanType)
                .status(ScanStatus.RUNNING)
                .riskScore(0)
                .vulnerabilities(List.of())
                .startTime(Instant.now())
                .estimatedCompletion(estimatedCompletion)
                .build();

        repository.saveScan(scan);

        // Async scan initiation
        scannerPort.startScan(scanId, target, scanType);

        return scan;
    }

    public VulnerabilityScan getScanResult(String scanId) {
        VulnerabilityScan scan = repository.findById(scanId)
                .orElseThrow(() -> new IllegalArgumentException("Scan not found: " + scanId));

        if (scan.getStatus() == ScanStatus.RUNNING) {
            VulnerabilityScan updatedScan = scannerPort.getScanStatus(scanId);
            if (updatedScan != null) {
                scan = updatedScan;
                repository.saveScan(scan);
            }
        }

        return scan;
    }

    public SecurityReport getReport(String reportId) {
        return repository.findReportById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));
    }

    public List<VulnerabilityScan> getUserScans(String userId, int limit) {
        return repository.findByUserId(userId, limit);
    }
}
