package com.gogidix.aiservices.voicerecognitionservice.infrastructure.governance;

import com.gogidix.aiservices.voicerecognitionservice.infrastructure.metrics.VoiceRecognitionMetrics;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ComplianceReportGenerator {
    private final VoiceRecognitionMetrics metrics;
    private final ThresholdValidator thresholdValidator;

    public ComplianceReportGenerator(VoiceRecognitionMetrics m, ThresholdValidator tv) { metrics = m; thresholdValidator = tv; }

    public GovernanceReport generateReport() {
        GovernanceReport r = new GovernanceReport();
        r.setGeneratedAt(Instant.now());
        r.setReportId("GOV-" + DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now(ZoneId.of("UTC"))));
        var health = thresholdValidator.getHealthStatus();
        r.setGovernanceStatus(new GovernanceStatus());
        r.getGovernanceStatus().setHealthStatus(health.toString());
        r.getGovernanceStatus().setStatus(health == ThresholdValidator.HealthStatus.HEALTHY ? "COMPLIANT" : "NON-COMPLIANT");
        return r;
    }

    public static class GovernanceReport {
        private String reportId; private Instant generatedAt; private GovernanceStatus governanceStatus;
        public String getReportId() { return reportId; } public void setReportId(String r) { reportId = r; }
        public Instant getGeneratedAt() { return generatedAt; } public void setGeneratedAt(Instant g) { generatedAt = g; }
        public GovernanceStatus getGovernanceStatus() { return governanceStatus; } public void setGovernanceStatus(GovernanceStatus g) { governanceStatus = g; }
        public Map<String, Object> toMap() { Map<String, Object> m = new HashMap<>(); m.put("reportId", reportId); return m; }
    }

    public static class GovernanceStatus {
        private String status; private String healthStatus;
        public String getStatus() { return status; } public void setStatus(String s) { status = s; }
        public String getHealthStatus() { return healthStatus; } public void setHealthStatus(String h) { healthStatus = h; }
    }
}
