package com.gogidix.aiservices.aisecurityanalysisservice.domain.port.out;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.SecurityReport;
import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.VulnerabilityScan;

import java.util.List;
import java.util.Optional;

public interface SecurityAnalysisRepository {
    void saveScan(VulnerabilityScan scan);
    Optional<VulnerabilityScan> findById(String scanId);
    List<VulnerabilityScan> findByUserId(String userId, int limit);
    Optional<SecurityReport> findReportById(String reportId);
}
