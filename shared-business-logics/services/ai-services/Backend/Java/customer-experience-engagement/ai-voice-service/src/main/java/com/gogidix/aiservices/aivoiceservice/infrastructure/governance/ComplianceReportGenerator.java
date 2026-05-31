package com.gogidix.aiservices.aivoiceservice.infrastructure.governance;

import com.gogidix.aiservices.aivoiceservice.infrastructure.metrics.VoiceMetrics;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ComplianceReportGenerator {
    private final VoiceMetrics metrics;
    private final ThresholdValidator thresholdValidator;
    private static final double TARGET_P95 = 500.0;
    private static final double TARGET_ERROR = 0.01;

    public ComplianceReportGenerator(VoiceMetrics metrics, ThresholdValidator tv) { this.metrics = metrics; this.thresholdValidator = tv; }

    public GovernanceReport generateReport() {
        GovernanceReport report = new GovernanceReport();
        report.setGeneratedAt(Instant.now());
        report.setReportId("GOV-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now(ZoneId.of("UTC"))));
        var health = thresholdValidator.getHealthStatus();
        report.setGovernanceStatus(new GovernanceStatus());
        report.getGovernanceStatus().setHealthStatus(health.toString());
        report.getGovernanceStatus().setStatus(health == ThresholdValidator.HealthStatus.HEALTHY ? "COMPLIANT" : "NON-COMPLIANT");
        return report;
    }

    public static class GovernanceReport {
        private String reportId; private Instant generatedAt; private GovernanceStatus governanceStatus;
        public String getReportId() { return reportId; } public void setReportId(String r) { reportId = r; }
        public Instant getGeneratedAt() { return generatedAt; } public void setGeneratedAt(Instant g) { generatedAt = g; }
        public GovernanceStatus getGovernanceStatus() { return governanceStatus; } public void setGovernanceStatus(GovernanceStatus g) { governanceStatus = g; }
        public Map<String, Object> toMap() { Map<String, Object> m = new HashMap<>(); m.put("reportId", reportId); return m; }
    }

    public static class GovernanceStatus {
        private String status; private String healthStatus; private String severity; private String message;
        public String getStatus() { return status; } public void setStatus(String s) { status = s; }
        public String getHealthStatus() { return healthStatus; } public void setHealthStatus(String h) { healthStatus = h; }
        public String getSeverity() { return severity; } public void setSeverity(String s) { severity = s; }
        public String getMessage() { return message; } public void setMessage(String m) { message = m; }
    }
}
