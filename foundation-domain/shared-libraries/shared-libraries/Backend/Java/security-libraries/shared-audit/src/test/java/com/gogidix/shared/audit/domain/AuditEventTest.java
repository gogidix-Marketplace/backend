package com.gogidix.shared.audit.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for AuditEvent domain entity
 */
@DisplayName("AuditEvent Tests")
class AuditEventTest {

    @Test
    @DisplayName("Test builder creates valid audit event")
    void testBuilderCreatesValidAuditEvent() {
        LocalDateTime now = LocalDateTime.now();

        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(now)
                .eventType(AuditEventType.LOGIN_ATTEMPT)
                .domain(BusinessDomain.IDENTITY)
                .action("LOGIN")
                .resource("/api/auth/login")
                .result(AuditResult.SUCCESS)
                .ipAddress("192.168.1.1")
                .userAgent("Mozilla/5.0")
                .correlationId("corr-123")
                .build();

        assertEquals("evt-123", event.getEventId());
        assertEquals("user-456", event.getUserId());
        assertEquals("session-789", event.getSessionId());
        assertEquals(now, event.getTimestamp());
        assertEquals(AuditEventType.LOGIN_ATTEMPT, event.getEventType());
        assertEquals(BusinessDomain.IDENTITY, event.getDomain());
        assertEquals("LOGIN", event.getAction());
        assertEquals("/api/auth/login", event.getResource());
        assertEquals(AuditResult.SUCCESS, event.getResult());
        assertEquals("192.168.1.1", event.getIpAddress());
        assertEquals("Mozilla/5.0", event.getUserAgent());
        assertEquals("corr-123", event.getCorrelationId());
    }

    @Test
    @DisplayName("Test isCompliantEvent with all required fields")
    void testIsCompliantEventWithAllRequiredFields() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .domain(BusinessDomain.PAYMENTS)
                .action("PROCESS_PAYMENT")
                .resource("/api/payments")
                .result(AuditResult.SUCCESS)
                .description("Payment processed successfully")
                .complianceType(ComplianceType.PCI_DSS)
                .build();

        assertTrue(event.isCompliantEvent());
    }

    @Test
    @DisplayName("Test isCompliantEvent returns false without compliance type")
    void testIsCompliantEventWithoutComplianceType() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("User data accessed")
                .build();

        assertFalse(event.isCompliantEvent());
    }

    @Test
    @DisplayName("Test isCompliantEvent returns false with empty description")
    void testIsCompliantEventWithEmptyDescription() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("")
                .complianceType(ComplianceType.GDPR)
                .build();

        assertFalse(event.isCompliantEvent());
    }

    @Test
    @DisplayName("Test requiresSecurityEscalation for security event")
    void testRequiresSecurityEscalationForSecurityEvent() {
        AuditEvent securityEvent = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.SECURITY_EVENT)
                .domain(BusinessDomain.SECURITY)
                .action("INTRUSION_DETECTED")
                .resource("/api/admin")
                .result(AuditResult.FAILURE)
                .description("Security breach detected")
                .build();

        assertTrue(securityEvent.requiresSecurityEscalation());
    }

    @Test
    @DisplayName("Test requiresSecurityEscalation for multiple access denied failures")
    void testRequiresSecurityEscalationForMultipleFailures() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.ACCESS_DENIED)
                .domain(BusinessDomain.IDENTITY)
                .action("LOGIN")
                .resource("/api/auth/login")
                .result(AuditResult.FAILURE)
                .description("Login failed")
                .metadata("failureCount:5,attempts:5")
                .build();

        assertTrue(event.requiresSecurityEscalation());
    }

    @Test
    @DisplayName("Test calculateSeverity for security event")
    void testCalculateSeverityForSecurityEvent() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.SECURITY_EVENT)
                .domain(BusinessDomain.SECURITY)
                .action("BRUTE_FORCE")
                .resource("/api/auth")
                .result(AuditResult.FAILURE)
                .description("Brute force attack")
                .build();

        assertEquals("CRITICAL", event.calculateSeverity());
    }

    @Test
    @DisplayName("Test calculateSeverity for high value financial transaction")
    void testCalculateSeverityForHighValueTransaction() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .domain(BusinessDomain.PAYMENTS)
                .action("PROCESS_PAYMENT")
                .resource("/api/payments")
                .result(AuditResult.SUCCESS)
                .description("Large payment processed")
                .metadata("amount:15000.00,currency:USD")
                .build();

        assertEquals("HIGH", event.calculateSeverity());
    }

    @Test
    @DisplayName("Test calculateSeverity for failed event")
    void testCalculateSeverityForFailedEvent() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_MODIFICATION)
                .domain(BusinessDomain.IDENTITY)
                .action("UPDATE")
                .resource("/api/users/123")
                .result(AuditResult.FAILURE)
                .description("Update failed")
                .build();

        assertEquals("MEDIUM", event.calculateSeverity());
    }

    @Test
    @DisplayName("Test calculateSeverity for successful event")
    void testCalculateSeverityForSuccessfulEvent() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("User data accessed")
                .build();

        assertEquals("LOW", event.calculateSeverity());
    }

    @Test
    @DisplayName("Test isSuspiciousPattern for failed login")
    void testIsSuspiciousPatternForFailedLogin() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.LOGIN_ATTEMPT)
                .domain(BusinessDomain.IDENTITY)
                .action("LOGIN")
                .resource("/api/auth/login")
                .result(AuditResult.FAILURE)
                .description("Login failed")
                .build();

        assertTrue(event.isSuspiciousPattern());
    }

    @Test
    @DisplayName("Test isSuspiciousPattern for off-hours financial transaction")
    void testIsSuspiciousPatternForOffHoursTransaction() {
        LocalDateTime offHours = LocalDateTime.now().withHour(3).withMinute(0);

        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(offHours)
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .domain(BusinessDomain.PAYMENTS)
                .action("PROCESS_PAYMENT")
                .resource("/api/payments")
                .result(AuditResult.SUCCESS)
                .description("Payment at 3 AM")
                .build();

        assertTrue(event.isSuspiciousPattern());
    }

    @Test
    @DisplayName("Test isSuspiciousPattern for high frequency requests")
    void testIsSuspiciousPatternForHighFrequency() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("High frequency access")
                .metadata("frequency:150,requestsPerMinute:150")
                .build();

        assertTrue(event.isSuspiciousPattern());
    }

    @Test
    @DisplayName("Test generateComplianceReport")
    void testGenerateComplianceReport() {
        LocalDateTime now = LocalDateTime.now();

        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(now)
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("User data accessed")
                .complianceType(ComplianceType.GDPR)
                .riskScore("LOW")
                .build();

        ComplianceReport report = event.generateComplianceReport();

        assertEquals("evt-123", report.getEventId());
        assertEquals(now, report.getTimestamp());
        assertEquals(ComplianceType.GDPR, report.getComplianceType());
        assertEquals("User data accessed", report.getAuditTrail());
        assertEquals("LOW", report.getRiskAssessment());
        assertEquals("General Data Protection Regulation", report.getRegulatoryContext());
    }

    @DisplayName("Test generateComplianceReport for different compliance types")
    @ParameterizedTest
    @EnumSource(ComplianceType.class)
    void testGenerateComplianceReportForDifferentTypes(ComplianceType complianceType) {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("Data accessed")
                .complianceType(complianceType)
                .riskScore("MEDIUM")
                .build();

        ComplianceReport report = event.generateComplianceReport();

        assertEquals(complianceType, report.getComplianceType());
        assertNotNull(report.getRegulatoryContext());
    }

    @Test
    @DisplayName("Test createTrailEntry")
    void testCreateTrailEntry() {
        LocalDateTime now = LocalDateTime.now();

        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(now)
                .eventType(AuditEventType.DATA_MODIFICATION)
                .domain(BusinessDomain.IDENTITY)
                .action("UPDATE")
                .resource("/api/users/123")
                .result(AuditResult.SUCCESS)
                .description("User profile updated")
                .build();

        AuditTrailEntry entry = event.createTrailEntry();

        assertEquals("evt-123", entry.getEventId());
        assertEquals(now, entry.getTimestamp());
        assertEquals("user-456", entry.getUserId());
        assertEquals("UPDATE", entry.getAction());
        assertEquals("/api/users/123", entry.getResource());
        assertEquals("SUCCESS", entry.getResult());
        assertEquals("User profile updated", entry.getDetails());
    }

    @Test
    @DisplayName("Test isFinancialEvent for financial transaction type")
    void testIsFinancialEventForFinancialTransaction() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .domain(BusinessDomain.PAYMENTS)
                .action("PAYMENT")
                .resource("/api/payments")
                .result(AuditResult.SUCCESS)
                .description("Payment processed")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent for payment processing type")
    void testIsFinancialEventForPaymentProcessing() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.PAYMENT_PROCESSING)
                .domain(BusinessDomain.PAYMENTS)
                .action("PROCESS")
                .resource("/api/payments")
                .result(AuditResult.SUCCESS)
                .description("Payment processing")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent for refund processed type")
    void testIsFinancialEventForRefundProcessed() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.REFUND_PROCESSED)
                .domain(BusinessDomain.PAYMENTS)
                .action("REFUND")
                .resource("/api/refunds")
                .result(AuditResult.SUCCESS)
                .description("Refund processed")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent for commission calculation type")
    void testIsFinancialEventForCommissionCalculation() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.COMMISSION_CALCULATION)
                .domain(BusinessDomain.COMMISSIONS)
                .action("CALCULATE")
                .resource("/api/commissions")
                .result(AuditResult.SUCCESS)
                .description("Commission calculated")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent for payment in action")
    void testIsFinancialEventForPaymentInAction() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.API_CALL)
                .domain(BusinessDomain.PAYMENTS)
                .action("processPayment")
                .resource("/api/orders")
                .result(AuditResult.SUCCESS)
                .description("Payment action")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent for billing resource")
    void testIsFinancialEventForBillingResource() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.API_CALL)
                .domain(BusinessDomain.BILLING)
                .action("READ")
                .resource("/api/billing/invoices")
                .result(AuditResult.SUCCESS)
                .description("Billing info accessed")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent with amount in metadata")
    void testIsFinancialEventWithAmountInMetadata() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.API_CALL)
                .domain(BusinessDomain.ORDERS)
                .action("CREATE")
                .resource("/api/orders")
                .result(AuditResult.SUCCESS)
                .description("Order created")
                .metadata("amount:100.00,currency:USD")
                .build();

        assertTrue(event.isFinancialEvent());
    }

    @Test
    @DisplayName("Test isFinancialEvent returns false for non-financial event")
    void testIsFinancialEventReturnsFalse() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-123")
                .userId("user-456")
                .sessionId("session-789")
                .timestamp(LocalDateTime.now())
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .action("READ")
                .resource("/api/users")
                .result(AuditResult.SUCCESS)
                .description("User data accessed")
                .build();

        assertFalse(event.isFinancialEvent());
    }

    @Test
    @DisplayName(" Test no-args constructor")
    void testNoArgsConstructor() {
        AuditEvent event = new AuditEvent();

        assertNull(event.getEventId());
        assertNull(event.getUserId());
        assertNull(event.getSessionId());
        assertNull(event.getTimestamp());
        assertNull(event.getEventType());
        assertNull(event.getDomain());
        assertNull(event.getAction());
        assertNull(event.getResource());
        assertNull(event.getResult());
    }

    @Test
    @DisplayName("Test all-args constructor")
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        AuditEvent event = new AuditEvent(
            1L,
            "evt-123",
            "user-456",
            "session-789",
            now,
            AuditEventType.DATA_ACCESS,
            BusinessDomain.IDENTITY,
            "READ",
            "/api/users",
            "resource-123",
            AuditResult.SUCCESS,
            "Description",
            "192.168.1.1",
            "Mozilla",
            "corr-123",
            ComplianceType.GDPR,
            "LOW",
            "metadata"
        );

        assertEquals(1L, event.getId());
        assertEquals("evt-123", event.getEventId());
        assertEquals("user-456", event.getUserId());
    }

    @Test
    @DisplayName("Test setters and getters")
    void testSettersAndGetters() {
        AuditEvent event = new AuditEvent();
        LocalDateTime now = LocalDateTime.now();

        event.setId(1L);
        event.setEventId("evt-123");
        event.setUserId("user-456");
        event.setSessionId("session-789");
        event.setTimestamp(now);
        event.setEventType(AuditEventType.DATA_ACCESS);
        event.setDomain(BusinessDomain.IDENTITY);
        event.setAction("READ");
        event.setResource("/api/users");
        event.setResourceId("resource-123");
        event.setResult(AuditResult.SUCCESS);
        event.setDescription("Description");
        event.setIpAddress("192.168.1.1");
        event.setUserAgent("Mozilla");
        event.setCorrelationId("corr-123");
        event.setComplianceType(ComplianceType.GDPR);
        event.setRiskScore("LOW");
        event.setMetadata("metadata");

        assertEquals(1L, event.getId());
        assertEquals("evt-123", event.getEventId());
        assertEquals("user-456", event.getUserId());
    }
}
