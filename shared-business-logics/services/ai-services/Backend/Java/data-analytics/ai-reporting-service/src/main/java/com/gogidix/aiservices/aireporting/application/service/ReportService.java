package com.gogidix.aiservices.aireporting.application.service;

import com.gogidix.aiservices.aireporting.application.dto.request.GenerateReportRequest;
import com.gogidix.aiservices.aireporting.application.dto.response.ReportResponse;
import com.gogidix.aiservices.aireporting.domain.aggregate.ReportGeneration;
import com.gogidix.aiservices.aireporting.domain.port.out.ReportRepository;
import com.gogidix.aiservices.aireporting.shared.exception.ReportNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportResponse generateReport(GenerateReportRequest request) {
        if (request.getType() == null) {
            throw new IllegalArgumentException("Report type cannot be null");
        }

        List<String> metrics = request.getIncludeMetrics() != null ? request.getIncludeMetrics() : List.of();

        ReportGeneration generation = ReportGeneration.create(
                request.getType(),
                metrics,
                request.getFormat()
        );

        generation.setProgress(100);
        generation.complete("https://storage.example.com/reports/" + generation.getGenerationId() + ".pdf");

        ReportGeneration saved = reportRepository.save(generation);
        return toResponse(saved);
    }

    public ReportResponse getReport(String reportId) {
        ReportGeneration generation = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));
        return toResponse(generation);
    }

    public String getReportStatus(String reportId) {
        ReportGeneration generation = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));
        return generation.getStatus().name();
    }

    public void deleteReport(String reportId) {
        reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException(reportId));
        reportRepository.delete(reportId);
    }

    private ReportResponse toResponse(ReportGeneration generation) {
        return ReportResponse.builder()
                .reportId(generation.getGenerationId())
                .status(generation.getStatus().name())
                .downloadUrl(generation.getDownloadUrl())
                .expiresAt(generation.getExpiresAt())
                .build();
    }
}
