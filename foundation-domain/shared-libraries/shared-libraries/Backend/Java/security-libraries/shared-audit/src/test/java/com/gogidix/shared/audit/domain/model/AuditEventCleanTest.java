package com.gogidix.shared.audit.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

class AuditEventCleanTest {

    private AuditEventClean createBasicEvent() {
        return new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        );
    }

    @Test
    void constructor8ParamsCreatesEvent() {
        UUID id = UUID.randomUUID();
        AuditEventClean event = new AuditEventClean(
            id, "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        );
        assertEquals(id, event.getId());
        assertEquals("EVT-001", event.getEventId());
        assertEquals("user-1", event.getUserId());
        assertEquals("tenant-1", event.getTenantId());
        assertEquals(AuditEventTypeClean.USER_LOGIN, event.getEventType());
        assertEquals(BusinessDomainClean.USER_MANAGEMENT, event.getBusinessDomain());
        assertEquals("LOGIN", event.getAction());
        assertEquals(AuditResultClean.SUCCESS, event.getResult());
    }

    @Test
    void constructor8ParamsSetsDefaults() {
        AuditEventClean event = createBasicEvent();
        assertNotNull(event.getCorrelationId());
        assertNotNull(event.getTimestamp());
        assertEquals("2.0.0", event.getVersion());
        assertNull(event.getCausationId());
        assertNull(event.getSessionId());
        assertNull(event.getOrganizationId());
        assertNull(event.getUserRole());
        assertTrue(event.getUserPermissions().isEmpty());
        assertNull(event.getSubAction());
        assertNotNull(event.getSeverity());
        assertNull(event.getResourceType());
        assertNull(event.getResourceId());
        assertNull(event.getResourceName());
        assertNull(event.getParentResourceId());
        assertTrue(event.getResourceMetadata().isEmpty());
        assertEquals("gogidix-audit-system", event.getSourceSystem());
        assertEquals("1.0.0", event.getServiceVersion());
        assertNull(event.getIpAddress());
        assertNull(event.getUserAgent());
        assertNull(event.getRequestMethod());
        assertNull(event.getRequestUri());
        assertNull(event.getGeographicLocation());
        assertNull(event.getDeviceFingerprint());
        assertNull(event.getBusinessProcess());
        assertNull(event.getWorkflowId());
        assertNull(event.getTransactionId());
        assertNull(event.getFinancialAmount());
        assertNull(event.getCurrency());
        assertNull(event.getPaymentMethod());
        assertNull(event.getVendorId());
        assertNull(event.getCustomerId());
        assertEquals(ComplianceTypeClean.STANDARD, event.getComplianceType());
        assertTrue(event.getRegulatoryRequirements().isEmpty());
        assertEquals(SecurityClassificationClean.PUBLIC, event.getSecurityClassification());
        assertNotNull(event.getRiskLevel());
        assertNotNull(event.getRiskScore());
        assertFalse(event.getContainsPII());
        assertFalse(event.getContainsPCI());
        assertFalse(event.getContainsPHI());
        assertNull(event.getProcessingTimeMs());
        assertNull(event.getResponseTimeMs());
        assertEquals("STANDARD", event.getPerformanceTier());
        assertNull(event.getCpuUtilization());
        assertNull(event.getMemoryUtilization());
        assertEquals(0, event.getErrorCount());
        assertNull(event.getErrorCode());
        assertNull(event.getErrorMessage());
        assertNull(event.getDescription());
        assertNull(event.getBusinessReason());
        assertTrue(event.getBeforeState().isEmpty());
        assertTrue(event.getAfterState().isEmpty());
        assertTrue(event.getAdditionalMetadata().isEmpty());
        assertTrue(event.getEvidenceFiles().isEmpty());
        assertNull(event.getLegalHold());
        assertNotNull(event.getRetentionExpiryDate());
        assertFalse(event.getIsArchived());
        assertNull(event.getArchiveLocation());
        assertNotNull(event.getDigitalSignature());
        assertTrue(event.getIsImmutable());
    }

    @Test
    void constructorRejectsNullId() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            null, "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullEventId() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), null, "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsEmptyEventId() {
        assertThrows(IllegalArgumentException.class, () -> new AuditEventClean(
            UUID.randomUUID(), " ", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullUserId() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", null, "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullTenantId() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", null,
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullEventType() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            null, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullBusinessDomain() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, null,
            "LOGIN", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullAction() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            null, AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsEmptyAction() {
        assertThrows(IllegalArgumentException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            " ", AuditResultClean.SUCCESS
        ));
    }

    @Test
    void constructorRejectsNullResult() {
        assertThrows(NullPointerException.class, () -> new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT,
            "LOGIN", null
        ));
    }

    @Test
    void builderCreatesEvent() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .build();
        assertNotNull(event.getId());
        assertNotNull(event.getEventId());
        assertEquals("user-1", event.getUserId());
        assertEquals("tenant-1", event.getTenantId());
    }

    @Test
    void builderWithAllFields() {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("key", "value");
        AuditEventClean event = AuditEventClean.builder()
            .id(UUID.randomUUID())
            .eventId("EVT-BUILDER")
            .correlationId("corr-1")
            .causationId("caus-1")
            .timestamp(LocalDateTime.now())
            .version("3.0.0")
            .userId("user-1")
            .sessionId("sess-1")
            .tenantId("tenant-1")
            .organizationId("org-1")
            .userRole("ADMIN")
            .userPermissions(Set.of("READ", "WRITE"))
            .eventType(AuditEventTypeClean.DATA_MODIFICATION)
            .businessDomain(BusinessDomainClean.DATA_MANAGEMENT)
            .action("MODIFY")
            .subAction("UPDATE")
            .result(AuditResultClean.SUCCESS)
            .severity(AuditSeverityClean.MEDIUM)
            .resourceType("Document")
            .resourceId("doc-1")
            .resourceName("Report")
            .parentResourceId("folder-1")
            .resourceMetadata(metadata)
            .sourceSystem("test-system")
            .serviceVersion("2.0.0")
            .ipAddress("192.168.1.1")
            .userAgent("TestAgent/1.0")
            .requestMethod("POST")
            .requestUri("/api/docs")
            .geographicLocation("Nigeria")
            .deviceFingerprint("fp-123")
            .businessProcess("Reporting")
            .workflowId("wf-1")
            .transactionId("tx-1")
            .financialAmount(BigDecimal.valueOf(5000))
            .currency("NGN")
            .paymentMethod("CARD")
            .vendorId("vendor-1")
            .customerId("cust-1")
            .complianceType(ComplianceTypeClean.GDPR)
            .regulatoryRequirements(Set.of("GDPR"))
            .securityClassification(SecurityClassificationClean.CONFIDENTIAL)
            .riskLevel(RiskLevelClean.MEDIUM)
            .riskScore(50)
            .containsPII(true)
            .containsPCI(false)
            .containsPHI(false)
            .processingTimeMs(100L)
            .responseTimeMs(50L)
            .performanceTier("FAST")
            .cpuUtilization(0.5)
            .memoryUtilization(0.3)
            .errorCount(0)
            .errorCode(null)
            .errorMessage(null)
            .description("Test event")
            .businessReason("Testing")
            .beforeState(metadata)
            .afterState(metadata)
            .additionalMetadata(metadata)
            .evidenceFiles(List.of("file1.pdf"))
            .legalHold("legal-hold-1")
            .isArchived(false)
            .archiveLocation("s3://archive")
            .build();
        assertEquals("EVT-BUILDER", event.getEventId());
        assertEquals("corr-1", event.getCorrelationId());
        assertEquals("caus-1", event.getCausationId());
        assertEquals("3.0.0", event.getVersion());
        assertEquals("sess-1", event.getSessionId());
        assertEquals("org-1", event.getOrganizationId());
        assertEquals("ADMIN", event.getUserRole());
        assertEquals(2, event.getUserPermissions().size());
        assertEquals(AuditEventTypeClean.DATA_MODIFICATION, event.getEventType());
        assertEquals("UPDATE", event.getSubAction());
        assertEquals(AuditSeverityClean.MEDIUM, event.getSeverity());
        assertEquals("Document", event.getResourceType());
        assertEquals("doc-1", event.getResourceId());
        assertEquals("Report", event.getResourceName());
        assertEquals("folder-1", event.getParentResourceId());
        assertEquals(1, event.getResourceMetadata().size());
        assertEquals("test-system", event.getSourceSystem());
        assertEquals("2.0.0", event.getServiceVersion());
        assertEquals("192.168.1.1", event.getIpAddress());
        assertEquals("TestAgent/1.0", event.getUserAgent());
        assertEquals("POST", event.getRequestMethod());
        assertEquals("/api/docs", event.getRequestUri());
        assertEquals("Nigeria", event.getGeographicLocation());
        assertEquals("fp-123", event.getDeviceFingerprint());
        assertEquals("Reporting", event.getBusinessProcess());
        assertEquals("wf-1", event.getWorkflowId());
        assertEquals("tx-1", event.getTransactionId());
        assertEquals(BigDecimal.valueOf(5000), event.getFinancialAmount());
        assertEquals("NGN", event.getCurrency());
        assertEquals("CARD", event.getPaymentMethod());
        assertEquals("vendor-1", event.getVendorId());
        assertEquals("cust-1", event.getCustomerId());
        assertEquals(ComplianceTypeClean.GDPR, event.getComplianceType());
        assertTrue(event.getRegulatoryRequirements().contains("GDPR"));
        assertEquals(SecurityClassificationClean.CONFIDENTIAL, event.getSecurityClassification());
        assertEquals(RiskLevelClean.MEDIUM, event.getRiskLevel());
        assertEquals(50, event.getRiskScore());
        assertTrue(event.getContainsPII());
        assertFalse(event.getContainsPCI());
        assertEquals(100L, event.getProcessingTimeMs());
        assertEquals(50L, event.getResponseTimeMs());
        assertEquals("FAST", event.getPerformanceTier());
        assertEquals(0.5, event.getCpuUtilization());
        assertEquals(0.3, event.getMemoryUtilization());
        assertEquals("Test event", event.getDescription());
        assertEquals("Testing", event.getBusinessReason());
        assertEquals(1, event.getEvidenceFiles().size());
        assertEquals("legal-hold-1", event.getLegalHold());
        assertEquals("s3://archive", event.getArchiveLocation());
    }

    @Test
    void builderSetsDefaultRetentionAndSignature() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .build();
        assertNotNull(event.getRetentionExpiryDate());
        assertNotNull(event.getDigitalSignature());
    }

    @Test
    void createSecurityEvent() {
        AuditEventClean event = AuditEventClean.createSecurityEvent(
            "user-1", "tenant-1", "INTRUSION_ATTEMPT", "10.0.0.1", AuditResultClean.FAILURE
        );
        assertTrue(event.getEventId().startsWith("SEC-"));
        assertEquals("user-1", event.getUserId());
        assertEquals(AuditEventTypeClean.SECURITY_EVENT, event.getEventType());
        assertEquals(BusinessDomainClean.SECURITY, event.getBusinessDomain());
        assertEquals(AuditResultClean.FAILURE, event.getResult());
    }

    @Test
    void createFinancialTransaction() {
        assertThrows(IllegalArgumentException.class, () ->
            AuditEventClean.createFinancialTransaction(
                "user-1", "tenant-1", BigDecimal.valueOf(500), "NGN", "TX-123"
            )
        );
    }

    @Test
    void createComplianceEvent() {
        AuditEventClean event = AuditEventClean.createComplianceEvent(
            "user-1", "tenant-1", ComplianceTypeClean.GDPR, "CHECK_CONSENT", AuditResultClean.SUCCESS
        );
        assertTrue(event.getEventId().startsWith("COMP-"));
        assertEquals(AuditEventTypeClean.COMPLIANCE_CHECK, event.getEventType());
        assertEquals(BusinessDomainClean.COMPLIANCE, event.getBusinessDomain());
        assertEquals(ComplianceTypeClean.GDPR, event.getComplianceType());
    }

    @Test
    void isSecurityCriticalWithSecurityEvent() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.SECURITY_EVENT, BusinessDomainClean.SECURITY,
            "ALERT", AuditResultClean.SUCCESS
        );
        assertTrue(event.isSecurityCritical());
    }

    @Test
    void isSecurityCriticalWithUnauthorizedAccess() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.UNAUTHORIZED_ACCESS, BusinessDomainClean.SECURITY,
            "ACCESS", AuditResultClean.ACCESS_DENIED
        );
        assertTrue(event.isSecurityCritical());
    }

    @Test
    void isNotSecurityCriticalForNonSecurityEvent() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.DATA_ACCESS, BusinessDomainClean.DATA_MANAGEMENT,
            "READ", AuditResultClean.SUCCESS
        );
        assertFalse(event.isSecurityCritical());
    }

    @Test
    void calculateBusinessImpactScoreForNormalEvent() {
        AuditEventClean event = createBasicEvent();
        int score = event.calculateBusinessImpactScore();
        assertTrue(score >= 0 && score <= 100);
    }

    @Test
    void calculateBusinessImpactScoreForFailureEvent() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.FINANCIAL_TRANSACTION)
            .businessDomain(BusinessDomainClean.FINANCE)
            .action("PAY")
            .result(AuditResultClean.FAILURE)
            .severity(AuditSeverityClean.HIGH)
            .riskLevel(RiskLevelClean.HIGH)
            .financialAmount(BigDecimal.valueOf(50000))
            .currency("NGN")
            .build();
        int score = event.calculateBusinessImpactScore();
        assertTrue(score > 0);
    }

    @Test
    void calculateBusinessImpactScoreWithLargeAmount() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.FINANCIAL_TRANSACTION)
            .businessDomain(BusinessDomainClean.FINANCE)
            .action("PAY")
            .result(AuditResultClean.SUCCESS)
            .financialAmount(BigDecimal.valueOf(2000000))
            .currency("NGN")
            .build();
        int score = event.calculateBusinessImpactScore();
        assertTrue(score >= 25);
    }

    @Test
    void requiresRegulatoryReportingForLargeFinancial() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.FINANCIAL_TRANSACTION)
            .businessDomain(BusinessDomainClean.FINANCE)
            .action("PAY")
            .result(AuditResultClean.SUCCESS)
            .financialAmount(BigDecimal.valueOf(50000))
            .currency("NGN")
            .complianceType(ComplianceTypeClean.PCI_DSS)
            .build();
        assertTrue(event.requiresRegulatoryReporting());
    }

    @Test
    void doesNotRequireRegulatoryReportingForNonSecurityEvent() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.DATA_ACCESS, BusinessDomainClean.DATA_MANAGEMENT,
            "READ", AuditResultClean.SUCCESS
        );
        assertFalse(event.requiresRegulatoryReporting());
    }

    @Test
    void getRequiredRetentionPeriodSOX() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .complianceType(ComplianceTypeClean.SOX)
            .build();
        assertEquals(Duration.ofDays(2555), event.getRequiredRetentionPeriod());
    }

    @Test
    void getRequiredRetentionPeriodGDPR() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .complianceType(ComplianceTypeClean.GDPR)
            .containsPII(true)
            .build();
        assertEquals(Duration.ofDays(2190), event.getRequiredRetentionPeriod());
    }

    @Test
    void getRequiredRetentionPeriodDefault() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.DATA_ACCESS, BusinessDomainClean.DATA_MANAGEMENT,
            "READ", AuditResultClean.SUCCESS
        );
        assertEquals(Duration.ofDays(365), event.getRequiredRetentionPeriod());
    }

    @Test
    void hasExpiredRetentionFalseForNewEvent() {
        AuditEventClean event = createBasicEvent();
        assertFalse(event.hasExpiredRetention());
    }

    @Test
    void hasExpiredRetentionTrueForPastDate() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .retentionExpiryDate(LocalDateTime.now().minusDays(1))
            .build();
        assertTrue(event.hasExpiredRetention());
    }

    @Test
    void canBeArchivedFalseForRecentEvent() {
        AuditEventClean event = createBasicEvent();
        assertFalse(event.canBeArchived());
    }

    @Test
    void calculateCriticalityNegligible() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.DATA_ACCESS, BusinessDomainClean.DATA_MANAGEMENT,
            "READ", AuditResultClean.SUCCESS
        );
        assertEquals(EventCriticalityClean.NEGLIGIBLE, event.calculateCriticality());
    }

    @Test
    void calculateCriticalityForSecurityEvent() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.SECURITY_EVENT, BusinessDomainClean.SECURITY,
            "ALERT", AuditResultClean.FAILURE
        );
        EventCriticalityClean c = event.calculateCriticality();
        assertNotNull(c);
    }

    @Test
    void generateComplianceEvidence() {
        AuditEventClean event = createBasicEvent();
        assertNotNull(event.generateComplianceEvidence());
    }

    @Test
    void createFraudReportReturnsNullForNormalEvent() {
        AuditEventClean event = createBasicEvent();
        assertNull(event.createFraudReport());
    }

    @Test
    void createFraudReportReturnsReportForFraudEvent() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.FRAUD_SUSPECTED)
            .businessDomain(BusinessDomainClean.FINANCE)
            .action("TRANSFER")
            .result(AuditResultClean.FAILURE)
            .riskScore(90)
            .financialAmount(BigDecimal.valueOf(10000))
            .currency("NGN")
            .build();
        assertNotNull(event.createFraudReport());
    }

    @Test
    void requiresImmediateEscalationForSecurityEvent() {
        AuditEventClean event = new AuditEventClean(
            UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.SECURITY_EVENT, BusinessDomainClean.SECURITY,
            "ALERT", AuditResultClean.FAILURE
        );
        assertTrue(event.requiresImmediateEscalation());
    }

    @Test
    void requiresImmediateEscalationForHighRiskScore() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .riskScore(90)
            .build();
        assertTrue(event.requiresImmediateEscalation());
    }

    @Test
    void getRecommendedEscalationsForNormalEvent() {
        AuditEventClean event = createBasicEvent();
        List<EscalationActionClean> actions = event.getRecommendedEscalations();
        assertNotNull(actions);
    }

    @Test
    void getRecommendedEscalationsForSecurityAndFraud() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.FRAUD_SUSPECTED)
            .businessDomain(BusinessDomainClean.FINANCE)
            .action("TRANSFER")
            .result(AuditResultClean.FAILURE)
            .riskScore(90)
            .complianceType(ComplianceTypeClean.PCI_DSS)
            .financialAmount(BigDecimal.valueOf(100000))
            .currency("NGN")
            .build();
        List<EscalationActionClean> actions = event.getRecommendedEscalations();
        assertFalse(actions.isEmpty());
    }

    @Test
    void withRiskAssessmentCreatesNewEvent() {
        AuditEventClean original = createBasicEvent();
        AuditEventClean modified = original.withRiskAssessment(RiskLevelClean.HIGH, 80);
        assertEquals(RiskLevelClean.HIGH, modified.getRiskLevel());
        assertEquals(80, modified.getRiskScore());
        assertEquals(original.getId(), modified.getId());
        assertEquals(original.getEventId(), modified.getEventId());
    }

    @Test
    void withComplianceEnhancementCreatesNewEvent() {
        AuditEventClean original = createBasicEvent();
        AuditEventClean modified = original.withComplianceEnhancement(
            ComplianceTypeClean.GDPR, Set.of("GDPR", "PRIVACY")
        );
        assertEquals(ComplianceTypeClean.GDPR, modified.getComplianceType());
        assertEquals(2, modified.getRegulatoryRequirements().size());
        assertEquals(original.getId(), modified.getId());
    }

    @Test
    void withLegalHoldCreatesNewEvent() {
        AuditEventClean original = createBasicEvent();
        AuditEventClean modified = original.withLegalHold("pending-litigation");
        assertEquals("pending-litigation", modified.getLegalHold());
        assertEquals(original.getId(), modified.getId());
    }

    @Test
    void equalsSameIdAndEventId() {
        UUID id = UUID.randomUUID();
        AuditEventClean e1 = new AuditEventClean(id, "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT, "LOGIN", AuditResultClean.SUCCESS);
        AuditEventClean e2 = new AuditEventClean(id, "EVT-001", "user-2", "tenant-2",
            AuditEventTypeClean.USER_LOGOUT, BusinessDomainClean.SECURITY, "LOGOUT", AuditResultClean.FAILURE);
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void equalsDifferentId() {
        AuditEventClean e1 = new AuditEventClean(UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT, "LOGIN", AuditResultClean.SUCCESS);
        AuditEventClean e2 = new AuditEventClean(UUID.randomUUID(), "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT, "LOGIN", AuditResultClean.SUCCESS);
        assertNotEquals(e1, e2);
    }

    @Test
    void equalsDifferentEventId() {
        UUID id = UUID.randomUUID();
        AuditEventClean e1 = new AuditEventClean(id, "EVT-001", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT, "LOGIN", AuditResultClean.SUCCESS);
        AuditEventClean e2 = new AuditEventClean(id, "EVT-002", "user-1", "tenant-1",
            AuditEventTypeClean.USER_LOGIN, BusinessDomainClean.USER_MANAGEMENT, "LOGIN", AuditResultClean.SUCCESS);
        assertNotEquals(e1, e2);
    }

    @Test
    void equalsSelf() {
        AuditEventClean event = createBasicEvent();
        assertEquals(event, event);
    }

    @Test
    void equalsNull() {
        AuditEventClean event = createBasicEvent();
        assertNotEquals(null, event);
    }

    @Test
    void equalsDifferentType() {
        AuditEventClean event = createBasicEvent();
        assertNotEquals("not an event", event);
    }

    @Test
    void toStringContainsFields() {
        AuditEventClean event = createBasicEvent();
        String s = event.toString();
        assertTrue(s.contains("AuditEventClean"));
        assertTrue(s.contains("EVT-001"));
        assertTrue(s.contains("user-1"));
    }

    @Test
    void isComplianceViolationForComplianceResult() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.COMPLIANCE_CHECK)
            .businessDomain(BusinessDomainClean.COMPLIANCE)
            .action("CHECK")
            .result(AuditResultClean.COMPLIANCE_VIOLATION)
            .build();
        assertTrue(event.isComplianceViolation());
    }

    @Test
    void isNotComplianceViolationForNormalEvent() {
        AuditEventClean event = createBasicEvent();
        assertFalse(event.isComplianceViolation());
    }

    @Test
    void isPerformanceDegradedWithSlowResponse() {
        AuditEventClean event = AuditEventClean.builder()
            .userId("user-1")
            .tenantId("tenant-1")
            .eventType(AuditEventTypeClean.USER_LOGIN)
            .businessDomain(BusinessDomainClean.USER_MANAGEMENT)
            .action("LOGIN")
            .result(AuditResultClean.SUCCESS)
            .responseTimeMs(10000L)
            .build();
        assertTrue(event.calculateBusinessImpactScore() >= 10);
    }

    @Test
    void allAuditEventTypeCleanValues() {
        AuditEventTypeClean[] values = AuditEventTypeClean.values();
        assertEquals(20, values.length);
        for (AuditEventTypeClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allBusinessDomainCleanValues() {
        BusinessDomainClean[] values = BusinessDomainClean.values();
        assertEquals(10, values.length);
        for (BusinessDomainClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allAuditResultCleanValues() {
        AuditResultClean[] values = AuditResultClean.values();
        assertEquals(6, values.length);
        for (AuditResultClean v : values) {
            assertNotNull(v.getDisplayName());
            assertTrue(v.getRiskScore() >= 0);
        }
    }

    @Test
    void allAuditSeverityCleanValues() {
        AuditSeverityClean[] values = AuditSeverityClean.values();
        assertEquals(4, values.length);
        for (AuditSeverityClean v : values) {
            assertNotNull(v.getDisplayName());
            assertTrue(v.getBaseScore() > 0);
        }
    }

    @Test
    void allComplianceTypeCleanValues() {
        ComplianceTypeClean[] values = ComplianceTypeClean.values();
        assertEquals(7, values.length);
        for (ComplianceTypeClean v : values) {
            assertNotNull(v.getDisplayName());
            assertNotNull(v.getRetentionPeriod());
            assertNotNull(v.getRequiredStandards());
        }
    }

    @Test
    void allSecurityClassificationCleanValues() {
        SecurityClassificationClean[] values = SecurityClassificationClean.values();
        assertEquals(5, values.length);
        for (SecurityClassificationClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allRiskLevelCleanValues() {
        RiskLevelClean[] values = RiskLevelClean.values();
        assertEquals(4, values.length);
        for (RiskLevelClean v : values) {
            assertNotNull(v.getDisplayName());
            assertTrue(v.getBaseScore() > 0);
            assertTrue(v.getImpactScore() > 0);
        }
    }

    @Test
    void allEventCriticalityCleanValues() {
        EventCriticalityClean[] values = EventCriticalityClean.values();
        assertEquals(5, values.length);
        for (EventCriticalityClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allEscalationActionCleanValues() {
        EscalationActionClean[] values = EscalationActionClean.values();
        assertEquals(10, values.length);
        for (EscalationActionClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allFraudIndicatorCleanValues() {
        FraudIndicatorClean[] values = FraudIndicatorClean.values();
        assertEquals(6, values.length);
        for (FraudIndicatorClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void allFraudActionRecommendationCleanValues() {
        FraudActionRecommendationClean[] values = FraudActionRecommendationClean.values();
        assertEquals(4, values.length);
        for (FraudActionRecommendationClean v : values) {
            assertNotNull(v.getDisplayName());
        }
    }

    @Test
    void valueOfForEnums() {
        assertEquals(AuditEventTypeClean.USER_LOGIN, AuditEventTypeClean.valueOf("USER_LOGIN"));
        assertEquals(BusinessDomainClean.SECURITY, BusinessDomainClean.valueOf("SECURITY"));
        assertEquals(AuditResultClean.SUCCESS, AuditResultClean.valueOf("SUCCESS"));
        assertEquals(AuditSeverityClean.HIGH, AuditSeverityClean.valueOf("HIGH"));
        assertEquals(ComplianceTypeClean.GDPR, ComplianceTypeClean.valueOf("GDPR"));
        assertEquals(SecurityClassificationClean.SECRET, SecurityClassificationClean.valueOf("SECRET"));
        assertEquals(RiskLevelClean.CRITICAL, RiskLevelClean.valueOf("CRITICAL"));
        assertEquals(EventCriticalityClean.SEVERE, EventCriticalityClean.valueOf("SEVERE"));
        assertEquals(EscalationActionClean.FREEZE_ACCOUNT, EscalationActionClean.valueOf("FREEZE_ACCOUNT"));
        assertEquals(FraudIndicatorClean.UNUSUAL_GEOGRAPHIC_LOCATION, FraudIndicatorClean.valueOf("UNUSUAL_GEOGRAPHIC_LOCATION"));
        assertEquals(FraudActionRecommendationClean.IMMEDIATE_BLOCK, FraudActionRecommendationClean.valueOf("IMMEDIATE_BLOCK"));
    }

    @Test
    void eventTypeCleanBooleans() {
        assertTrue(AuditEventTypeClean.SECURITY_EVENT.isSecurityEvent());
        assertFalse(AuditEventTypeClean.SECURITY_EVENT.isFinancialEvent());
        assertTrue(AuditEventTypeClean.SECURITY_EVENT.isBusinessCritical());
        assertTrue(AuditEventTypeClean.FINANCIAL_TRANSACTION.isFinancialEvent());
        assertFalse(AuditEventTypeClean.FINANCIAL_TRANSACTION.isSecurityEvent());
        assertFalse(AuditEventTypeClean.API_CALL.isFinancialEvent());
        assertFalse(AuditEventTypeClean.API_CALL.isSecurityEvent());
        assertFalse(AuditEventTypeClean.API_CALL.isBusinessCritical());
        assertTrue(AuditEventTypeClean.USER_LOGIN.isSecurityEvent());
    }

    @Test
    void complianceTypeRequiresReporting() {
        assertTrue(ComplianceTypeClean.GDPR.requiresReporting());
        assertTrue(ComplianceTypeClean.PCI_DSS.requiresReporting());
        assertTrue(ComplianceTypeClean.SOX.requiresReporting());
        assertTrue(ComplianceTypeClean.HIPAA.requiresReporting());
        assertFalse(ComplianceTypeClean.NONE.requiresReporting());
        assertFalse(ComplianceTypeClean.STANDARD.requiresReporting());
    }
}
