package com.gogidix.aiservices.aisecurityanalysisservice.interfaces.rest;

import com.gogidix.aiservices.aisecurityanalysisservice.application.dto.request.ScanRequest;
import com.gogidix.aiservices.aisecurityanalysisservice.application.dto.response.ScanResponse;
import com.gogidix.aiservices.aisecurityanalysisservice.application.service.SecurityAnalysisService;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.VulnerabilityScan;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
public class SecurityAnalysisController {

    private final SecurityAnalysisService securityAnalysisService;

    @PostMapping("/scan")
    public ResponseEntity<ScanResponse> initiateScan(@Valid @RequestBody ScanRequest request) {
        VulnerabilityScan scan = securityAnalysisService.initiateScan(
                request.getTarget(),
                request.getScanType()
        );
        return ResponseEntity.ok(ScanResponse.fromDomain(scan));
    }

    @GetMapping("/scan/{scanId}")
    public ResponseEntity<VulnerabilityScan> getScanResult(@PathVariable String scanId) {
        return ResponseEntity.ok(securityAnalysisService.getScanResult(scanId));
    }

    @GetMapping("/reports/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable String reportId) {
        return ResponseEntity.ok(securityAnalysisService.getReport(reportId));
    }

    @GetMapping("/scans/{userId}")
    public ResponseEntity<List<VulnerabilityScan>> getUserScans(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(securityAnalysisService.getUserScans(userId, limit));
    }
}
