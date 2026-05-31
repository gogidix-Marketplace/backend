package com.gogidix.shared.audit.application;

import com.gogidix.shared.audit.domain.*;
import com.gogidix.shared.audit.domain.port.AuditEventPublisher;
import com.gogidix.shared.audit.domain.port.AuditEventRepository;
import com.gogidix.shared.audit.domain.port.ComplianceReporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SharedAuditServiceTest {

    @Mock
    private AuditEventRepository auditEventRepository;

    @Mock
    private AuditEventPublisher auditEventPublisher;

    @Mock
    private ComplianceReporter complianceReporter;

    @InjectMocks
    private SharedAuditService sharedAuditService;

    private AuditEventCreationRequest buildValidRequest() {
        return AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("CREATE")
                .resource("/api/test")
                .resourceId("res-1")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .ipAddress("127.0.0.1")
                .userAgent("test-agent")
                .correlationId("corr-1")
                .description("test event")
                .metadata("meta")
                .build();
    }

    private AuditStatistics buildTestStatistics() {
        return AuditStatistics.builder()
                .periodStart(LocalDateTime.now().minusHours(1))
                .periodEnd(LocalDateTime.now())
                .totalEvents(100)
                .successfulEvents(95)
                .failedEvents(5)
                .securityEvents(2)
                .complianceEvents(10)
                .uniqueUsers(20)
                .uniqueSessions(30)
                .mostActiveUser("user1")
                .mostActiveUserEvents(15)
                .averageEventsPerUser(5.0)
                .averageEventsPerHour(10.0)
                .compliancePercentage(95.0)
                .securityEventPercentage(2.0)
                .criticalSeverityEvents(0)
                .highSeverityEvents(1)
                .mediumSeverityEvents(3)
                .lowSeverityEvents(96)
                .suspiciousPatterns(0)
                .escalationRequiredEvents(0)
                .financialTransactions(5)
                .totalFinancialAmount(10000.0)
                .build();
    }

    @Test
    void recordAuditEvent_validRequest_returnsSavedEvent() {
        AuditEventCreationRequest request = buildValidRequest();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        assertEquals("user1", result.getUserId());
        assertEquals("CREATE", result.getAction());
        assertEquals("/api/test", result.getResource());
        assertNotNull(result.getEventId());
        verify(auditEventRepository).save(any(AuditEvent.class));
    }

    @Test
    void recordAuditEvent_securityEvent_triggersEscalation() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("INTRUSION")
                .resource("/api/admin")
                .resourceId("res-1")
                .result(AuditResult.FAILURE)
                .eventType(AuditEventType.SECURITY_EVENT)
                .domain(BusinessDomain.SECURITY)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        verify(auditEventPublisher).publishSecurityEscalation(any(AuditEvent.class));
        verify(auditEventPublisher).notifySecurityTeam(any(AuditEvent.class), eq("Security escalation required"));
    }

    @Test
    void recordAuditEvent_financialEvent_triggersFinancialAlert() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("PROCESS_PAYMENT")
                .resource("/api/payments")
                .resourceId("res-1")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.FINANCIAL_TRANSACTION)
                .domain(BusinessDomain.PAYMENTS)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        verify(auditEventPublisher).publishFinancialTransactionAlert(any(AuditEvent.class));
        assertEquals(ComplianceType.PCI_DSS, result.getComplianceType());
    }

    @Test
    void recordAuditEvent_sensitiveDataEvent_setsGdprCompliance() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("ACCESS_PII")
                .resource("/api/users/123")
                .resourceId("res-1")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SENSITIVE_DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        assertEquals(ComplianceType.GDPR, result.getComplianceType());
    }

    @Test
    void recordAuditEvent_financialDomain_setsSoxCompliance() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("UPDATE_LEDGER")
                .resource("/api/ledger")
                .resourceId("res-1")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.DATA_MODIFICATION)
                .domain(BusinessDomain.SOCIAL_COMMERCE)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        assertEquals(ComplianceType.SOX, result.getComplianceType());
    }

    @Test
    void recordAuditEvent_defaultCompliance_setsInternalPolicy() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("READ")
                .resource("/api/config")
                .resourceId("res-1")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.SHARED_LIBRARIES)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        assertEquals(ComplianceType.INTERNAL_POLICY, result.getComplianceType());
    }

    @Test
    void recordAuditEvent_securityMonitoringType_calculatesRiskScore() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("BRUTE_FORCE")
                .resource("/api/auth")
                .resourceId("res-1")
                .result(AuditResult.FAILURE)
                .eventType(AuditEventType.SECURITY_EVENT)
                .domain(BusinessDomain.SECURITY)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        int score = Integer.parseInt(result.getRiskScore());
        assertTrue(score > 0);
    }

    @Test
    void recordAuditEvent_failureResult_addsRiskScore() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .sessionId("session1")
                .action("CREATE")
                .resource("/api/test")
                .resourceId("res-1")
                .result(AuditResult.FAILURE)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);
        AuditEvent result = future.join();

        assertNotNull(result);
        int score = Integer.parseInt(result.getRiskScore());
        assertTrue(score >= 20);
    }

    @Test
    void recordAuditEvent_missingUserId_returnsFailedFuture() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("")
                .action("CREATE")
                .resource("/api/test")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .build();

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void recordAuditEvent_missingAction_returnsFailedFuture() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .action("")
                .resource("/api/test")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .build();

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void recordAuditEvent_missingResource_returnsFailedFuture() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .action("CREATE")
                .resource("")
                .result(AuditResult.SUCCESS)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .build();

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void recordAuditEvent_missingResult_returnsFailedFuture() {
        AuditEventCreationRequest request = AuditEventCreationRequest.builder()
                .userId("user1")
                .action("CREATE")
                .resource("/api/test")
                .result(null)
                .eventType(AuditEventType.SYSTEM_EVENT)
                .domain(BusinessDomain.SHARED_INFRASTRUCTURE)
                .build();

        CompletableFuture<AuditEvent> future = sharedAuditService.recordAuditEvent(request);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void recordBatchAuditEvents_success() {
        AuditEventCreationRequest request = buildValidRequest();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<List<AuditEvent>> future =
                sharedAuditService.recordBatchAuditEvents(List.of(request));
        List<AuditEvent> results = future.join();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("user1", results.get(0).getUserId());
    }

    @Test
    void recordBatchAuditEvents_multipleRequests() {
        AuditEventCreationRequest req1 = buildValidRequest();
        AuditEventCreationRequest req2 = AuditEventCreationRequest.builder()
                .userId("user2").sessionId("s2").action("UPDATE")
                .resource("/api/test2").result(AuditResult.SUCCESS)
                .eventType(AuditEventType.DATA_MODIFICATION)
                .domain(BusinessDomain.IDENTITY).build();

        when(auditEventRepository.save(any(AuditEvent.class))).thenAnswer(inv -> inv.getArgument(0));

        CompletableFuture<List<AuditEvent>> future =
                sharedAuditService.recordBatchAuditEvents(List.of(req1, req2));
        List<AuditEvent> results = future.join();

        assertEquals(2, results.size());
    }

    @Test
    void recordBatchAuditEvents_exception_returnsFailedFuture() {
        AuditEventCreationRequest request = buildValidRequest();

        when(auditEventRepository.save(any(AuditEvent.class)))
                .thenThrow(new RuntimeException("DB error"));

        CompletableFuture<List<AuditEvent>> future =
                sharedAuditService.recordBatchAuditEvents(List.of(request));

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void searchAuditEvents_validCriteria_returnsResults() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
                .userId("user1")
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now())
                .build();

        AuditEvent event = AuditEvent.builder().eventId("evt-1").userId("user1").build();
        when(auditEventRepository.findBySearchCriteria(criteria)).thenReturn(List.of(event));

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.searchAuditEvents(criteria);
        List<AuditEvent> results = future.join();

        assertEquals(1, results.size());
        assertEquals("evt-1", results.get(0).getEventId());
    }

    @Test
    void searchAuditEvents_invalidCriteria_returnsFailedFuture() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now().minusDays(1))
                .build();

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.searchAuditEvents(criteria);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void searchAuditEvents_exception_returnsFailedFuture() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
                .userId("user1")
                .startTime(LocalDateTime.now().minusDays(1))
                .endTime(LocalDateTime.now())
                .build();

        when(auditEventRepository.findBySearchCriteria(criteria))
                .thenThrow(new RuntimeException("search error"));

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.searchAuditEvents(criteria);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void generateAuditStatistics_success() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        AuditStatistics stats = buildTestStatistics();

        when(auditEventRepository.getAuditStatistics(startTime, endTime)).thenReturn(stats);

        CompletableFuture<AuditStatistics> future =
                sharedAuditService.generateAuditStatistics(startTime, endTime);
        AuditStatistics result = future.join();

        assertNotNull(result);
        assertEquals(100, result.getTotalEvents());
        verify(auditEventPublisher).publishAuditStatistics(stats);
    }

    @Test
    void generateAuditStatistics_exception_returnsFailedFuture() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();

        when(auditEventRepository.getAuditStatistics(startTime, endTime))
                .thenThrow(new RuntimeException("stats error"));

        CompletableFuture<AuditStatistics> future =
                sharedAuditService.generateAuditStatistics(startTime, endTime);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void generateComplianceReport_success() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();
        ComplianceReport report = ComplianceReport.builder()
                .reportId("rpt-1").generatedAt(LocalDateTime.now())
                .complianceType(ComplianceType.PCI_DSS)
                .eventId("evt-1").timestamp(LocalDateTime.now())
                .auditTrail("trail").riskAssessment("LOW")
                .regulatoryContext("PCI DSS").build();

        when(complianceReporter.generateComplianceReport(ComplianceType.PCI_DSS, startTime, endTime))
                .thenReturn(CompletableFuture.completedFuture(report));

        CompletableFuture<ComplianceReport> future =
                sharedAuditService.generateComplianceReport(ComplianceType.PCI_DSS, startTime, endTime);
        ComplianceReport result = future.join();

        assertNotNull(result);
        assertEquals("rpt-1", result.getReportId());
        verify(auditEventPublisher).publishComplianceReport(report);
    }

    @Test
    void generateComplianceReport_exception_throwsRuntimeException() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(30);
        LocalDateTime endTime = LocalDateTime.now();

        when(complianceReporter.generateComplianceReport(ComplianceType.GDPR, startTime, endTime))
                .thenReturn(CompletableFuture.failedFuture(new RuntimeException("report error")));

        CompletableFuture<ComplianceReport> future =
                sharedAuditService.generateComplianceReport(ComplianceType.GDPR, startTime, endTime);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void detectSuspiciousPatterns_withNonCriticalEvents() {
        AuditEvent event = AuditEvent.builder()
                .eventId("evt-1").userId("user1").action("READ")
                .eventType(AuditEventType.DATA_ACCESS)
                .domain(BusinessDomain.IDENTITY)
                .result(AuditResult.SUCCESS).timestamp(LocalDateTime.now())
                .resource("/api/data").build();

        when(auditEventRepository.findSuspiciousPatterns()).thenReturn(List.of(event));

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.detectSuspiciousPatterns();
        List<AuditEvent> results = future.join();

        assertEquals(1, results.size());
        verify(auditEventPublisher).publishSuspiciousPatternAlert(event);
        verify(auditEventPublisher, never()).publishSecurityEscalation(any());
    }

    @Test
    void detectSuspiciousPatterns_withCriticalEvents() {
        AuditEvent criticalEvent = AuditEvent.builder()
                .eventId("evt-crit").userId("user1").action("INTRUSION")
                .eventType(AuditEventType.SECURITY_EVENT)
                .domain(BusinessDomain.SECURITY)
                .result(AuditResult.FAILURE).timestamp(LocalDateTime.now())
                .resource("/api/admin")
                .description("Security breach detected").build();

        when(auditEventRepository.findSuspiciousPatterns()).thenReturn(List.of(criticalEvent));

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.detectSuspiciousPatterns();
        List<AuditEvent> results = future.join();

        assertEquals(1, results.size());
        verify(auditEventPublisher).publishSuspiciousPatternAlert(criticalEvent);
        verify(auditEventPublisher).publishSecurityEscalation(criticalEvent);
    }

    @Test
    void detectSuspiciousPatterns_emptyList() {
        when(auditEventRepository.findSuspiciousPatterns()).thenReturn(Collections.emptyList());

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.detectSuspiciousPatterns();
        List<AuditEvent> results = future.join();

        assertTrue(results.isEmpty());
        verify(auditEventPublisher, never()).publishSuspiciousPatternAlert(any());
    }

    @Test
    void detectSuspiciousPatterns_exception_returnsFailedFuture() {
        when(auditEventRepository.findSuspiciousPatterns())
                .thenThrow(new RuntimeException("detection error"));

        CompletableFuture<List<AuditEvent>> future = sharedAuditService.detectSuspiciousPatterns();

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void validateAuditTrailCompleteness_complete() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(auditEventRepository.isAuditTrailComplete(startTime, endTime)).thenReturn(true);

        CompletableFuture<Boolean> future =
                sharedAuditService.validateAuditTrailCompleteness(startTime, endTime);
        Boolean result = future.join();

        assertTrue(result);
    }

    @Test
    void validateAuditTrailCompleteness_incomplete() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(auditEventRepository.isAuditTrailComplete(startTime, endTime)).thenReturn(false);

        CompletableFuture<Boolean> future =
                sharedAuditService.validateAuditTrailCompleteness(startTime, endTime);
        Boolean result = future.join();

        assertFalse(result);
    }

    @Test
    void validateAuditTrailCompleteness_exception_returnsFailedFuture() {
        LocalDateTime startTime = LocalDateTime.now().minusDays(7);
        LocalDateTime endTime = LocalDateTime.now();

        when(auditEventRepository.isAuditTrailComplete(startTime, endTime))
                .thenThrow(new RuntimeException("validation error"));

        CompletableFuture<Boolean> future =
                sharedAuditService.validateAuditTrailCompleteness(startTime, endTime);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void performAuditDataRetention_withEvents() {
        AuditEvent oldEvent = AuditEvent.builder().eventId("evt-old").userId("user1").build();
        when(auditEventRepository.findByTimestampBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(oldEvent));

        CompletableFuture<Long> future = sharedAuditService.performAuditDataRetention();
        Long deletedCount = future.join();

        assertEquals(1L, deletedCount);
        verify(auditEventPublisher).archiveAuditEvent(oldEvent);
        verify(auditEventRepository).deleteByTimestampBefore(any(LocalDateTime.class));
    }

    @Test
    void performAuditDataRetention_noEvents() {
        when(auditEventRepository.findByTimestampBefore(any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        CompletableFuture<Long> future = sharedAuditService.performAuditDataRetention();
        Long deletedCount = future.join();

        assertEquals(0L, deletedCount);
        verify(auditEventRepository).deleteByTimestampBefore(any(LocalDateTime.class));
    }

    @Test
    void performAuditDataRetention_multipleEvents() {
        AuditEvent evt1 = AuditEvent.builder().eventId("evt-1").build();
        AuditEvent evt2 = AuditEvent.builder().eventId("evt-2").build();
        AuditEvent evt3 = AuditEvent.builder().eventId("evt-3").build();
        when(auditEventRepository.findByTimestampBefore(any(LocalDateTime.class)))
                .thenReturn(List.of(evt1, evt2, evt3));

        CompletableFuture<Long> future = sharedAuditService.performAuditDataRetention();
        Long deletedCount = future.join();

        assertEquals(3L, deletedCount);
        verify(auditEventPublisher, times(3)).archiveAuditEvent(any(AuditEvent.class));
    }

    @Test
    void performAuditDataRetention_exception_returnsFailedFuture() {
        when(auditEventRepository.findByTimestampBefore(any(LocalDateTime.class)))
                .thenThrow(new RuntimeException("retention error"));

        CompletableFuture<Long> future = sharedAuditService.performAuditDataRetention();

        assertTrue(future.isCompletedExceptionally());
    }
}