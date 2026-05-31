package com.gogidix.shared.audit.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportBatchTest {

    @Test
    void accessControlReportNoArgsConstructor() {
        AccessControlReport report = new AccessControlReport();
        assertNotNull(report);
        assertNull(report.getReportId());
        assertNull(report.getTotalAccessAttempts());
    }

    @Test
    void accessControlReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> unauthorized = Arrays.asList("attempt1", "attempt2");
        List<String> violations = Arrays.asList("violation1");
        AccessControlReport report = new AccessControlReport(
            "rpt-1", now, 100L, 80L, 15L, 5L, unauthorized, violations, "LOW"
        );
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(100L, report.getTotalAccessAttempts());
        assertEquals(80L, report.getSuccessfulAccess());
        assertEquals(15L, report.getFailedAccess());
        assertEquals(5L, report.getBlockedAccess());
        assertEquals(unauthorized, report.getUnauthorizedAttempts());
        assertEquals(violations, report.getSecurityViolations());
        assertEquals("LOW", report.getRiskAssessment());
    }

    @Test
    void accessControlReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AccessControlReport report = AccessControlReport.builder()
            .reportId("rpt-2").generatedAt(now).totalAccessAttempts(200L)
            .successfulAccess(180L).failedAccess(10L).blockedAccess(10L)
            .unauthorizedAttempts(Collections.emptyList()).securityViolations(Collections.emptyList())
            .riskAssessment("HIGH").build();
        assertEquals("rpt-2", report.getReportId());
        assertEquals(200L, report.getTotalAccessAttempts());
    }

    @Test
    void accessControlReportSettersAndGetters() {
        AccessControlReport report = new AccessControlReport();
        LocalDateTime now = LocalDateTime.now();
        report.setReportId("rpt-3");
        report.setGeneratedAt(now);
        report.setTotalAccessAttempts(50L);
        report.setSuccessfulAccess(40L);
        report.setFailedAccess(5L);
        report.setBlockedAccess(5L);
        report.setUnauthorizedAttempts(Arrays.asList("a1"));
        report.setSecurityViolations(Arrays.asList("v1"));
        report.setRiskAssessment("MEDIUM");
        assertEquals("rpt-3", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(50L, report.getTotalAccessAttempts());
        assertEquals(40L, report.getSuccessfulAccess());
        assertEquals(5L, report.getFailedAccess());
        assertEquals(5L, report.getBlockedAccess());
        assertEquals(Arrays.asList("a1"), report.getUnauthorizedAttempts());
        assertEquals(Arrays.asList("v1"), report.getSecurityViolations());
        assertEquals("MEDIUM", report.getRiskAssessment());
    }

    @Test
    void accessControlReportGetAccessSuccessRateNormal() {
        AccessControlReport report = AccessControlReport.builder()
            .totalAccessAttempts(100L).successfulAccess(80L).build();
        assertEquals(80.0, report.getAccessSuccessRate(), 0.001);
    }

    @Test
    void accessControlReportGetAccessSuccessRateZeroTotal() {
        AccessControlReport report = AccessControlReport.builder()
            .totalAccessAttempts(0L).successfulAccess(0L).build();
        assertEquals(0.0, report.getAccessSuccessRate(), 0.001);
    }

    @Test
    void accessControlReportGetAccessSuccessRateNullTotal() {
        AccessControlReport report = new AccessControlReport();
        assertEquals(0.0, report.getAccessSuccessRate(), 0.001);
    }

    @Test
    void accessControlReportGetAccessSuccessRateNullSuccessful() {
        AccessControlReport report = AccessControlReport.builder().totalAccessAttempts(100L).build();
        assertEquals(0.0, report.getAccessSuccessRate(), 0.001);
    }

    @Test
    void accessControlReportEqualsAndHashCode() {
        AccessControlReport r1 = AccessControlReport.builder().reportId("r1").build();
        AccessControlReport r2 = AccessControlReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void accessControlReportNotEquals() {
        AccessControlReport r1 = AccessControlReport.builder().reportId("r1").build();
        AccessControlReport r2 = AccessControlReport.builder().reportId("r2").build();
        assertNotEquals(r1, r2);
    }

    @Test
    void accessControlReportToString() {
        AccessControlReport report = AccessControlReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }

    @Test
    void auditIntegrityReportNoArgsConstructor() {
        AuditIntegrityReport report = new AuditIntegrityReport();
        assertNotNull(report);
        assertNull(report.getReportId());
    }

    @Test
    void auditIntegrityReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> issues = Arrays.asList("issue1");
        List<String> tampered = Arrays.asList("tampered1");
        AuditIntegrityReport report = new AuditIntegrityReport(
            "rpt-1", now, 1000L, 500L, 10L, issues, tampered, "95%"
        );
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(1000L, report.getTotalAuditEvents());
        assertEquals(500L, report.getIntegrityChecksPerformed());
        assertEquals(10L, report.getIntegrityFailures());
        assertEquals(issues, report.getIntegrityIssues());
        assertEquals(tampered, report.getTamperedEvents());
        assertEquals("95%", report.getIntegrityScore());
    }

    @Test
    void auditIntegrityReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AuditIntegrityReport report = AuditIntegrityReport.builder()
            .reportId("rpt-2").generatedAt(now).totalAuditEvents(500L)
            .integrityChecksPerformed(200L).integrityFailures(5L)
            .integrityIssues(Collections.emptyList()).tamperedEvents(Collections.emptyList())
            .integrityScore("97%").build();
        assertEquals("rpt-2", report.getReportId());
        assertEquals(500L, report.getTotalAuditEvents());
    }

    @Test
    void auditIntegrityReportSettersAndGetters() {
        AuditIntegrityReport report = new AuditIntegrityReport();
        LocalDateTime now = LocalDateTime.now();
        report.setReportId("rpt-3");
        report.setGeneratedAt(now);
        report.setTotalAuditEvents(300L);
        report.setIntegrityChecksPerformed(150L);
        report.setIntegrityFailures(3L);
        report.setIntegrityIssues(Arrays.asList("i1"));
        report.setTamperedEvents(Arrays.asList("t1"));
        report.setIntegrityScore("98%");
        assertEquals("rpt-3", report.getReportId());
        assertEquals(300L, report.getTotalAuditEvents());
        assertEquals(150L, report.getIntegrityChecksPerformed());
        assertEquals(3L, report.getIntegrityFailures());
    }

    @Test
    void auditIntegrityReportGetRateNormal() {
        AuditIntegrityReport report = AuditIntegrityReport.builder()
            .integrityChecksPerformed(100L).integrityFailures(5L).build();
        assertEquals(95.0, report.getIntegritySuccessRate(), 0.001);
    }

    @Test
    void auditIntegrityReportGetRateZeroChecks() {
        AuditIntegrityReport report = AuditIntegrityReport.builder()
            .integrityChecksPerformed(0L).build();
        assertEquals(100.0, report.getIntegritySuccessRate(), 0.001);
    }

    @Test
    void auditIntegrityReportGetRateNullChecks() {
        AuditIntegrityReport report = new AuditIntegrityReport();
        assertEquals(100.0, report.getIntegritySuccessRate(), 0.001);
    }

    @Test
    void auditIntegrityReportGetRateNullFailures() {
        AuditIntegrityReport report = AuditIntegrityReport.builder()
            .integrityChecksPerformed(100L).build();
        assertEquals(100.0, report.getIntegritySuccessRate(), 0.001);
    }

    @Test
    void auditIntegrityReportEqualsAndHashCode() {
        AuditIntegrityReport r1 = AuditIntegrityReport.builder().reportId("r1").build();
        AuditIntegrityReport r2 = AuditIntegrityReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void auditIntegrityReportToString() {
        AuditIntegrityReport report = AuditIntegrityReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }

    @Test
    void complianceReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ComplianceReport report = ComplianceReport.builder()
            .eventId("evt-1").timestamp(now).complianceType(ComplianceType.GDPR)
            .auditTrail("trail-data").riskAssessment("LOW").regulatoryContext("GDPR context")
            .reportId("rpt-1").generatedAt(now).build();
        assertEquals("evt-1", report.getEventId());
        assertEquals(now, report.getTimestamp());
        assertEquals(ComplianceType.GDPR, report.getComplianceType());
        assertEquals("trail-data", report.getAuditTrail());
        assertEquals("LOW", report.getRiskAssessment());
        assertEquals("GDPR context", report.getRegulatoryContext());
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
    }

    @Test
    void complianceReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ComplianceReport report = new ComplianceReport(
            "evt-1", now, ComplianceType.PCI_DSS, "trail", "HIGH", "PCI context", "rpt-1", now
        );
        assertEquals("evt-1", report.getEventId());
        assertEquals(ComplianceType.PCI_DSS, report.getComplianceType());
    }

    @Test
    void complianceReportIsValidTrue() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.SOX)
            .auditTrail("trail").regulatoryContext("SOX context").build();
        assertTrue(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseNullEventId() {
        ComplianceReport report = ComplianceReport.builder().timestamp(LocalDateTime.now())
            .complianceType(ComplianceType.GDPR).auditTrail("trail")
            .regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseEmptyEventId() {
        ComplianceReport report = ComplianceReport.builder().eventId("")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.GDPR)
            .auditTrail("trail").regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseNullTimestamp() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .complianceType(ComplianceType.GDPR).auditTrail("trail")
            .regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseNullComplianceType() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).auditTrail("trail")
            .regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseNullAuditTrail() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.GDPR)
            .regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseEmptyAuditTrail() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.GDPR)
            .auditTrail("").regulatoryContext("context").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseNullRegulatoryContext() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.GDPR)
            .auditTrail("trail").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportIsValidFalseEmptyRegulatoryContext() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).complianceType(ComplianceType.GDPR)
            .auditTrail("trail").regulatoryContext("").build();
        assertFalse(report.isValid());
    }

    @Test
    void complianceReportGetScoreFull() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).auditTrail("trail")
            .riskAssessment("LOW").regulatoryContext("ctx").build();
        assertEquals(100, report.getComplianceScore());
    }

    @Test
    void complianceReportGetScorePartial() {
        ComplianceReport report = ComplianceReport.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).build();
        assertEquals(40, report.getComplianceScore());
    }

    @Test
    void complianceReportGetScoreZero() {
        ComplianceReport report = ComplianceReport.builder().build();
        assertEquals(0, report.getComplianceScore());
    }

    @Test
    void complianceReportGetScoreEmptyEventId() {
        ComplianceReport report = ComplianceReport.builder().eventId("")
            .timestamp(LocalDateTime.now()).build();
        assertEquals(20, report.getComplianceScore());
    }

    @Test
    void complianceReportGetScoreEmptyAuditTrail() {
        ComplianceReport report = ComplianceReport.builder().eventId("e1")
            .timestamp(LocalDateTime.now()).auditTrail("").build();
        assertEquals(40, report.getComplianceScore());
    }

    @Test
    void complianceReportRequiresRegulatoryFilingSOX() {
        ComplianceReport report = ComplianceReport.builder().complianceType(ComplianceType.SOX).build();
        assertTrue(report.requiresRegulatoryFiling());
    }

    @Test
    void complianceReportRequiresRegulatoryFilingPCI() {
        ComplianceReport report = ComplianceReport.builder().complianceType(ComplianceType.PCI_DSS).build();
        assertTrue(report.requiresRegulatoryFiling());
    }

    @Test
    void complianceReportRequiresRegulatoryFilingGDPRDataBreach() {
        ComplianceReport report = ComplianceReport.builder()
            .complianceType(ComplianceType.GDPR).auditTrail("data breach detected").build();
        assertTrue(report.requiresRegulatoryFiling());
    }

    @Test
    void complianceReportRequiresRegulatoryFilingGDPRNoBreach() {
        ComplianceReport report = ComplianceReport.builder()
            .complianceType(ComplianceType.GDPR).auditTrail("normal operation").build();
        assertFalse(report.requiresRegulatoryFiling());
    }

    @Test
    void complianceReportRequiresRegulatoryFilingOtherType() {
        ComplianceReport report = ComplianceReport.builder().complianceType(ComplianceType.HIPAA).build();
        assertFalse(report.requiresRegulatoryFiling());
    }

    @Test
    void complianceReportGetters() {
        LocalDateTime now = LocalDateTime.now();
        ComplianceReport report = new ComplianceReport("e1", now, ComplianceType.GDPR, "t", "r", "rc", "rp1", now);
        assertEquals("e1", report.getEventId());
        assertEquals(now, report.getTimestamp());
        assertEquals(ComplianceType.GDPR, report.getComplianceType());
        assertEquals("t", report.getAuditTrail());
        assertEquals("r", report.getRiskAssessment());
        assertEquals("rc", report.getRegulatoryContext());
        assertEquals("rp1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
    }

    @Test
    void complianceReportEqualsHashCode() {
        ComplianceReport r1 = ComplianceReport.builder().eventId("e1").reportId("r1").build();
        ComplianceReport r2 = ComplianceReport.builder().eventId("e1").reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void complianceReportToString() {
        ComplianceReport report = ComplianceReport.builder().eventId("e1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("e1"));
    }

    @Test
    void dataRetentionReportNoArgsConstructor() {
        DataRetentionReport report = new DataRetentionReport();
        assertNotNull(report);
        assertNull(report.getReportId());
    }

    @Test
    void dataRetentionReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> policies = Arrays.asList("policy1", "policy2");
        DataRetentionReport report = new DataRetentionReport(
            "rpt-1", now, 1000L, 800L, 150L, 50L, policies, "COMPLIANT"
        );
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(1000L, report.getTotalRecords());
        assertEquals(800L, report.getRetainedRecords());
        assertEquals(150L, report.getArchivedRecords());
        assertEquals(50L, report.getDeletedRecords());
        assertEquals(policies, report.getRetentionPolicies());
        assertEquals("COMPLIANT", report.getComplianceStatus());
    }

    @Test
    void dataRetentionReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        DataRetentionReport report = DataRetentionReport.builder().reportId("rpt-2").generatedAt(now)
            .totalRecords(500L).retainedRecords(400L).archivedRecords(50L).deletedRecords(50L)
            .retentionPolicies(Collections.emptyList()).complianceStatus("NON-COMPLIANT").build();
        assertEquals("rpt-2", report.getReportId());
        assertEquals(500L, report.getTotalRecords());
    }

    @Test
    void dataRetentionReportSettersAndGetters() {
        DataRetentionReport report = new DataRetentionReport();
        LocalDateTime now = LocalDateTime.now();
        report.setReportId("rpt-3");
        report.setGeneratedAt(now);
        report.setTotalRecords(200L);
        report.setRetainedRecords(150L);
        report.setArchivedRecords(30L);
        report.setDeletedRecords(20L);
        report.setRetentionPolicies(Arrays.asList("p1"));
        report.setComplianceStatus("COMPLIANT");
        assertEquals("rpt-3", report.getReportId());
        assertEquals(200L, report.getTotalRecords());
        assertEquals(150L, report.getRetainedRecords());
        assertEquals(30L, report.getArchivedRecords());
        assertEquals(20L, report.getDeletedRecords());
    }

    @Test
    void dataRetentionReportGetPercentageNormal() {
        DataRetentionReport report = DataRetentionReport.builder()
            .totalRecords(100L).retainedRecords(80L).archivedRecords(15L).build();
        assertEquals(95.0, report.getRetentionCompliancePercentage(), 0.001);
    }

    @Test
    void dataRetentionReportGetPercentageZeroTotal() {
        DataRetentionReport report = DataRetentionReport.builder().totalRecords(0L).build();
        assertEquals(100.0, report.getRetentionCompliancePercentage(), 0.001);
    }

    @Test
    void dataRetentionReportGetPercentageNullTotal() {
        DataRetentionReport report = new DataRetentionReport();
        assertEquals(100.0, report.getRetentionCompliancePercentage(), 0.001);
    }

    @Test
    void dataRetentionReportGetPercentageNullRetained() {
        DataRetentionReport report = DataRetentionReport.builder()
            .totalRecords(100L).archivedRecords(50L).build();
        assertEquals(50.0, report.getRetentionCompliancePercentage(), 0.001);
    }

    @Test
    void dataRetentionReportGetPercentageNullArchived() {
        DataRetentionReport report = DataRetentionReport.builder()
            .totalRecords(100L).retainedRecords(70L).build();
        assertEquals(70.0, report.getRetentionCompliancePercentage(), 0.001);
    }

    @Test
    void dataRetentionReportEqualsHashCode() {
        DataRetentionReport r1 = DataRetentionReport.builder().reportId("r1").build();
        DataRetentionReport r2 = DataRetentionReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void dataRetentionReportToString() {
        DataRetentionReport report = DataRetentionReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }

    @Test
    void financialAuditReportNoArgsConstructor() {
        FinancialAuditReport report = new FinancialAuditReport();
        assertNotNull(report);
        assertNull(report.getReportId());
    }

    @Test
    void financialAuditReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> discrepancies = Arrays.asList("d1");
        List<String> recommendations = Arrays.asList("r1");
        FinancialAuditReport report = new FinancialAuditReport(
            "rpt-1", now, now.minusDays(30), now, new BigDecimal("10000.00"), 50L,
            new BigDecimal("200.00"), discrepancies, recommendations, "COMPLIANT"
        );
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(now.minusDays(30), report.getPeriodStart());
        assertEquals(now, report.getPeriodEnd());
        assertEquals(new BigDecimal("10000.00"), report.getTotalTransactionAmount());
        assertEquals(50L, report.getTransactionCount());
        assertEquals(new BigDecimal("200.00"), report.getAverageTransactionAmount());
        assertEquals(discrepancies, report.getDiscrepancies());
        assertEquals(recommendations, report.getRecommendations());
        assertEquals("COMPLIANT", report.getComplianceStatus());
    }

    @Test
    void financialAuditReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-2").generatedAt(now)
            .periodStart(now.minusDays(7)).periodEnd(now)
            .totalTransactionAmount(new BigDecimal("5000.00")).transactionCount(25L)
            .averageTransactionAmount(new BigDecimal("200.00"))
            .discrepancies(Collections.emptyList()).recommendations(Collections.emptyList())
            .complianceStatus("PENDING").build();
        assertEquals("rpt-2", report.getReportId());
        assertEquals(new BigDecimal("5000.00"), report.getTotalTransactionAmount());
    }

    @Test
    void financialAuditReportSettersAndGetters() {
        FinancialAuditReport report = new FinancialAuditReport();
        LocalDateTime now = LocalDateTime.now();
        report.setReportId("rpt-3");
        report.setGeneratedAt(now);
        report.setPeriodStart(now.minusDays(1));
        report.setPeriodEnd(now);
        report.setTotalTransactionAmount(new BigDecimal("1000.00"));
        report.setTransactionCount(10L);
        report.setAverageTransactionAmount(new BigDecimal("100.00"));
        report.setDiscrepancies(Arrays.asList("d1"));
        report.setRecommendations(Arrays.asList("r1"));
        report.setComplianceStatus("COMPLIANT");
        assertEquals("rpt-3", report.getReportId());
        assertEquals(new BigDecimal("1000.00"), report.getTotalTransactionAmount());
        assertEquals(10L, report.getTransactionCount());
    }

    @Test
    void financialAuditReportIsCompleteTrue() {
        LocalDateTime now = LocalDateTime.now();
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1").generatedAt(now)
            .periodStart(now.minusDays(1)).periodEnd(now)
            .totalTransactionAmount(new BigDecimal("100.00")).transactionCount(5L).build();
        assertTrue(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullReportId() {
        FinancialAuditReport report = FinancialAuditReport.builder().generatedAt(LocalDateTime.now())
            .periodStart(LocalDateTime.now()).periodEnd(LocalDateTime.now())
            .totalTransactionAmount(BigDecimal.ONE).transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseEmptyReportId() {
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("  ")
            .generatedAt(LocalDateTime.now()).periodStart(LocalDateTime.now())
            .periodEnd(LocalDateTime.now()).totalTransactionAmount(BigDecimal.ONE)
            .transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullGeneratedAt() {
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1")
            .periodStart(LocalDateTime.now()).periodEnd(LocalDateTime.now())
            .totalTransactionAmount(BigDecimal.ONE).transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullPeriodStart() {
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1")
            .generatedAt(LocalDateTime.now()).periodEnd(LocalDateTime.now())
            .totalTransactionAmount(BigDecimal.ONE).transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullPeriodEnd() {
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1")
            .generatedAt(LocalDateTime.now()).periodStart(LocalDateTime.now())
            .totalTransactionAmount(BigDecimal.ONE).transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullAmount() {
        LocalDateTime now = LocalDateTime.now();
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1").generatedAt(now)
            .periodStart(now).periodEnd(now).transactionCount(1L).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportIsCompleteFalseNullCount() {
        LocalDateTime now = LocalDateTime.now();
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1").generatedAt(now)
            .periodStart(now).periodEnd(now).totalTransactionAmount(BigDecimal.ONE).build();
        assertFalse(report.isComplete());
    }

    @Test
    void financialAuditReportEqualsHashCode() {
        FinancialAuditReport r1 = FinancialAuditReport.builder().reportId("r1").build();
        FinancialAuditReport r2 = FinancialAuditReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void financialAuditReportToString() {
        FinancialAuditReport report = FinancialAuditReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }

    @Test
    void securityIncidentReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        SecurityIncidentReport report = SecurityIncidentReport.builder()
            .reportId("rpt-1").generatedAt(now).periodStart(now.minusDays(7)).periodEnd(now)
            .totalSecurityEvents(50).criticalIncidents(2).highSeverityIncidents(5)
            .mediumSeverityIncidents(10).lowSeverityIncidents(33)
            .criticalSecurityEvents(Collections.emptyList()).suspiciousPatterns(Collections.emptyList())
            .accessDeniedEvents(Collections.emptyList()).privilegeEscalationAttempts(Collections.emptyList())
            .uniqueThreatsDetected(3).topThreatSources(Arrays.asList("src1"))
            .mostTargetedResources(Arrays.asList("res1")).riskAssessment("HIGH")
            .recommendedActions(Arrays.asList("action1"))
            .overallThreatLevel(SecurityThreatLevel.HIGH).build();
        assertEquals("rpt-1", report.getReportId());
        assertEquals(50, report.getTotalSecurityEvents());
        assertEquals(2, report.getCriticalIncidents());
        assertEquals(5, report.getHighSeverityIncidents());
        assertEquals(10, report.getMediumSeverityIncidents());
        assertEquals(33, report.getLowSeverityIncidents());
        assertEquals(SecurityThreatLevel.HIGH, report.getOverallThreatLevel());
        assertEquals(3, report.getUniqueThreatsDetected());
        assertEquals("HIGH", report.getRiskAssessment());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScoreNoCritical() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .highSeverityIncidents(3).suspiciousPatterns(Collections.emptyList())
            .privilegeEscalationAttempts(Collections.emptyList()).build();
        assertEquals(0, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScoreWithCritical() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(1)
            .highSeverityIncidents(3).suspiciousPatterns(Collections.emptyList())
            .privilegeEscalationAttempts(Collections.emptyList()).build();
        assertEquals(40, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScoreHighSeverity() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .highSeverityIncidents(6).suspiciousPatterns(Collections.emptyList())
            .privilegeEscalationAttempts(Collections.emptyList()).build();
        assertEquals(25, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScoreSuspiciousPatterns() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .highSeverityIncidents(0)
            .suspiciousPatterns(Arrays.asList(new AuditEvent(), new AuditEvent(), new AuditEvent(),
                new AuditEvent(), new AuditEvent(), new AuditEvent(), new AuditEvent(),
                new AuditEvent(), new AuditEvent(), new AuditEvent(), new AuditEvent()))
            .privilegeEscalationAttempts(Collections.emptyList()).build();
        assertEquals(20, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScorePrivilegeEscalation() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .highSeverityIncidents(0).suspiciousPatterns(Collections.emptyList())
            .privilegeEscalationAttempts(Arrays.asList(new AuditEvent())).build();
        assertEquals(15, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportGetSecurityRiskScoreMax100() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(5)
            .highSeverityIncidents(10)
            .suspiciousPatterns(Arrays.asList(new AuditEvent(), new AuditEvent(), new AuditEvent(),
                new AuditEvent(), new AuditEvent(), new AuditEvent(), new AuditEvent(),
                new AuditEvent(), new AuditEvent(), new AuditEvent(), new AuditEvent()))
            .privilegeEscalationAttempts(Arrays.asList(new AuditEvent())).build();
        assertEquals(100, report.getSecurityRiskScore());
    }

    @Test
    void securityIncidentReportRequiresImmediateActionCritical() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(1)
            .privilegeEscalationAttempts(Collections.emptyList())
            .overallThreatLevel(SecurityThreatLevel.LOW).build();
        assertTrue(report.requiresImmediateAction());
    }

    @Test
    void securityIncidentReportRequiresImmediateActionPrivEsc() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .privilegeEscalationAttempts(Arrays.asList(new AuditEvent()))
            .overallThreatLevel(SecurityThreatLevel.LOW).build();
        assertTrue(report.requiresImmediateAction());
    }

    @Test
    void securityIncidentReportRequiresImmediateActionCriticalThreat() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .privilegeEscalationAttempts(Collections.emptyList())
            .overallThreatLevel(SecurityThreatLevel.CRITICAL).build();
        assertTrue(report.requiresImmediateAction());
    }

    @Test
    void securityIncidentReportRequiresNoImmediateAction() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().criticalIncidents(0)
            .privilegeEscalationAttempts(Collections.emptyList())
            .overallThreatLevel(SecurityThreatLevel.LOW).build();
        assertFalse(report.requiresImmediateAction());
    }

    @Test
    void securityIncidentReportGetters() {
        LocalDateTime now = LocalDateTime.now();
        SecurityIncidentReport report = SecurityIncidentReport.builder()
            .reportId("r1").generatedAt(now).periodStart(now.minusDays(1)).periodEnd(now)
            .totalSecurityEvents(10).criticalIncidents(1).highSeverityIncidents(2)
            .mediumSeverityIncidents(3).lowSeverityIncidents(4)
            .criticalSecurityEvents(Collections.emptyList()).suspiciousPatterns(Collections.emptyList())
            .accessDeniedEvents(Collections.emptyList()).privilegeEscalationAttempts(Collections.emptyList())
            .uniqueThreatsDetected(1).topThreatSources(Collections.emptyList())
            .mostTargetedResources(Collections.emptyList()).riskAssessment("LOW")
            .recommendedActions(Collections.emptyList()).overallThreatLevel(SecurityThreatLevel.MINIMAL).build();
        assertEquals(now, report.getGeneratedAt());
        assertEquals(now.minusDays(1), report.getPeriodStart());
        assertEquals(now, report.getPeriodEnd());
        assertEquals(10, report.getTotalSecurityEvents());
        assertEquals(1, report.getCriticalIncidents());
        assertEquals(2, report.getHighSeverityIncidents());
        assertEquals(3, report.getMediumSeverityIncidents());
        assertEquals(4, report.getLowSeverityIncidents());
        assertEquals(1, report.getUniqueThreatsDetected());
        assertEquals("LOW", report.getRiskAssessment());
        assertEquals(SecurityThreatLevel.MINIMAL, report.getOverallThreatLevel());
    }

    @Test
    void securityIncidentReportEqualsHashCode() {
        SecurityIncidentReport r1 = SecurityIncidentReport.builder().reportId("r1").build();
        SecurityIncidentReport r2 = SecurityIncidentReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void securityIncidentReportToString() {
        SecurityIncidentReport report = SecurityIncidentReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }

    @Test
    void suspiciousActivityReportNoArgsConstructor() {
        SuspiciousActivityReport report = new SuspiciousActivityReport();
        assertNotNull(report);
        assertNull(report.getReportId());
    }

    @Test
    void suspiciousActivityReportAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> patterns = Arrays.asList("p1");
        List<String> indicators = Arrays.asList("i1");
        SuspiciousActivityReport report = new SuspiciousActivityReport(
            "rpt-1", now, 100L, 20L, 30L, 50L, patterns, indicators, "MEDIUM"
        );
        assertEquals("rpt-1", report.getReportId());
        assertEquals(now, report.getGeneratedAt());
        assertEquals(100L, report.getTotalSuspiciousEvents());
        assertEquals(20L, report.getHighRiskEvents());
        assertEquals(30L, report.getMediumRiskEvents());
        assertEquals(50L, report.getLowRiskEvents());
        assertEquals(patterns, report.getSuspiciousPatterns());
        assertEquals(indicators, report.getRiskIndicators());
        assertEquals("MEDIUM", report.getThreatAssessment());
    }

    @Test
    void suspiciousActivityReportBuilder() {
        LocalDateTime now = LocalDateTime.now();
        SuspiciousActivityReport report = SuspiciousActivityReport.builder().reportId("rpt-2")
            .generatedAt(now).totalSuspiciousEvents(200L).highRiskEvents(40L)
            .mediumRiskEvents(60L).lowRiskEvents(100L)
            .suspiciousPatterns(Collections.emptyList()).riskIndicators(Collections.emptyList())
            .threatAssessment("HIGH").build();
        assertEquals("rpt-2", report.getReportId());
        assertEquals(200L, report.getTotalSuspiciousEvents());
    }

    @Test
    void suspiciousActivityReportSettersAndGetters() {
        SuspiciousActivityReport report = new SuspiciousActivityReport();
        LocalDateTime now = LocalDateTime.now();
        report.setReportId("rpt-3");
        report.setGeneratedAt(now);
        report.setTotalSuspiciousEvents(50L);
        report.setHighRiskEvents(10L);
        report.setMediumRiskEvents(15L);
        report.setLowRiskEvents(25L);
        report.setSuspiciousPatterns(Arrays.asList("p1"));
        report.setRiskIndicators(Arrays.asList("i1"));
        report.setThreatAssessment("LOW");
        assertEquals("rpt-3", report.getReportId());
        assertEquals(50L, report.getTotalSuspiciousEvents());
        assertEquals(10L, report.getHighRiskEvents());
        assertEquals(15L, report.getMediumRiskEvents());
        assertEquals(25L, report.getLowRiskEvents());
    }

    @Test
    void suspiciousActivityReportGetHighRiskPercentageNormal() {
        SuspiciousActivityReport report = SuspiciousActivityReport.builder()
            .totalSuspiciousEvents(100L).highRiskEvents(25L).build();
        assertEquals(25.0, report.getHighRiskPercentage(), 0.001);
    }

    @Test
    void suspiciousActivityReportGetHighRiskPercentageZeroTotal() {
        SuspiciousActivityReport report = SuspiciousActivityReport.builder()
            .totalSuspiciousEvents(0L).build();
        assertEquals(0.0, report.getHighRiskPercentage(), 0.001);
    }

    @Test
    void suspiciousActivityReportGetHighRiskPercentageNullTotal() {
        SuspiciousActivityReport report = new SuspiciousActivityReport();
        assertEquals(0.0, report.getHighRiskPercentage(), 0.001);
    }

    @Test
    void suspiciousActivityReportGetHighRiskPercentageNullHighRisk() {
        SuspiciousActivityReport report = SuspiciousActivityReport.builder()
            .totalSuspiciousEvents(100L).build();
        assertEquals(0.0, report.getHighRiskPercentage(), 0.001);
    }

    @Test
    void suspiciousActivityReportEqualsHashCode() {
        SuspiciousActivityReport r1 = SuspiciousActivityReport.builder().reportId("r1").build();
        SuspiciousActivityReport r2 = SuspiciousActivityReport.builder().reportId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void suspiciousActivityReportToString() {
        SuspiciousActivityReport report = SuspiciousActivityReport.builder().reportId("rpt-1").build();
        assertNotNull(report.toString());
        assertTrue(report.toString().contains("rpt-1"));
    }
}
