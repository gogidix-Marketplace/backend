package com.gogidix.shared.audit.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class EnumBatchTest {

    @Test
    void auditDetailLevelValues() {
        assertEquals(5, AuditDetailLevel.values().length);
    }

    @Test
    void auditDetailLevelValueOf() {
        assertEquals(AuditDetailLevel.MINIMAL, AuditDetailLevel.valueOf("MINIMAL"));
        assertEquals(AuditDetailLevel.STANDARD, AuditDetailLevel.valueOf("STANDARD"));
        assertEquals(AuditDetailLevel.DETAILED, AuditDetailLevel.valueOf("DETAILED"));
        assertEquals(AuditDetailLevel.COMPREHENSIVE, AuditDetailLevel.valueOf("COMPREHENSIVE"));
        assertEquals(AuditDetailLevel.FORENSIC, AuditDetailLevel.valueOf("FORENSIC"));
    }

    @ParameterizedTest
    @EnumSource(AuditDetailLevel.class)
    void auditDetailLevelGetters(AuditDetailLevel level) {
        assertNotNull(level.getDisplayName());
        assertNotNull(level.getDescription());
    }

    @Test
    void auditDetailLevelIncludesMetadata() {
        assertFalse(AuditDetailLevel.MINIMAL.includesMetadata());
        assertFalse(AuditDetailLevel.STANDARD.includesMetadata());
        assertTrue(AuditDetailLevel.DETAILED.includesMetadata());
        assertTrue(AuditDetailLevel.COMPREHENSIVE.includesMetadata());
        assertTrue(AuditDetailLevel.FORENSIC.includesMetadata());
    }

    @Test
    void auditDetailLevelIncludesPerformanceMetrics() {
        assertFalse(AuditDetailLevel.MINIMAL.includesPerformanceMetrics());
        assertFalse(AuditDetailLevel.STANDARD.includesPerformanceMetrics());
        assertFalse(AuditDetailLevel.DETAILED.includesPerformanceMetrics());
        assertTrue(AuditDetailLevel.COMPREHENSIVE.includesPerformanceMetrics());
        assertTrue(AuditDetailLevel.FORENSIC.includesPerformanceMetrics());
    }

    @Test
    void auditDetailLevelIncludesFullRequestResponse() {
        assertFalse(AuditDetailLevel.MINIMAL.includesFullRequestResponse());
        assertFalse(AuditDetailLevel.STANDARD.includesFullRequestResponse());
        assertFalse(AuditDetailLevel.DETAILED.includesFullRequestResponse());
        assertFalse(AuditDetailLevel.COMPREHENSIVE.includesFullRequestResponse());
        assertTrue(AuditDetailLevel.FORENSIC.includesFullRequestResponse());
    }

    @Test
    void auditEventTypeValues() {
        assertTrue(AuditEventType.values().length > 0);
    }

    @Test
    void auditEventTypeValueOf() {
        assertEquals(AuditEventType.USER_LOGIN, AuditEventType.valueOf("USER_LOGIN"));
        assertEquals(AuditEventType.SECURITY_EVENT, AuditEventType.valueOf("SECURITY_EVENT"));
        assertEquals(AuditEventType.FINANCIAL_TRANSACTION, AuditEventType.valueOf("FINANCIAL_TRANSACTION"));
    }

    @ParameterizedTest
    @EnumSource(AuditEventType.class)
    void auditEventTypeGetters(AuditEventType type) {
        assertNotNull(type.getDescription());
    }

    @Test
    void auditEventTypeRequiresSecurityMonitoring() {
        assertTrue(AuditEventType.SECURITY_EVENT.requiresSecurityMonitoring());
        assertTrue(AuditEventType.ACCESS_DENIED.requiresSecurityMonitoring());
        assertTrue(AuditEventType.PRIVILEGE_ESCALATION.requiresSecurityMonitoring());
        assertTrue(AuditEventType.UNAUTHORIZED_ACCESS.requiresSecurityMonitoring());
        assertTrue(AuditEventType.MALICIOUS_ACTIVITY.requiresSecurityMonitoring());
        assertTrue(AuditEventType.SECURITY_POLICY_VIOLATION.requiresSecurityMonitoring());
        assertFalse(AuditEventType.USER_LOGIN.requiresSecurityMonitoring());
        assertFalse(AuditEventType.DATA_ACCESS.requiresSecurityMonitoring());
    }

    @Test
    void auditEventTypeIsFinancialEvent() {
        assertTrue(AuditEventType.FINANCIAL_TRANSACTION.isFinancialEvent());
        assertTrue(AuditEventType.PAYMENT_PROCESSING.isFinancialEvent());
        assertTrue(AuditEventType.REFUND_PROCESSING.isFinancialEvent());
        assertTrue(AuditEventType.REFUND_PROCESSED.isFinancialEvent());
        assertTrue(AuditEventType.COMMISSION_CALCULATION.isFinancialEvent());
        assertTrue(AuditEventType.INVOICE_GENERATION.isFinancialEvent());
        assertTrue(AuditEventType.CHARGEBACK_EVENT.isFinancialEvent());
        assertTrue(AuditEventType.SETTLEMENT_PROCESSING.isFinancialEvent());
        assertFalse(AuditEventType.USER_LOGIN.isFinancialEvent());
        assertFalse(AuditEventType.DATA_ACCESS.isFinancialEvent());
    }

    @Test
    void auditEventTypeInvolvesSensitiveData() {
        assertTrue(AuditEventType.SENSITIVE_DATA_ACCESS.involvesSensitiveData());
        assertTrue(AuditEventType.DATA_EXPORT.involvesSensitiveData());
        assertTrue(AuditEventType.GDPR_REQUEST.involvesSensitiveData());
        assertTrue(AuditEventType.FINANCIAL_TRANSACTION.involvesSensitiveData());
        assertTrue(AuditEventType.USER_PROFILE_UPDATE.involvesSensitiveData());
        assertFalse(AuditEventType.USER_LOGIN.involvesSensitiveData());
        assertFalse(AuditEventType.DATA_ACCESS.involvesSensitiveData());
    }

    @Test
    void auditEventTypeGetDefaultRetentionDays() {
        assertEquals(2555, AuditEventType.FINANCIAL_TRANSACTION.getDefaultRetentionDays());
        assertEquals(1095, AuditEventType.SECURITY_EVENT.getDefaultRetentionDays());
        assertEquals(365, AuditEventType.SENSITIVE_DATA_ACCESS.getDefaultRetentionDays());
        assertEquals(90, AuditEventType.USER_LOGIN.getDefaultRetentionDays());
        assertEquals(90, AuditEventType.DATA_ACCESS.getDefaultRetentionDays());
    }

    @Test
    void auditSeverityValues() {
        assertEquals(5, AuditSeverity.values().length);
    }

    @Test
    void auditSeverityValueOf() {
        assertEquals(AuditSeverity.CRITICAL, AuditSeverity.valueOf("CRITICAL"));
        assertEquals(AuditSeverity.HIGH, AuditSeverity.valueOf("HIGH"));
        assertEquals(AuditSeverity.MEDIUM, AuditSeverity.valueOf("MEDIUM"));
        assertEquals(AuditSeverity.LOW, AuditSeverity.valueOf("LOW"));
        assertEquals(AuditSeverity.INFO, AuditSeverity.valueOf("INFO"));
    }

    @ParameterizedTest
    @EnumSource(AuditSeverity.class)
    void auditSeverityGetters(AuditSeverity severity) {
        assertTrue(severity.getLevel() >= 0);
        assertNotNull(severity.getDisplayName());
        assertNotNull(severity.getDescription());
    }

    @Test
    void auditSeverityRequiresImmediateNotification() {
        assertTrue(AuditSeverity.CRITICAL.requiresImmediateNotification());
        assertTrue(AuditSeverity.HIGH.requiresImmediateNotification());
        assertFalse(AuditSeverity.MEDIUM.requiresImmediateNotification());
        assertFalse(AuditSeverity.LOW.requiresImmediateNotification());
        assertFalse(AuditSeverity.INFO.requiresImmediateNotification());
    }

    @Test
    void auditSeverityGetEscalationTimeoutMinutes() {
        assertEquals(5, AuditSeverity.CRITICAL.getEscalationTimeoutMinutes());
        assertEquals(60, AuditSeverity.HIGH.getEscalationTimeoutMinutes());
        assertEquals(1440, AuditSeverity.MEDIUM.getEscalationTimeoutMinutes());
        assertEquals(0, AuditSeverity.LOW.getEscalationTimeoutMinutes());
        assertEquals(0, AuditSeverity.INFO.getEscalationTimeoutMinutes());
    }

    @Test
    void auditSeverityRequiresEncryption() {
        assertTrue(AuditSeverity.CRITICAL.requiresEncryption());
        assertTrue(AuditSeverity.HIGH.requiresEncryption());
        assertFalse(AuditSeverity.MEDIUM.requiresEncryption());
        assertFalse(AuditSeverity.LOW.requiresEncryption());
        assertFalse(AuditSeverity.INFO.requiresEncryption());
    }

    @Test
    void businessDomainValues() {
        assertTrue(BusinessDomain.values().length > 0);
    }

    @Test
    void businessDomainValueOf() {
        assertEquals(BusinessDomain.SHARED_INFRASTRUCTURE, BusinessDomain.valueOf("SHARED_INFRASTRUCTURE"));
        assertEquals(BusinessDomain.SOCIAL_COMMERCE, BusinessDomain.valueOf("SOCIAL_COMMERCE"));
        assertEquals(BusinessDomain.IDENTITY, BusinessDomain.valueOf("IDENTITY"));
    }

    @ParameterizedTest
    @EnumSource(BusinessDomain.class)
    void businessDomainGetters(BusinessDomain domain) {
        assertNotNull(domain.getDomainCode());
        assertNotNull(domain.getDescription());
    }

    @Test
    void businessDomainIsFinancialDomain() {
        assertTrue(BusinessDomain.SOCIAL_COMMERCE.isFinancialDomain());
        assertTrue(BusinessDomain.MANAGEMENT_SUPPORT.isFinancialDomain());
        assertTrue(BusinessDomain.COURIER_SERVICES.isFinancialDomain());
        assertTrue(BusinessDomain.HAULAGE_LOGISTICS.isFinancialDomain());
        assertFalse(BusinessDomain.IDENTITY.isFinancialDomain());
        assertFalse(BusinessDomain.SHARED_INFRASTRUCTURE.isFinancialDomain());
    }

    @Test
    void businessDomainRequiresEnhancedSecurity() {
        assertTrue(BusinessDomain.SOCIAL_COMMERCE.requiresEnhancedSecurity());
        assertTrue(BusinessDomain.MANAGEMENT_SUPPORT.requiresEnhancedSecurity());
        assertTrue(BusinessDomain.SHARED_INFRASTRUCTURE.requiresEnhancedSecurity());
        assertTrue(BusinessDomain.CENTRAL_CONFIGURATION.requiresEnhancedSecurity());
        assertFalse(BusinessDomain.IDENTITY.requiresEnhancedSecurity());
        assertFalse(BusinessDomain.PAYMENTS.requiresEnhancedSecurity());
    }

    @Test
    void businessDomainGetComplianceRequirements() {
        assertArrayEquals(new ComplianceType[]{ComplianceType.PCI_DSS, ComplianceType.GDPR},
                BusinessDomain.SOCIAL_COMMERCE.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.SOX, ComplianceType.GDPR},
                BusinessDomain.MANAGEMENT_SUPPORT.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.GDPR, ComplianceType.INDUSTRY_SPECIFIC},
                BusinessDomain.WAREHOUSING.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.GDPR, ComplianceType.INDUSTRY_SPECIFIC},
                BusinessDomain.COURIER_SERVICES.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.GDPR, ComplianceType.INDUSTRY_SPECIFIC},
                BusinessDomain.HAULAGE_LOGISTICS.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.GDPR},
                BusinessDomain.IDENTITY.getComplianceRequirements());
        assertArrayEquals(new ComplianceType[]{ComplianceType.GDPR},
                BusinessDomain.PAYMENTS.getComplianceRequirements());
    }

    @Test
    void businessDomainGetDefaultRetentionDays() {
        assertEquals(2555, BusinessDomain.SOCIAL_COMMERCE.getDefaultRetentionDays());
        assertEquals(1095, BusinessDomain.IDENTITY.getDefaultRetentionDays());
    }

    @Test
    void complianceTypeValues() {
        assertEquals(10, ComplianceType.values().length);
    }

    @Test
    void complianceTypeValueOf() {
        assertEquals(ComplianceType.PCI_DSS, ComplianceType.valueOf("PCI_DSS"));
        assertEquals(ComplianceType.GDPR, ComplianceType.valueOf("GDPR"));
        assertEquals(ComplianceType.SOX, ComplianceType.valueOf("SOX"));
        assertEquals(ComplianceType.HIPAA, ComplianceType.valueOf("HIPAA"));
    }

    @ParameterizedTest
    @EnumSource(ComplianceType.class)
    void complianceTypeGetters(ComplianceType type) {
        assertNotNull(type.getCode());
        assertNotNull(type.getFullName());
    }

    @Test
    void complianceTypeMandatory() {
        assertTrue(ComplianceType.PCI_DSS.isMandatory());
        assertTrue(ComplianceType.GDPR.isMandatory());
        assertTrue(ComplianceType.SOX.isMandatory());
        assertFalse(ComplianceType.HIPAA.isMandatory());
        assertFalse(ComplianceType.CCPA.isMandatory());
        assertTrue(ComplianceType.INTERNAL_POLICY.isMandatory());
        assertTrue(ComplianceType.DATA_PROTECTION.isMandatory());
    }

    @Test
    void complianceTypeRequiresRealTimeMonitoring() {
        assertTrue(ComplianceType.PCI_DSS.requiresRealTimeMonitoring());
        assertTrue(ComplianceType.SOX.requiresRealTimeMonitoring());
        assertTrue(ComplianceType.HIPAA.requiresRealTimeMonitoring());
        assertFalse(ComplianceType.GDPR.requiresRealTimeMonitoring());
        assertFalse(ComplianceType.CCPA.requiresRealTimeMonitoring());
    }

    @Test
    void complianceTypeGetMaxRetentionDays() {
        assertEquals(365, ComplianceType.PCI_DSS.getMaxRetentionDays());
        assertEquals(2555, ComplianceType.SOX.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.GDPR.getMaxRetentionDays());
        assertEquals(2190, ComplianceType.HIPAA.getMaxRetentionDays());
        assertEquals(730, ComplianceType.CCPA.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.ISO_27001.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.SOC_2.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.INDUSTRY_SPECIFIC.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.INTERNAL_POLICY.getMaxRetentionDays());
        assertEquals(1095, ComplianceType.DATA_PROTECTION.getMaxRetentionDays());
    }

    @Test
    void complianceTypeGetMinimumDetailLevel() {
        assertEquals(AuditDetailLevel.COMPREHENSIVE, ComplianceType.PCI_DSS.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.COMPREHENSIVE, ComplianceType.SOX.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.COMPREHENSIVE, ComplianceType.HIPAA.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.DETAILED, ComplianceType.GDPR.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.DETAILED, ComplianceType.CCPA.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.STANDARD, ComplianceType.ISO_27001.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.STANDARD, ComplianceType.SOC_2.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.STANDARD, ComplianceType.INDUSTRY_SPECIFIC.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.STANDARD, ComplianceType.INTERNAL_POLICY.getMinimumDetailLevel());
        assertEquals(AuditDetailLevel.STANDARD, ComplianceType.DATA_PROTECTION.getMinimumDetailLevel());
    }

    @Test
    void complianceTypeRequiresLogEncryption() {
        assertTrue(ComplianceType.PCI_DSS.requiresLogEncryption());
        assertTrue(ComplianceType.HIPAA.requiresLogEncryption());
        assertTrue(ComplianceType.SOX.requiresLogEncryption());
        assertFalse(ComplianceType.GDPR.requiresLogEncryption());
        assertFalse(ComplianceType.CCPA.requiresLogEncryption());
        assertFalse(ComplianceType.ISO_27001.requiresLogEncryption());
    }

    @Test
    void complianceTypeGetRequiredTrailCompleteness() {
        assertEquals(1.0, ComplianceType.PCI_DSS.getRequiredTrailCompleteness(), 0.001);
        assertEquals(1.0, ComplianceType.SOX.getRequiredTrailCompleteness(), 0.001);
        assertEquals(1.0, ComplianceType.HIPAA.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.95, ComplianceType.GDPR.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.95, ComplianceType.CCPA.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.90, ComplianceType.ISO_27001.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.90, ComplianceType.SOC_2.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.90, ComplianceType.INDUSTRY_SPECIFIC.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.90, ComplianceType.INTERNAL_POLICY.getRequiredTrailCompleteness(), 0.001);
        assertEquals(0.90, ComplianceType.DATA_PROTECTION.getRequiredTrailCompleteness(), 0.001);
    }

    @Test
    void eventClassificationValues() {
        assertEquals(6, EventClassification.values().length);
    }

    @Test
    void eventClassificationValueOf() {
        assertEquals(EventClassification.SYSTEM_CRITICAL, EventClassification.valueOf("SYSTEM_CRITICAL"));
        assertEquals(EventClassification.HIGH_RISK, EventClassification.valueOf("HIGH_RISK"));
        assertEquals(EventClassification.SECURITY, EventClassification.valueOf("SECURITY"));
        assertEquals(EventClassification.FINANCIAL, EventClassification.valueOf("FINANCIAL"));
        assertEquals(EventClassification.COMPLIANCE, EventClassification.valueOf("COMPLIANCE"));
        assertEquals(EventClassification.STANDARD, EventClassification.valueOf("STANDARD"));
    }

    @ParameterizedTest
    @EnumSource(EventClassification.class)
    void eventClassificationGetters(EventClassification classification) {
        assertNotNull(classification.getDisplayName());
        assertNotNull(classification.getDescription());
    }

    @Test
    void eventClassificationRequiresExecutiveReporting() {
        assertTrue(EventClassification.SYSTEM_CRITICAL.requiresExecutiveReporting());
        assertTrue(EventClassification.HIGH_RISK.requiresExecutiveReporting());
        assertFalse(EventClassification.SECURITY.requiresExecutiveReporting());
        assertFalse(EventClassification.FINANCIAL.requiresExecutiveReporting());
        assertFalse(EventClassification.COMPLIANCE.requiresExecutiveReporting());
        assertFalse(EventClassification.STANDARD.requiresExecutiveReporting());
    }

    @Test
    void eventClassificationRequiresRegulatoryReporting() {
        assertTrue(EventClassification.COMPLIANCE.requiresRegulatoryReporting());
        assertTrue(EventClassification.FINANCIAL.requiresRegulatoryReporting());
        assertFalse(EventClassification.SYSTEM_CRITICAL.requiresRegulatoryReporting());
        assertFalse(EventClassification.HIGH_RISK.requiresRegulatoryReporting());
        assertFalse(EventClassification.SECURITY.requiresRegulatoryReporting());
        assertFalse(EventClassification.STANDARD.requiresRegulatoryReporting());
    }

    @Test
    void riskLevelValues() {
        assertEquals(5, RiskLevel.values().length);
    }

    @Test
    void riskLevelValueOf() {
        assertEquals(RiskLevel.CRITICAL, RiskLevel.valueOf("CRITICAL"));
        assertEquals(RiskLevel.HIGH, RiskLevel.valueOf("HIGH"));
        assertEquals(RiskLevel.MEDIUM, RiskLevel.valueOf("MEDIUM"));
        assertEquals(RiskLevel.LOW, RiskLevel.valueOf("LOW"));
        assertEquals(RiskLevel.NEGLIGIBLE, RiskLevel.valueOf("NEGLIGIBLE"));
    }

    @ParameterizedTest
    @EnumSource(RiskLevel.class)
    void riskLevelGetters(RiskLevel level) {
        assertTrue(level.getLevel() >= 0);
        assertNotNull(level.getDisplayName());
        assertNotNull(level.getDescription());
    }

    @Test
    void riskLevelGetBaseScore() {
        assertEquals(40, RiskLevel.CRITICAL.getBaseScore());
        assertEquals(30, RiskLevel.HIGH.getBaseScore());
        assertEquals(20, RiskLevel.MEDIUM.getBaseScore());
        assertEquals(10, RiskLevel.LOW.getBaseScore());
        assertEquals(0, RiskLevel.NEGLIGIBLE.getBaseScore());
    }

    @Test
    void riskLevelRequiresImmediateAction() {
        assertTrue(RiskLevel.CRITICAL.requiresImmediateAction());
        assertTrue(RiskLevel.HIGH.requiresImmediateAction());
        assertFalse(RiskLevel.MEDIUM.requiresImmediateAction());
        assertFalse(RiskLevel.LOW.requiresImmediateAction());
        assertFalse(RiskLevel.NEGLIGIBLE.requiresImmediateAction());
    }

    @Test
    void riskLevelRequiresCommitteeNotification() {
        assertTrue(RiskLevel.CRITICAL.requiresCommitteeNotification());
        assertTrue(RiskLevel.HIGH.requiresCommitteeNotification());
        assertTrue(RiskLevel.MEDIUM.requiresCommitteeNotification());
        assertFalse(RiskLevel.LOW.requiresCommitteeNotification());
        assertFalse(RiskLevel.NEGLIGIBLE.requiresCommitteeNotification());
    }

    @Test
    void riskLevelRequiresDetailedAuditTrail() {
        assertTrue(RiskLevel.CRITICAL.requiresDetailedAuditTrail());
        assertTrue(RiskLevel.HIGH.requiresDetailedAuditTrail());
        assertFalse(RiskLevel.MEDIUM.requiresDetailedAuditTrail());
        assertFalse(RiskLevel.LOW.requiresDetailedAuditTrail());
        assertFalse(RiskLevel.NEGLIGIBLE.requiresDetailedAuditTrail());
    }

    @Test
    void riskLevelGetReviewPeriodHours() {
        assertEquals(1, RiskLevel.CRITICAL.getReviewPeriodHours());
        assertEquals(4, RiskLevel.HIGH.getReviewPeriodHours());
        assertEquals(24, RiskLevel.MEDIUM.getReviewPeriodHours());
        assertEquals(168, RiskLevel.LOW.getReviewPeriodHours());
        assertEquals(0, RiskLevel.NEGLIGIBLE.getReviewPeriodHours());
    }

    @Test
    void riskLevelRequiresFinancialApproval() {
        assertTrue(RiskLevel.CRITICAL.requiresFinancialApproval());
        assertTrue(RiskLevel.HIGH.requiresFinancialApproval());
        assertFalse(RiskLevel.MEDIUM.requiresFinancialApproval());
        assertFalse(RiskLevel.LOW.requiresFinancialApproval());
        assertFalse(RiskLevel.NEGLIGIBLE.requiresFinancialApproval());
    }

    @Test
    void securityLevelValues() {
        assertEquals(5, SecurityLevel.values().length);
    }

    @Test
    void securityLevelValueOf() {
        assertEquals(SecurityLevel.CRITICAL, SecurityLevel.valueOf("CRITICAL"));
        assertEquals(SecurityLevel.HIGH, SecurityLevel.valueOf("HIGH"));
        assertEquals(SecurityLevel.ELEVATED, SecurityLevel.valueOf("ELEVATED"));
        assertEquals(SecurityLevel.NORMAL, SecurityLevel.valueOf("NORMAL"));
        assertEquals(SecurityLevel.LOW, SecurityLevel.valueOf("LOW"));
    }

    @ParameterizedTest
    @EnumSource(SecurityLevel.class)
    void securityLevelGetters(SecurityLevel level) {
        assertTrue(level.getLevel() >= 0);
        assertNotNull(level.getDisplayName());
        assertNotNull(level.getDescription());
    }

    @Test
    void securityLevelRequiresSecurityTeamAlert() {
        assertTrue(SecurityLevel.CRITICAL.requiresSecurityTeamAlert());
        assertTrue(SecurityLevel.HIGH.requiresSecurityTeamAlert());
        assertFalse(SecurityLevel.ELEVATED.requiresSecurityTeamAlert());
        assertFalse(SecurityLevel.NORMAL.requiresSecurityTeamAlert());
        assertFalse(SecurityLevel.LOW.requiresSecurityTeamAlert());
    }

    @Test
    void securityLevelRequiresEnhancedMonitoring() {
        assertTrue(SecurityLevel.CRITICAL.requiresEnhancedMonitoring());
        assertTrue(SecurityLevel.HIGH.requiresEnhancedMonitoring());
        assertTrue(SecurityLevel.ELEVATED.requiresEnhancedMonitoring());
        assertFalse(SecurityLevel.NORMAL.requiresEnhancedMonitoring());
        assertFalse(SecurityLevel.LOW.requiresEnhancedMonitoring());
    }

    @Test
    void securityLevelRequiresEncryption() {
        assertTrue(SecurityLevel.CRITICAL.requiresEncryption());
        assertTrue(SecurityLevel.HIGH.requiresEncryption());
        assertFalse(SecurityLevel.ELEVATED.requiresEncryption());
        assertFalse(SecurityLevel.NORMAL.requiresEncryption());
        assertFalse(SecurityLevel.LOW.requiresEncryption());
    }

    @Test
    void securityLevelGetEscalationTimeoutMinutes() {
        assertEquals(5, SecurityLevel.CRITICAL.getEscalationTimeoutMinutes());
        assertEquals(30, SecurityLevel.HIGH.getEscalationTimeoutMinutes());
        assertEquals(120, SecurityLevel.ELEVATED.getEscalationTimeoutMinutes());
        assertEquals(0, SecurityLevel.NORMAL.getEscalationTimeoutMinutes());
        assertEquals(0, SecurityLevel.LOW.getEscalationTimeoutMinutes());
    }

    @Test
    void securityThreatLevelValues() {
        assertEquals(5, SecurityThreatLevel.values().length);
    }

    @Test
    void securityThreatLevelValueOf() {
        assertEquals(SecurityThreatLevel.MINIMAL, SecurityThreatLevel.valueOf("MINIMAL"));
        assertEquals(SecurityThreatLevel.LOW, SecurityThreatLevel.valueOf("LOW"));
        assertEquals(SecurityThreatLevel.MODERATE, SecurityThreatLevel.valueOf("MODERATE"));
        assertEquals(SecurityThreatLevel.HIGH, SecurityThreatLevel.valueOf("HIGH"));
        assertEquals(SecurityThreatLevel.CRITICAL, SecurityThreatLevel.valueOf("CRITICAL"));
    }

    @ParameterizedTest
    @EnumSource(SecurityThreatLevel.class)
    void securityThreatLevelGetters(SecurityThreatLevel level) {
        assertNotNull(level.getDisplayName());
        assertNotNull(level.getDescription());
    }

    @Test
    void securityThreatLevelRequiresImmediateEscalation() {
        assertTrue(SecurityThreatLevel.CRITICAL.requiresImmediateEscalation());
        assertTrue(SecurityThreatLevel.HIGH.requiresImmediateEscalation());
        assertFalse(SecurityThreatLevel.MODERATE.requiresImmediateEscalation());
        assertFalse(SecurityThreatLevel.LOW.requiresImmediateEscalation());
        assertFalse(SecurityThreatLevel.MINIMAL.requiresImmediateEscalation());
    }

    @Test
    void securityThreatLevelGetMaxResponseTimeMinutes() {
        assertEquals(5, SecurityThreatLevel.CRITICAL.getMaxResponseTimeMinutes());
        assertEquals(30, SecurityThreatLevel.HIGH.getMaxResponseTimeMinutes());
        assertEquals(240, SecurityThreatLevel.MODERATE.getMaxResponseTimeMinutes());
        assertEquals(1440, SecurityThreatLevel.LOW.getMaxResponseTimeMinutes());
        assertEquals(0, SecurityThreatLevel.MINIMAL.getMaxResponseTimeMinutes());
    }
}