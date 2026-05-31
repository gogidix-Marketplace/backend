package com.gogidix.shared.audit.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelBatchTest {

    @Test
    void auditEventCreationRequestNoArgsConstructor() {
        AuditEventCreationRequest req = new AuditEventCreationRequest();
        assertNotNull(req);
        assertNull(req.getUserId());
    }

    @Test
    void auditEventCreationRequestAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AuditEventCreationRequest req = new AuditEventCreationRequest(
            "user1", "sess1", now, AuditEventType.USER_LOGIN, BusinessDomain.IDENTITY,
            "LOGIN", "/api/auth", "res1", AuditResult.SUCCESS, "desc",
            "192.168.1.1", "agent", "corr1", ComplianceType.GDPR, "LOW", "meta"
        );
        assertEquals("user1", req.getUserId());
        assertEquals("sess1", req.getSessionId());
        assertEquals(now, req.getTimestamp());
        assertEquals(AuditEventType.USER_LOGIN, req.getEventType());
        assertEquals(BusinessDomain.IDENTITY, req.getDomain());
        assertEquals("LOGIN", req.getAction());
        assertEquals("/api/auth", req.getResource());
        assertEquals("res1", req.getResourceId());
        assertEquals(AuditResult.SUCCESS, req.getResult());
        assertEquals("desc", req.getDescription());
        assertEquals("192.168.1.1", req.getIpAddress());
        assertEquals("agent", req.getUserAgent());
        assertEquals("corr1", req.getCorrelationId());
        assertEquals(ComplianceType.GDPR, req.getComplianceType());
        assertEquals("LOW", req.getRiskScore());
        assertEquals("meta", req.getMetadata());
    }

    @Test
    void auditEventCreationRequestBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user2").sessionId("sess2").timestamp(now)
            .eventType(AuditEventType.DATA_ACCESS).domain(BusinessDomain.SECURITY)
            .action("READ").resource("/api/data").resourceId("res2")
            .result(AuditResult.FAILURE).description("desc2")
            .ipAddress("10.0.0.1").userAgent("agent2").correlationId("corr2")
            .complianceType(ComplianceType.PCI_DSS).riskScore("HIGH").metadata("meta2").build();
        assertEquals("user2", req.getUserId());
        assertEquals(AuditEventType.DATA_ACCESS, req.getEventType());
    }

    @Test
    void auditEventCreationRequestSettersAndGetters() {
        AuditEventCreationRequest req = new AuditEventCreationRequest();
        LocalDateTime now = LocalDateTime.now();
        req.setUserId("user3");
        req.setSessionId("sess3");
        req.setTimestamp(now);
        req.setEventType(AuditEventType.SECURITY_EVENT);
        req.setDomain(BusinessDomain.SECURITY);
        req.setAction("WRITE");
        req.setResource("/api/sec");
        req.setResourceId("res3");
        req.setResult(AuditResult.WARNING);
        req.setDescription("desc3");
        req.setIpAddress("172.16.0.1");
        req.setUserAgent("agent3");
        req.setCorrelationId("corr3");
        req.setComplianceType(ComplianceType.SOX);
        req.setRiskScore("MEDIUM");
        req.setMetadata("meta3");
        assertEquals("user3", req.getUserId());
        assertEquals("sess3", req.getSessionId());
        assertEquals(now, req.getTimestamp());
        assertEquals(AuditEventType.SECURITY_EVENT, req.getEventType());
        assertEquals(BusinessDomain.SECURITY, req.getDomain());
        assertEquals("WRITE", req.getAction());
        assertEquals("/api/sec", req.getResource());
        assertEquals("res3", req.getResourceId());
        assertEquals(AuditResult.WARNING, req.getResult());
        assertEquals("desc3", req.getDescription());
        assertEquals("172.16.0.1", req.getIpAddress());
        assertEquals("agent3", req.getUserAgent());
        assertEquals("corr3", req.getCorrelationId());
        assertEquals(ComplianceType.SOX, req.getComplianceType());
        assertEquals("MEDIUM", req.getRiskScore());
        assertEquals("meta3", req.getMetadata());
    }

    @Test
    void auditEventCreationRequestIsValidTrue() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertTrue(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullUserId() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseEmptyUserId() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("  ").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullSessionId() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseEmptySessionId() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullEventType() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").domain(BusinessDomain.IDENTITY)
            .action("READ").resource("/api/data").result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullDomain() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .action("READ").resource("/api/data").result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullAction() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).resource("/api/data").result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseEmptyAction() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("").resource("/api/data")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullResource() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseEmptyResource() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("")
            .result(AuditResult.SUCCESS).build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestIsValidFalseNullResult() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder()
            .userId("user1").sessionId("sess1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).action("READ").resource("/api/data").build();
        assertFalse(req.isValidForBusinessRules());
    }

    @Test
    void auditEventCreationRequestEqualsHashCode() {
        AuditEventCreationRequest r1 = AuditEventCreationRequest.builder().userId("u1").build();
        AuditEventCreationRequest r2 = AuditEventCreationRequest.builder().userId("u1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void auditEventCreationRequestToString() {
        AuditEventCreationRequest req = AuditEventCreationRequest.builder().userId("u1").build();
        assertNotNull(req.toString());
        assertTrue(req.toString().contains("u1"));
    }

    @Test
    void auditResultEnumValues() {
        assertEquals(16, AuditResult.values().length);
        assertEquals("Operation completed successfully", AuditResult.SUCCESS.getDescription());
        assertEquals("Operation failed", AuditResult.FAILURE.getDescription());
        assertEquals("Operation partially completed", AuditResult.PARTIAL_SUCCESS.getDescription());
        assertEquals("Operation completed with warnings", AuditResult.WARNING.getDescription());
        assertEquals("Operation encountered errors", AuditResult.ERROR.getDescription());
        assertEquals("Operation was cancelled", AuditResult.CANCELLED.getDescription());
        assertEquals("Operation timed out", AuditResult.TIMEOUT.getDescription());
        assertEquals("Operation unauthorized", AuditResult.UNAUTHORIZED.getDescription());
        assertEquals("Operation forbidden", AuditResult.FORBIDDEN.getDescription());
        assertEquals("Resource not found", AuditResult.NOT_FOUND.getDescription());
        assertEquals("Operation caused conflict", AuditResult.CONFLICT.getDescription());
        assertEquals("Validation failed", AuditResult.VALIDATION_ERROR.getDescription());
        assertEquals("Business rule violated", AuditResult.BUSINESS_RULE_VIOLATION.getDescription());
        assertEquals("System error occurred", AuditResult.SYSTEM_ERROR.getDescription());
        assertEquals("Operation pending", AuditResult.PENDING.getDescription());
        assertEquals("Operation in progress", AuditResult.IN_PROGRESS.getDescription());
    }

    @Test
    void auditResultIsSuccess() {
        assertTrue(AuditResult.SUCCESS.isSuccess());
        assertTrue(AuditResult.PARTIAL_SUCCESS.isSuccess());
        assertFalse(AuditResult.FAILURE.isSuccess());
        assertFalse(AuditResult.ERROR.isSuccess());
        assertFalse(AuditResult.WARNING.isSuccess());
        assertFalse(AuditResult.CANCELLED.isSuccess());
        assertFalse(AuditResult.TIMEOUT.isSuccess());
        assertFalse(AuditResult.UNAUTHORIZED.isSuccess());
        assertFalse(AuditResult.FORBIDDEN.isSuccess());
        assertFalse(AuditResult.NOT_FOUND.isSuccess());
        assertFalse(AuditResult.CONFLICT.isSuccess());
        assertFalse(AuditResult.VALIDATION_ERROR.isSuccess());
        assertFalse(AuditResult.BUSINESS_RULE_VIOLATION.isSuccess());
        assertFalse(AuditResult.SYSTEM_ERROR.isSuccess());
        assertFalse(AuditResult.PENDING.isSuccess());
        assertFalse(AuditResult.IN_PROGRESS.isSuccess());
    }

    @Test
    void auditResultIsFailure() {
        assertTrue(AuditResult.FAILURE.isFailure());
        assertTrue(AuditResult.ERROR.isFailure());
        assertTrue(AuditResult.TIMEOUT.isFailure());
        assertTrue(AuditResult.SYSTEM_ERROR.isFailure());
        assertTrue(AuditResult.BUSINESS_RULE_VIOLATION.isFailure());
        assertTrue(AuditResult.VALIDATION_ERROR.isFailure());
        assertFalse(AuditResult.SUCCESS.isFailure());
        assertFalse(AuditResult.PARTIAL_SUCCESS.isFailure());
        assertFalse(AuditResult.WARNING.isFailure());
        assertFalse(AuditResult.CANCELLED.isFailure());
        assertFalse(AuditResult.UNAUTHORIZED.isFailure());
        assertFalse(AuditResult.FORBIDDEN.isFailure());
        assertFalse(AuditResult.NOT_FOUND.isFailure());
        assertFalse(AuditResult.CONFLICT.isFailure());
        assertFalse(AuditResult.PENDING.isFailure());
        assertFalse(AuditResult.IN_PROGRESS.isFailure());
    }

    @Test
    void auditResultRequiresAttention() {
        assertTrue(AuditResult.UNAUTHORIZED.requiresAttention());
        assertTrue(AuditResult.FORBIDDEN.requiresAttention());
        assertTrue(AuditResult.SYSTEM_ERROR.requiresAttention());
        assertTrue(AuditResult.BUSINESS_RULE_VIOLATION.requiresAttention());
        assertTrue(AuditResult.CONFLICT.requiresAttention());
        assertFalse(AuditResult.SUCCESS.requiresAttention());
        assertFalse(AuditResult.FAILURE.requiresAttention());
        assertFalse(AuditResult.PARTIAL_SUCCESS.requiresAttention());
        assertFalse(AuditResult.WARNING.requiresAttention());
        assertFalse(AuditResult.ERROR.requiresAttention());
        assertFalse(AuditResult.CANCELLED.requiresAttention());
        assertFalse(AuditResult.TIMEOUT.requiresAttention());
        assertFalse(AuditResult.NOT_FOUND.requiresAttention());
        assertFalse(AuditResult.VALIDATION_ERROR.requiresAttention());
        assertFalse(AuditResult.PENDING.requiresAttention());
        assertFalse(AuditResult.IN_PROGRESS.requiresAttention());
    }

    @Test
    void auditResultGetSeverity() {
        assertEquals(AuditSeverity.CRITICAL, AuditResult.SYSTEM_ERROR.getSeverity());
        assertEquals(AuditSeverity.CRITICAL, AuditResult.UNAUTHORIZED.getSeverity());
        assertEquals(AuditSeverity.CRITICAL, AuditResult.FORBIDDEN.getSeverity());
        assertEquals(AuditSeverity.HIGH, AuditResult.FAILURE.getSeverity());
        assertEquals(AuditSeverity.HIGH, AuditResult.ERROR.getSeverity());
        assertEquals(AuditSeverity.HIGH, AuditResult.TIMEOUT.getSeverity());
        assertEquals(AuditSeverity.HIGH, AuditResult.BUSINESS_RULE_VIOLATION.getSeverity());
        assertEquals(AuditSeverity.MEDIUM, AuditResult.WARNING.getSeverity());
        assertEquals(AuditSeverity.MEDIUM, AuditResult.PARTIAL_SUCCESS.getSeverity());
        assertEquals(AuditSeverity.MEDIUM, AuditResult.VALIDATION_ERROR.getSeverity());
        assertEquals(AuditSeverity.MEDIUM, AuditResult.CONFLICT.getSeverity());
        assertEquals(AuditSeverity.LOW, AuditResult.SUCCESS.getSeverity());
        assertEquals(AuditSeverity.LOW, AuditResult.PENDING.getSeverity());
        assertEquals(AuditSeverity.LOW, AuditResult.IN_PROGRESS.getSeverity());
    }

    @Test
    void auditResultValueOf() {
        assertEquals(AuditResult.SUCCESS, AuditResult.valueOf("SUCCESS"));
        assertEquals(AuditResult.FAILURE, AuditResult.valueOf("FAILURE"));
    }

    @Test
    void auditSearchCriteriaBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .userId("user1").sessionId("sess1").correlationId("corr1").ipAddress("192.168.1.1")
            .eventType(AuditEventType.DATA_ACCESS).domain(BusinessDomain.IDENTITY)
            .result(AuditResult.SUCCESS).complianceType(ComplianceType.GDPR)
            .minSeverity(AuditSeverity.HIGH).startTime(now.minusDays(1)).endTime(now)
            .resourceContains("resource").descriptionContains("desc").metadataContains("meta")
            .userIds(Arrays.asList("u1", "u2")).eventTypes(Arrays.asList(AuditEventType.DATA_ACCESS))
            .domains(Arrays.asList(BusinessDomain.IDENTITY)).results(Arrays.asList(AuditResult.SUCCESS))
            .requiresSecurityEscalation(true).isSuspiciousPattern(false).isCompliantEvent(true)
            .minTransactionAmount(100.0).maxTransactionAmount(1000.0)
            .maxResults(50).sortBy("timestamp").sortDirection("DESC").build();
        assertEquals("user1", criteria.getUserId());
        assertEquals("sess1", criteria.getSessionId());
        assertEquals("corr1", criteria.getCorrelationId());
        assertEquals("192.168.1.1", criteria.getIpAddress());
        assertEquals(AuditEventType.DATA_ACCESS, criteria.getEventType());
        assertEquals(BusinessDomain.IDENTITY, criteria.getDomain());
        assertEquals(AuditResult.SUCCESS, criteria.getResult());
        assertEquals(ComplianceType.GDPR, criteria.getComplianceType());
        assertEquals(AuditSeverity.HIGH, criteria.getMinSeverity());
        assertEquals(now.minusDays(1), criteria.getStartTime());
        assertEquals(now, criteria.getEndTime());
        assertEquals("resource", criteria.getResourceContains());
        assertEquals("desc", criteria.getDescriptionContains());
        assertEquals("meta", criteria.getMetadataContains());
        assertEquals(Arrays.asList("u1", "u2"), criteria.getUserIds());
        assertEquals(true, criteria.getRequiresSecurityEscalation());
        assertEquals(false, criteria.getIsSuspiciousPattern());
        assertEquals(true, criteria.getIsCompliantEvent());
        assertEquals(100.0, criteria.getMinTransactionAmount());
        assertEquals(1000.0, criteria.getMaxTransactionAmount());
        assertEquals(50, criteria.getMaxResults());
        assertEquals("timestamp", criteria.getSortBy());
        assertEquals("DESC", criteria.getSortDirection());
    }

    @Test
    void auditSearchCriteriaSettersAndGetters() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();
        criteria.setUserId("user2");
        criteria.setSessionId("sess2");
        criteria.setCorrelationId("corr2");
        criteria.setIpAddress("10.0.0.1");
        assertEquals("user2", criteria.getUserId());
        assertEquals("sess2", criteria.getSessionId());
        assertEquals("corr2", criteria.getCorrelationId());
        assertEquals("10.0.0.1", criteria.getIpAddress());
    }

    @Test
    void auditSearchCriteriaIsValidTrue() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .startTime(LocalDateTime.now()).endTime(LocalDateTime.now().plusDays(1))
            .minTransactionAmount(100.0).maxTransactionAmount(500.0).maxResults(10).build();
        assertTrue(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsValidTrueNullAll() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();
        assertTrue(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsValidFalseStartAfterEnd() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .startTime(LocalDateTime.now()).endTime(LocalDateTime.now().minusDays(1)).build();
        assertFalse(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsValidFalseMinGtMax() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .minTransactionAmount(500.0).maxTransactionAmount(100.0).build();
        assertFalse(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsValidFalseMaxResultsZero() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().maxResults(0).build();
        assertFalse(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsValidFalseMaxResultsNegative() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().maxResults(-1).build();
        assertFalse(criteria.isValid());
    }

    @Test
    void auditSearchCriteriaIsComplexSearchTrue() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .userId("u1").eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY).result(AuditResult.SUCCESS).build();
        assertTrue(criteria.isComplexSearch());
    }

    @Test
    void auditSearchCriteriaIsComplexSearchFalse() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder()
            .userId("u1").eventType(AuditEventType.DATA_ACCESS).build();
        assertFalse(criteria.isComplexSearch());
    }

    @Test
    void auditSearchCriteriaIsComplexSearchWithTimeRange() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("u1")
            .eventType(AuditEventType.DATA_ACCESS).domain(BusinessDomain.IDENTITY)
            .startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).build();
        assertTrue(criteria.isComplexSearch());
    }

    @Test
    void auditSearchCriteriaGetCacheKeyAllFields() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("u1")
            .eventType(AuditEventType.DATA_ACCESS).domain(BusinessDomain.IDENTITY)
            .startTime(LocalDateTime.now()).endTime(LocalDateTime.now()).build();
        String key = criteria.getCacheKey();
        assertTrue(key.contains("u:u1"));
        assertTrue(key.contains("et:"));
        assertTrue(key.contains("d:"));
        assertTrue(key.contains("st:"));
    }

    @Test
    void auditSearchCriteriaGetCacheKeyEmpty() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().build();
        assertEquals("", criteria.getCacheKey());
    }

    @Test
    void auditSearchCriteriaGetCacheKeyPartial() {
        AuditSearchCriteria criteria = AuditSearchCriteria.builder().userId("u1").build();
        assertTrue(criteria.getCacheKey().contains("u:u1"));
        assertFalse(criteria.getCacheKey().contains("et:"));
    }

    @Test
    void auditSearchCriteriaEqualsHashCode() {
        AuditSearchCriteria c1 = AuditSearchCriteria.builder().userId("u1").build();
        AuditSearchCriteria c2 = AuditSearchCriteria.builder().userId("u1").build();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void auditSearchCriteriaToString() {
        AuditSearchCriteria c = AuditSearchCriteria.builder().userId("u1").build();
        assertNotNull(c.toString());
    }

    @Test
    void auditStatisticsBuilderAndGetters() {
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now();
        Map<AuditEventType, Long> etDist = Map.of(AuditEventType.DATA_ACCESS, 50L);
        Map<BusinessDomain, Long> dDist = Map.of(BusinessDomain.IDENTITY, 30L);
        Map<AuditResult, Long> rDist = Map.of(AuditResult.SUCCESS, 80L);
        Map<ComplianceType, Long> cDist = Map.of(ComplianceType.GDPR, 20L);
        AuditStatistics stats = AuditStatistics.builder()
            .periodStart(start).periodEnd(end).totalEvents(100L).successfulEvents(80L)
            .failedEvents(15L).securityEvents(5L).complianceEvents(20L)
            .eventTypeDistribution(etDist).domainDistribution(dDist).resultDistribution(rDist)
            .complianceDistribution(cDist).uniqueUsers(10L).uniqueSessions(15L)
            .mostActiveUser("user1").mostActiveUserEvents(25L).averageEventsPerUser(10.0)
            .averageEventsPerHour(4.17).compliancePercentage(95.0).securityEventPercentage(5.0)
            .criticalSeverityEvents(1).highSeverityEvents(3).mediumSeverityEvents(10)
            .lowSeverityEvents(86).suspiciousPatterns(0L).escalationRequiredEvents(1L)
            .financialTransactions(30L).totalFinancialAmount(50000.0).build();
        assertEquals(start, stats.getPeriodStart());
        assertEquals(end, stats.getPeriodEnd());
        assertEquals(100L, stats.getTotalEvents());
        assertEquals(80L, stats.getSuccessfulEvents());
        assertEquals(15L, stats.getFailedEvents());
        assertEquals(5L, stats.getSecurityEvents());
        assertEquals(20L, stats.getComplianceEvents());
        assertEquals(etDist, stats.getEventTypeDistribution());
        assertEquals(dDist, stats.getDomainDistribution());
        assertEquals(rDist, stats.getResultDistribution());
        assertEquals(cDist, stats.getComplianceDistribution());
        assertEquals(10L, stats.getUniqueUsers());
        assertEquals(15L, stats.getUniqueSessions());
        assertEquals("user1", stats.getMostActiveUser());
        assertEquals(25L, stats.getMostActiveUserEvents());
        assertEquals(10.0, stats.getAverageEventsPerUser());
        assertEquals(4.17, stats.getAverageEventsPerHour());
        assertEquals(95.0, stats.getCompliancePercentage());
        assertEquals(5.0, stats.getSecurityEventPercentage());
        assertEquals(1, stats.getCriticalSeverityEvents());
        assertEquals(3, stats.getHighSeverityEvents());
        assertEquals(10, stats.getMediumSeverityEvents());
        assertEquals(86, stats.getLowSeverityEvents());
        assertEquals(0L, stats.getSuspiciousPatterns());
        assertEquals(1L, stats.getEscalationRequiredEvents());
        assertEquals(30L, stats.getFinancialTransactions());
        assertEquals(50000.0, stats.getTotalFinancialAmount());
    }

    @Test
    void auditStatisticsGetSuccessRate() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(80L).build();
        assertEquals(80.0, stats.getSuccessRate(), 0.001);
    }

    @Test
    void auditStatisticsGetSuccessRateZero() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(0L).successfulEvents(0L).build();
        assertEquals(0.0, stats.getSuccessRate(), 0.001);
    }

    @Test
    void auditStatisticsGetFailureRate() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).failedEvents(20L).build();
        assertEquals(20.0, stats.getFailureRate(), 0.001);
    }

    @Test
    void auditStatisticsGetFailureRateZero() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(0L).failedEvents(0L).build();
        assertEquals(0.0, stats.getFailureRate(), 0.001);
    }

    @Test
    void auditStatisticsIsHealthyOperationTrue() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .compliancePercentage(99.0).complianceEvents(50L).build();
        assertTrue(stats.isHealthyOperation());
    }

    @Test
    void auditStatisticsIsHealthyOperationFalseLowSuccess() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(90L)
            .failedEvents(10L).securityEventPercentage(3.0).suspiciousPatterns(0L).build();
        assertFalse(stats.isHealthyOperation());
    }

    @Test
    void auditStatisticsIsHealthyOperationFalseHighSecurity() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(6.0).suspiciousPatterns(0L).build();
        assertFalse(stats.isHealthyOperation());
    }

    @Test
    void auditStatisticsIsHealthyOperationFalseHighSuspicious() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(2L).build();
        assertFalse(stats.isHealthyOperation());
    }

    @Test
    void auditStatisticsGetRiskScoreLow() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(0, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreHighFailure() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(85L)
            .failedEvents(15L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(30, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreMedFailure() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(92L)
            .failedEvents(8L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(15, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreHighSecurity() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(12.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(25, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreMedSecurity() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(7.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(10, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreHighSuspicious() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(6L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(20, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreMedSuspicious() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(2L)
            .criticalSeverityEvents(0).escalationRequiredEvents(0L).build();
        assertEquals(10, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreCriticalEvents() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(1).escalationRequiredEvents(0L).build();
        assertEquals(15, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreEscalation() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(98L)
            .failedEvents(2L).securityEventPercentage(3.0).suspiciousPatterns(0L)
            .criticalSeverityEvents(0).escalationRequiredEvents(3L).build();
        assertEquals(10, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetRiskScoreCapped100() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).successfulEvents(80L)
            .failedEvents(20L).securityEventPercentage(15.0).suspiciousPatterns(10L)
            .criticalSeverityEvents(5).escalationRequiredEvents(10L).build();
        assertEquals(100, stats.getRiskScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreFull() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(50L)
            .compliancePercentage(99.0).securityEventPercentage(3.0).build();
        assertEquals(100, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreLowCompliance() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(50L)
            .compliancePercentage(90.0).securityEventPercentage(3.0).build();
        assertEquals(80, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreMedCompliance() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(50L)
            .compliancePercentage(96.0).securityEventPercentage(3.0).build();
        assertEquals(90, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreNoComplianceEvents() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(0L)
            .compliancePercentage(99.0).securityEventPercentage(3.0).build();
        assertEquals(70, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreHighSecurity() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(50L)
            .compliancePercentage(99.0).securityEventPercentage(7.0).build();
        assertEquals(85, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsGetComplianceHealthScoreMinZero() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).complianceEvents(0L)
            .compliancePercentage(80.0).securityEventPercentage(10.0).build();
        assertEquals(35, stats.getComplianceHealthScore());
    }

    @Test
    void auditStatisticsEqualsHashCode() {
        AuditStatistics s1 = AuditStatistics.builder().totalEvents(100L).build();
        AuditStatistics s2 = AuditStatistics.builder().totalEvents(100L).build();
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void auditStatisticsToString() {
        AuditStatistics stats = AuditStatistics.builder().totalEvents(100L).build();
        assertNotNull(stats.toString());
    }

    @Test
    void auditTrailEntryBuilder() {
        LocalDateTime now = LocalDateTime.now();
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1").timestamp(now)
            .userId("user1").action("READ").resource("/api/data").result("SUCCESS")
            .details("Read data").correlationId("corr1").build();
        assertEquals("evt-1", entry.getEventId());
        assertEquals(now, entry.getTimestamp());
        assertEquals("user1", entry.getUserId());
        assertEquals("READ", entry.getAction());
        assertEquals("/api/data", entry.getResource());
        assertEquals("SUCCESS", entry.getResult());
        assertEquals("Read data", entry.getDetails());
        assertEquals("corr1", entry.getCorrelationId());
    }

    @Test
    void auditTrailEntryAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        AuditTrailEntry entry = new AuditTrailEntry("evt-2", now, "user2", "WRITE",
            "/api/data", "FAILURE", "Write failed", "corr2");
        assertEquals("evt-2", entry.getEventId());
        assertEquals(now, entry.getTimestamp());
        assertEquals("user2", entry.getUserId());
        assertEquals("WRITE", entry.getAction());
        assertEquals("/api/data", entry.getResource());
        assertEquals("FAILURE", entry.getResult());
        assertEquals("Write failed", entry.getDetails());
        assertEquals("corr2", entry.getCorrelationId());
    }

    @Test
    void auditTrailEntryIsCompleteTrue() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("READ")
            .resource("/api/data").result("SUCCESS").build();
        assertTrue(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullEventId() {
        AuditTrailEntry entry = AuditTrailEntry.builder().timestamp(LocalDateTime.now())
            .userId("user1").action("READ").resource("/api/data").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseEmptyEventId() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("").timestamp(LocalDateTime.now())
            .userId("user1").action("READ").resource("/api/data").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullTimestamp() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1").userId("user1")
            .action("READ").resource("/api/data").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullUserId() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).action("READ").resource("/api/data")
            .result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseEmptyUserId() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("").action("READ")
            .resource("/api/data").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullAction() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").resource("/api/data")
            .result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseEmptyAction() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("")
            .resource("/api/data").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullResource() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("READ")
            .result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseEmptyResource() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("READ")
            .resource("").result("SUCCESS").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseNullResult() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("READ")
            .resource("/api/data").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryIsCompleteFalseEmptyResult() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("evt-1")
            .timestamp(LocalDateTime.now()).userId("user1").action("READ")
            .resource("/api/data").result("").build();
        assertFalse(entry.isComplete());
    }

    @Test
    void auditTrailEntryToLogFormat() {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        AuditTrailEntry entry = AuditTrailEntry.builder().timestamp(ts).userId("user1")
            .action("READ").resource("/api/data").result("SUCCESS").build();
        String log = entry.toLogFormat();
        assertNotNull(log);
        assertTrue(log.contains("user1"));
        assertTrue(log.contains("READ"));
        assertTrue(log.contains("/api/data"));
        assertTrue(log.contains("SUCCESS"));
    }

    @Test
    void auditTrailEntryGetDeduplicationHash() {
        LocalDateTime ts = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
        AuditTrailEntry entry = AuditTrailEntry.builder().timestamp(ts).userId("user1")
            .action("READ").resource("/api/data").build();
        String hash = entry.getDeduplicationHash();
        assertNotNull(hash);
        assertTrue(hash.contains("user1"));
        assertTrue(hash.contains("READ"));
        assertTrue(hash.contains("/api/data"));
    }

    @Test
    void auditTrailEntryEqualsHashCode() {
        AuditTrailEntry e1 = AuditTrailEntry.builder().eventId("e1").build();
        AuditTrailEntry e2 = AuditTrailEntry.builder().eventId("e1").build();
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void auditTrailEntryToString() {
        AuditTrailEntry entry = AuditTrailEntry.builder().eventId("e1").build();
        assertNotNull(entry.toString());
    }

    @Test
    void complianceRequirementNoArgsConstructor() {
        ComplianceRequirement req = new ComplianceRequirement();
        assertNotNull(req);
        assertNull(req.getRequirementId());
    }

    @Test
    void complianceRequirementAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> controls = Arrays.asList("c1", "c2");
        ComplianceRequirement req = new ComplianceRequirement(
            "req1", "name1", ComplianceType.GDPR, "desc", "cat", "HIGH",
            now.minusDays(1), now.plusDays(1), controls, true, "criteria"
        );
        assertEquals("req1", req.getRequirementId());
        assertEquals("name1", req.getRequirementName());
        assertEquals(ComplianceType.GDPR, req.getComplianceType());
        assertEquals("desc", req.getDescription());
        assertEquals("cat", req.getCategory());
        assertEquals("HIGH", req.getPriority());
        assertEquals(now.minusDays(1), req.getEffectiveDate());
        assertEquals(now.plusDays(1), req.getExpiryDate());
        assertEquals(controls, req.getControls());
        assertEquals(true, req.getIsMandatory());
        assertEquals("criteria", req.getValidationCriteria());
    }

    @Test
    void complianceRequirementBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ComplianceRequirement req = ComplianceRequirement.builder().requirementId("req2")
            .requirementName("name2").complianceType(ComplianceType.PCI_DSS).description("desc2")
            .category("cat2").priority("MEDIUM").effectiveDate(now.minusDays(1))
            .expiryDate(now.plusDays(1)).controls(Collections.emptyList())
            .isMandatory(false).validationCriteria("crit2").build();
        assertEquals("req2", req.getRequirementId());
        assertEquals(ComplianceType.PCI_DSS, req.getComplianceType());
    }

    @Test
    void complianceRequirementSettersAndGetters() {
        ComplianceRequirement req = new ComplianceRequirement();
        req.setRequirementId("req3");
        req.setRequirementName("name3");
        req.setComplianceType(ComplianceType.SOX);
        req.setDescription("desc3");
        req.setCategory("cat3");
        req.setPriority("LOW");
        req.setEffectiveDate(LocalDateTime.now().minusDays(1));
        req.setExpiryDate(LocalDateTime.now().plusDays(1));
        req.setControls(Arrays.asList("c1"));
        req.setIsMandatory(true);
        req.setValidationCriteria("crit3");
        assertEquals("req3", req.getRequirementId());
        assertEquals("name3", req.getRequirementName());
        assertEquals(ComplianceType.SOX, req.getComplianceType());
        assertEquals("desc3", req.getDescription());
        assertEquals("cat3", req.getCategory());
        assertEquals("LOW", req.getPriority());
        assertEquals(Arrays.asList("c1"), req.getControls());
        assertEquals(true, req.getIsMandatory());
        assertEquals("crit3", req.getValidationCriteria());
    }

    @Test
    void complianceRequirementIsActiveTrue() {
        ComplianceRequirement req = ComplianceRequirement.builder()
            .effectiveDate(LocalDateTime.now().minusDays(1))
            .expiryDate(LocalDateTime.now().plusDays(1)).build();
        assertTrue(req.isActive());
    }

    @Test
    void complianceRequirementIsActiveTrueNullDates() {
        ComplianceRequirement req = new ComplianceRequirement();
        assertTrue(req.isActive());
    }

    @Test
    void complianceRequirementIsActiveTrueNullEffective() {
        ComplianceRequirement req = ComplianceRequirement.builder()
            .expiryDate(LocalDateTime.now().plusDays(1)).build();
        assertTrue(req.isActive());
    }

    @Test
    void complianceRequirementIsActiveTrueNullExpiry() {
        ComplianceRequirement req = ComplianceRequirement.builder()
            .effectiveDate(LocalDateTime.now().minusDays(1)).build();
        assertTrue(req.isActive());
    }

    @Test
    void complianceRequirementIsActiveFalseExpired() {
        ComplianceRequirement req = ComplianceRequirement.builder()
            .effectiveDate(LocalDateTime.now().minusDays(10))
            .expiryDate(LocalDateTime.now().minusDays(1)).build();
        assertFalse(req.isActive());
    }

    @Test
    void complianceRequirementIsActiveFalseNotYetEffective() {
        ComplianceRequirement req = ComplianceRequirement.builder()
            .effectiveDate(LocalDateTime.now().plusDays(10))
            .expiryDate(LocalDateTime.now().plusDays(20)).build();
        assertFalse(req.isActive());
    }

    @Test
    void complianceRequirementEqualsHashCode() {
        ComplianceRequirement r1 = ComplianceRequirement.builder().requirementId("r1").build();
        ComplianceRequirement r2 = ComplianceRequirement.builder().requirementId("r1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void complianceRequirementToString() {
        ComplianceRequirement req = ComplianceRequirement.builder().requirementId("r1").build();
        assertNotNull(req.toString());
    }

    @Test
    void complianceValidationResultNoArgsConstructor() {
        ComplianceValidationResult result = new ComplianceValidationResult();
        assertNotNull(result);
        assertNull(result.getValidationId());
    }

    @Test
    void complianceValidationResultAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        List<String> violations = Arrays.asList("v1");
        List<String> warnings = Arrays.asList("w1");
        ComplianceValidationResult result = new ComplianceValidationResult(
            "val1", now, ComplianceType.GDPR, true, "PASSED",
            violations, warnings, "actions", "auditor1"
        );
        assertEquals("val1", result.getValidationId());
        assertEquals(now, result.getValidatedAt());
        assertEquals(ComplianceType.GDPR, result.getComplianceType());
        assertEquals(true, result.getIsValid());
        assertEquals("PASSED", result.getValidationStatus());
        assertEquals(violations, result.getViolations());
        assertEquals(warnings, result.getWarnings());
        assertEquals("actions", result.getRemedialActions());
        assertEquals("auditor1", result.getValidatedBy());
    }

    @Test
    void complianceValidationResultBuilder() {
        LocalDateTime now = LocalDateTime.now();
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .validationId("val2").validatedAt(now).complianceType(ComplianceType.PCI_DSS)
            .isValid(false).validationStatus("FAILED").violations(Arrays.asList("v1"))
            .warnings(Collections.emptyList()).remedialActions("fix").validatedBy("auditor2").build();
        assertEquals("val2", result.getValidationId());
        assertEquals(ComplianceType.PCI_DSS, result.getComplianceType());
    }

    @Test
    void complianceValidationResultSettersAndGetters() {
        ComplianceValidationResult result = new ComplianceValidationResult();
        LocalDateTime now = LocalDateTime.now();
        result.setValidationId("val3");
        result.setValidatedAt(now);
        result.setComplianceType(ComplianceType.SOX);
        result.setIsValid(true);
        result.setValidationStatus("PASSED");
        result.setViolations(Collections.emptyList());
        result.setWarnings(Arrays.asList("w1"));
        result.setRemedialActions("none");
        result.setValidatedBy("auditor3");
        assertEquals("val3", result.getValidationId());
        assertEquals(now, result.getValidatedAt());
        assertEquals(ComplianceType.SOX, result.getComplianceType());
        assertEquals(true, result.getIsValid());
        assertEquals("PASSED", result.getValidationStatus());
        assertEquals(Collections.emptyList(), result.getViolations());
        assertEquals(Arrays.asList("w1"), result.getWarnings());
        assertEquals("none", result.getRemedialActions());
        assertEquals("auditor3", result.getValidatedBy());
    }

    @Test
    void complianceValidationResultIsPassedTrue() {
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .isValid(true).violations(null).build();
        assertTrue(result.isPassed());
    }

    @Test
    void complianceValidationResultIsPassedTrueEmptyViolations() {
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .isValid(true).violations(Collections.emptyList()).build();
        assertTrue(result.isPassed());
    }

    @Test
    void complianceValidationResultIsPassedFalseNullValid() {
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .violations(Collections.emptyList()).build();
        assertFalse(result.isPassed());
    }

    @Test
    void complianceValidationResultIsPassedFalseNotValid() {
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .isValid(false).violations(Collections.emptyList()).build();
        assertFalse(result.isPassed());
    }

    @Test
    void complianceValidationResultIsPassedFalseWithViolations() {
        ComplianceValidationResult result = ComplianceValidationResult.builder()
            .isValid(true).violations(Arrays.asList("v1")).build();
        assertFalse(result.isPassed());
    }

    @Test
    void complianceValidationResultEqualsHashCode() {
        ComplianceValidationResult r1 = ComplianceValidationResult.builder().validationId("v1").build();
        ComplianceValidationResult r2 = ComplianceValidationResult.builder().validationId("v1").build();
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void complianceValidationResultToString() {
        ComplianceValidationResult result = ComplianceValidationResult.builder().validationId("v1").build();
        assertNotNull(result.toString());
    }
}
