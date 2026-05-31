package com.gogidix.aiservices.aireporting.application;

import com.gogidix.aiservices.aireporting.domain.ReportGenerator;
import com.gogidix.aiservices.aireporting.domain.ReportRequest;
import com.gogidix.aiservices.aireporting.domain.Report;
import com.gogidix.aiservices.aireporting.application.port.in.GenerateReportCommand;
import com.gogidix.aiservices.aireporting.application.port.in.GenerateReportUseCase;
import com.gogidix.aiservices.aireporting.application.port.out.ReportRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application service for report generation operations.
 */
@Service
public class ReportingService implements GenerateReportUseCase {

    private static final Logger log = LoggerFactory.getLogger(ReportingService.class);

    private final ReportRepository reportRepository;
    private final ReportGenerator reportGenerator;

    public ReportingService(ReportRepository reportRepository, ReportGenerator reportGenerator) {
        this.reportRepository = reportRepository;
        this.reportGenerator = reportGenerator;
    }

    @Override
    public Report generateReport(GenerateReportCommand command) {
        log.info("Generating report: type={}, format={}", command.type(), command.format());

        // Create report request
        ReportRequest request = new ReportRequest(
            UUID.randomUUID().toString(),
            command.type(),
            command.format(),
            command.startDate(),
            command.endDate(),
            command.includeMetrics(),
            command.options()
        );

        // Generate report
        Report report = reportGenerator.generateReport(request);

        // Save report
        reportRepository.save(report);

        return report;
    }

    @Override
    public Optional<Report> getReportStatus(String reportId) {
        log.info("Fetching report status for: {}", reportId);
        return reportRepository.findById(reportId);
    }

    @Override
    public String getDownloadUrl(String reportId) {
        return reportRepository.findById(reportId)
            .map(Report::getDownloadUrl)
            .orElse(null);
    }
}
