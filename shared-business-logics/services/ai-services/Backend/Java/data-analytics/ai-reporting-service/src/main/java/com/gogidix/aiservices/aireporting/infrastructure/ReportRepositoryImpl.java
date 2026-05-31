package com.gogidix.aiservices.aireporting.infrastructure;

import com.gogidix.aiservices.aireporting.application.port.out.ReportRepository;
import com.gogidix.aiservices.aireporting.domain.Report;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of ReportRepository.
 */
@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private final Map<String, Report> reports = new ConcurrentHashMap<>();

    @Override
    public Report save(Report report) {
        reports.put(report.getReportId(), report);
        return report;
    }

    @Override
    public Optional<Report> findById(String reportId) {
        return Optional.ofNullable(reports.get(reportId));
    }

    @Override
    public void deleteById(String reportId) {
        reports.remove(reportId);
    }

    @Override
    public java.util.List<Report> findByStatus(Report.ReportStatus status) {
        return reports.values().stream()
            .filter(r -> r.getStatus() == status)
            .toList();
    }
}
