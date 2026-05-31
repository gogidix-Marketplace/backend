package com.gogidix.shared.audit.api.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Batch Tests")
class DtoBatchTest {

    @Nested
    @DisplayName("AuditEventDTO Tests")
    class AuditEventDTOTests {

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            AuditEventDTO dto = new AuditEventDTO();
            assertNotNull(dto);
            assertNull(dto.getEventId());
            assertNull(dto.getUserId());
            assertNull(dto.getSessionId());
            assertNull(dto.getTimestamp());
            assertNull(dto.getAction());
            assertNull(dto.getResourceType());
            assertNull(dto.getResourceId());
            assertNull(dto.getIpAddress());
            assertNull(dto.getUserAgent());
            assertNull(dto.getAdditionalData());
            assertNull(dto.getSeverity());
            assertNull(dto.getRequiresSecurityEscalation());
            assertNull(dto.getSuspiciousPattern());
            assertNull(dto.getCompliantEvent());
            assertNull(dto.getFinancialEvent());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            Map<String, String> data = new HashMap<>();
            data.put("key", "value");
            AuditEventDTO dto = new AuditEventDTO(
                "evt-1", "user-1", "session-1", now,
                "LOGIN", "USER", "res-1", "192.168.1.1",
                "Mozilla", data, "HIGH", true, false, true, false
            );
            assertEquals("evt-1", dto.getEventId());
            assertEquals("user-1", dto.getUserId());
            assertEquals("session-1", dto.getSessionId());
            assertEquals(now, dto.getTimestamp());
            assertEquals("LOGIN", dto.getAction());
            assertEquals("USER", dto.getResourceType());
            assertEquals("res-1", dto.getResourceId());
            assertEquals("192.168.1.1", dto.getIpAddress());
            assertEquals("Mozilla", dto.getUserAgent());
            assertEquals(data, dto.getAdditionalData());
            assertEquals("HIGH", dto.getSeverity());
            assertTrue(dto.getRequiresSecurityEscalation());
            assertFalse(dto.getSuspiciousPattern());
            assertTrue(dto.getCompliantEvent());
            assertFalse(dto.getFinancialEvent());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();
            Map<String, String> data = new HashMap<>();
            data.put("k", "v");
            AuditEventDTO dto = AuditEventDTO.builder()
                .eventId("evt-2").userId("user-2").sessionId("sess-2")
                .timestamp(now).action("LOGOUT").resourceType("ORDER")
                .resourceId("res-2").ipAddress("10.0.0.1").userAgent("Chrome")
                .additionalData(data).severity("LOW")
                .requiresSecurityEscalation(false).suspiciousPattern(true)
                .compliantEvent(false).financialEvent(true).build();
            assertEquals("evt-2", dto.getEventId());
            assertEquals("user-2", dto.getUserId());
            assertEquals("sess-2", dto.getSessionId());
            assertEquals(now, dto.getTimestamp());
            assertEquals("LOGOUT", dto.getAction());
            assertEquals("ORDER", dto.getResourceType());
            assertEquals("res-2", dto.getResourceId());
            assertEquals("10.0.0.1", dto.getIpAddress());
            assertEquals("Chrome", dto.getUserAgent());
            assertEquals(data, dto.getAdditionalData());
            assertEquals("LOW", dto.getSeverity());
            assertFalse(dto.getRequiresSecurityEscalation());
            assertTrue(dto.getSuspiciousPattern());
            assertFalse(dto.getCompliantEvent());
            assertTrue(dto.getFinancialEvent());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            AuditEventDTO dto = new AuditEventDTO();
            LocalDateTime now = LocalDateTime.now();
            Map<String, String> data = new HashMap<>();
            dto.setEventId("evt-3");
            dto.setUserId("user-3");
            dto.setSessionId("sess-3");
            dto.setTimestamp(now);
            dto.setAction("CREATE");
            dto.setResourceType("PAYMENT");
            dto.setResourceId("res-3");
            dto.setIpAddress("172.16.0.1");
            dto.setUserAgent("Safari");
            dto.setAdditionalData(data);
            dto.setSeverity("MEDIUM");
            dto.setRequiresSecurityEscalation(true);
            dto.setSuspiciousPattern(false);
            dto.setCompliantEvent(true);
            dto.setFinancialEvent(false);
            assertEquals("evt-3", dto.getEventId());
            assertEquals("user-3", dto.getUserId());
            assertEquals("sess-3", dto.getSessionId());
            assertEquals(now, dto.getTimestamp());
            assertEquals("CREATE", dto.getAction());
            assertEquals("PAYMENT", dto.getResourceType());
            assertEquals("res-3", dto.getResourceId());
            assertEquals("172.16.0.1", dto.getIpAddress());
            assertEquals("Safari", dto.getUserAgent());
            assertEquals(data, dto.getAdditionalData());
            assertEquals("MEDIUM", dto.getSeverity());
            assertTrue(dto.getRequiresSecurityEscalation());
            assertFalse(dto.getSuspiciousPattern());
            assertTrue(dto.getCompliantEvent());
            assertFalse(dto.getFinancialEvent());
        }

        @Test
        @DisplayName("test equals and hashCode")
        void testEqualsAndHashCode() {
            AuditEventDTO dto1 = new AuditEventDTO();
            AuditEventDTO dto2 = new AuditEventDTO();
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
            dto1.setEventId("evt-1");
            assertNotEquals(dto1, dto2);
            dto2.setEventId("evt-1");
            assertEquals(dto1, dto2);
        }

        @Test
        @DisplayName("test toString")
        void testToString() {
            AuditEventDTO dto = AuditEventDTO.builder().eventId("evt-1").userId("user-1").build();
            String str = dto.toString();
            assertNotNull(str);
            assertTrue(str.contains("evt-1"));
            assertTrue(str.contains("user-1"));
        }
    }

    @Nested
    @DisplayName("AuditSearchDTO Tests")
    class AuditSearchDTOTests {

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            AuditSearchDTO dto = new AuditSearchDTO();
            assertNotNull(dto);
            assertNull(dto.getUserId());
            assertNull(dto.getAction());
            assertNull(dto.getResourceType());
            assertNull(dto.getResourceId());
            assertNull(dto.getStartDate());
            assertNull(dto.getEndDate());
            assertNull(dto.getLimit());
            assertNull(dto.getOffset());
            assertNull(dto.getSortBy());
            assertNull(dto.getSortDirection());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            AuditSearchDTO dto = new AuditSearchDTO(
                "user-1", "LOGIN", "USER", "res-1",
                now.minusDays(1), now, 50, 0, "timestamp", "DESC"
            );
            assertEquals("user-1", dto.getUserId());
            assertEquals("LOGIN", dto.getAction());
            assertEquals("USER", dto.getResourceType());
            assertEquals("res-1", dto.getResourceId());
            assertEquals(now.minusDays(1), dto.getStartDate());
            assertEquals(now, dto.getEndDate());
            assertEquals(50, dto.getLimit());
            assertEquals(0, dto.getOffset());
            assertEquals("timestamp", dto.getSortBy());
            assertEquals("DESC", dto.getSortDirection());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();
            AuditSearchDTO dto = AuditSearchDTO.builder()
                .userId("user-2").action("READ").resourceType("ORDER")
                .resourceId("res-2").startDate(now).endDate(now.plusDays(1))
                .limit(100).offset(10).sortBy("action").sortDirection("ASC")
                .build();
            assertEquals("user-2", dto.getUserId());
            assertEquals("READ", dto.getAction());
            assertEquals(100, dto.getLimit());
            assertEquals(10, dto.getOffset());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            AuditSearchDTO dto = new AuditSearchDTO();
            LocalDateTime now = LocalDateTime.now();
            dto.setUserId("user-3");
            dto.setAction("WRITE");
            dto.setResourceType("PAYMENT");
            dto.setResourceId("res-3");
            dto.setStartDate(now);
            dto.setEndDate(now.plusHours(1));
            dto.setLimit(25);
            dto.setOffset(5);
            dto.setSortBy("resourceType");
            dto.setSortDirection("ASC");
            assertEquals("user-3", dto.getUserId());
            assertEquals("WRITE", dto.getAction());
            assertEquals("PAYMENT", dto.getResourceType());
            assertEquals("res-3", dto.getResourceId());
            assertEquals(now, dto.getStartDate());
            assertEquals(now.plusHours(1), dto.getEndDate());
            assertEquals(25, dto.getLimit());
            assertEquals(5, dto.getOffset());
            assertEquals("resourceType", dto.getSortBy());
            assertEquals("ASC", dto.getSortDirection());
        }

        @Test
        @DisplayName("test getEffectiveMaxResults with limit set")
        void testGetEffectiveMaxResultsWithLimit() {
            AuditSearchDTO dto = new AuditSearchDTO();
            dto.setLimit(50);
            assertEquals(50, dto.getEffectiveMaxResults());
        }

        @Test
        @DisplayName("test getEffectiveMaxResults default")
        void testGetEffectiveMaxResultsDefault() {
            AuditSearchDTO dto = new AuditSearchDTO();
            assertEquals(100, dto.getEffectiveMaxResults());
        }

        @Test
        @DisplayName("test getEffectiveSortBy with value")
        void testGetEffectiveSortByWithValue() {
            AuditSearchDTO dto = new AuditSearchDTO();
            dto.setSortBy("action");
            assertEquals("action", dto.getEffectiveSortBy());
        }

        @Test
        @DisplayName("test getEffectiveSortBy default")
        void testGetEffectiveSortByDefault() {
            AuditSearchDTO dto = new AuditSearchDTO();
            assertEquals("timestamp", dto.getEffectiveSortBy());
        }

        @Test
        @DisplayName("test getEffectiveSortDirection with value")
        void testGetEffectiveSortDirectionWithValue() {
            AuditSearchDTO dto = new AuditSearchDTO();
            dto.setSortDirection("ASC");
            assertEquals("ASC", dto.getEffectiveSortDirection());
        }

        @Test
        @DisplayName("test getEffectiveSortDirection default")
        void testGetEffectiveSortDirectionDefault() {
            AuditSearchDTO dto = new AuditSearchDTO();
            assertEquals("DESC", dto.getEffectiveSortDirection());
        }

        @Test
        @DisplayName("test equals and hashCode")
        void testEqualsAndHashCode() {
            AuditSearchDTO dto1 = new AuditSearchDTO();
            AuditSearchDTO dto2 = new AuditSearchDTO();
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
            dto1.setUserId("user-1");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("test toString")
        void testToString() {
            AuditSearchDTO dto = AuditSearchDTO.builder().userId("user-1").build();
            String str = dto.toString();
            assertNotNull(str);
            assertTrue(str.contains("user-1"));
        }
    }

    @Nested
    @DisplayName("AuditStatisticsDTO Tests")
    class AuditStatisticsDTOTests {

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            AuditStatisticsDTO dto = new AuditStatisticsDTO();
            assertNotNull(dto);
            assertEquals(0L, dto.getTotalEvents());
            assertNull(dto.getGeneratedAt());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            AuditStatisticsDTO dto = new AuditStatisticsDTO(500L, now);
            assertEquals(500L, dto.getTotalEvents());
            assertEquals(now, dto.getGeneratedAt());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();
            AuditStatisticsDTO dto = AuditStatisticsDTO.builder()
                .totalEvents(1000L).generatedAt(now).build();
            assertEquals(1000L, dto.getTotalEvents());
            assertEquals(now, dto.getGeneratedAt());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            AuditStatisticsDTO dto = new AuditStatisticsDTO();
            LocalDateTime now = LocalDateTime.now();
            dto.setTotalEvents(2000L);
            dto.setGeneratedAt(now);
            assertEquals(2000L, dto.getTotalEvents());
            assertEquals(now, dto.getGeneratedAt());
        }

        @Test
        @DisplayName("test equals hashCode and toString")
        void testEqualsHashCodeAndToString() {
            AuditStatisticsDTO dto1 = new AuditStatisticsDTO();
            AuditStatisticsDTO dto2 = new AuditStatisticsDTO();
            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
            dto1.setTotalEvents(100L);
            assertNotEquals(dto1, dto2);
            String str = dto1.toString();
            assertNotNull(str);
            assertTrue(str.contains("100"));
        }
    }

    @Nested
    @DisplayName("CreateAuditEventDTO Tests")
    class CreateAuditEventDTOTests {

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            CreateAuditEventDTO dto = new CreateAuditEventDTO();
            assertNotNull(dto);
            assertNull(dto.getUserId());
            assertNull(dto.getSessionId());
            assertNull(dto.getAction());
            assertNull(dto.getResourceType());
            assertNull(dto.getResourceId());
            assertNull(dto.getIpAddress());
            assertNull(dto.getUserAgent());
            assertNull(dto.getAdditionalData());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            Map<String, String> data = new HashMap<>();
            CreateAuditEventDTO dto = new CreateAuditEventDTO(
                "user-1", "sess-1", "LOGIN", "USER", "res-1",
                "192.168.1.1", "Mozilla", data
            );
            assertEquals("user-1", dto.getUserId());
            assertEquals("sess-1", dto.getSessionId());
            assertEquals("LOGIN", dto.getAction());
            assertEquals("USER", dto.getResourceType());
            assertEquals("res-1", dto.getResourceId());
            assertEquals("192.168.1.1", dto.getIpAddress());
            assertEquals("Mozilla", dto.getUserAgent());
            assertEquals(data, dto.getAdditionalData());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            Map<String, String> data = new HashMap<>();
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-2").sessionId("sess-2").action("LOGOUT")
                .resourceType("ORDER").resourceId("res-2")
                .ipAddress("10.0.0.1").userAgent("Chrome").additionalData(data)
                .build();
            assertEquals("user-2", dto.getUserId());
            assertEquals("sess-2", dto.getSessionId());
            assertEquals("LOGOUT", dto.getAction());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            CreateAuditEventDTO dto = new CreateAuditEventDTO();
            Map<String, String> data = new HashMap<>();
            dto.setUserId("user-3");
            dto.setSessionId("sess-3");
            dto.setAction("CREATE");
            dto.setResourceType("PAYMENT");
            dto.setResourceId("res-3");
            dto.setIpAddress("172.16.0.1");
            dto.setUserAgent("Safari");
            dto.setAdditionalData(data);
            assertEquals("user-3", dto.getUserId());
            assertEquals("sess-3", dto.getSessionId());
            assertEquals("CREATE", dto.getAction());
            assertEquals("PAYMENT", dto.getResourceType());
            assertEquals("res-3", dto.getResourceId());
            assertEquals("172.16.0.1", dto.getIpAddress());
            assertEquals("Safari", dto.getUserAgent());
            assertEquals(data, dto.getAdditionalData());
        }

        @Test
        @DisplayName("test isValidForBusinessRules true")
        void testIsValidForBusinessRulesTrue() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-1").sessionId("sess-1").action("LOGIN").build();
            assertTrue(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules userId null")
        void testIsValidUserIdNull() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .sessionId("sess-1").action("LOGIN").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules userId empty")
        void testIsValidUserIdEmpty() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("").sessionId("sess-1").action("LOGIN").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules userId blank")
        void testIsValidUserIdBlank() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("   ").sessionId("sess-1").action("LOGIN").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules sessionId null")
        void testIsValidSessionIdNull() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-1").action("LOGIN").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules sessionId empty")
        void testIsValidSessionIdEmpty() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-1").sessionId("").action("LOGIN").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules action null")
        void testIsValidActionNull() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-1").sessionId("sess-1").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test isValidForBusinessRules action empty")
        void testIsValidActionEmpty() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder()
                .userId("user-1").sessionId("sess-1").action("").build();
            assertFalse(dto.isValidForBusinessRules());
        }

        @Test
        @DisplayName("test equals and hashCode")
        void testEqualsAndHashCode() {
            CreateAuditEventDTO dto1 = new CreateAuditEventDTO();
            CreateAuditEventDTO dto2 = new CreateAuditEventDTO();
            assertEquals(dto1, dto2);
            dto1.setUserId("user-1");
            assertNotEquals(dto1, dto2);
        }

        @Test
        @DisplayName("test toString")
        void testToString() {
            CreateAuditEventDTO dto = CreateAuditEventDTO.builder().userId("user-1").build();
            String str = dto.toString();
            assertNotNull(str);
            assertTrue(str.contains("user-1"));
        }
    }

    @Nested
    @DisplayName("AuditTrailCompletenessDTO Tests")
    class AuditTrailCompletenessDTOTests {

        private AuditTrailCompletenessDTO buildFullDTO() {
            return AuditTrailCompletenessDTO.builder()
                .periodStart(LocalDateTime.now().minusDays(1))
                .periodEnd(LocalDateTime.now())
                .validatedAt(LocalDateTime.now())
                .validatedBy("admin")
                .isComplete(true)
                .completenessPercentage(95.0)
                .completenessStatus("COMPLETE")
                .totalEventsExpected(1000L)
                .totalEventsFound(990L)
                .missingEventCount(10L)
                .duplicateEventCount(2L)
                .corruptedEventCount(0L)
                .detectedGaps(new ArrayList<>())
                .missingEventTypes(new ArrayList<>())
                .affectedDomains(new ArrayList<>())
                .affectedUsers(new ArrayList<>())
                .integrityVerified(true)
                .integrityCheckCount(1000L)
                .integrityFailureCount(0L)
                .integrityIssues(new ArrayList<>())
                .sequenceValidated(true)
                .sequenceGapCount(0L)
                .sequenceGaps(new ArrayList<>())
                .timestampValidated(true)
                .timestampAnomalyCount(0L)
                .timestampAnomalies(new ArrayList<>())
                .complianceImpact(new HashMap<>())
                .requiresRemediation(false)
                .remediationPriority("LOW")
                .remediationActions(new ArrayList<>())
                .build();
        }

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            assertNotNull(dto);
            assertNull(dto.getPeriodStart());
            assertNull(dto.getPeriodEnd());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO(
                now.minusDays(1), now, now, "admin",
                true, 95.0, "COMPLETE",
                1000L, 990L, 10L, 2L, 0L,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                true, 1000L, 0L, new ArrayList<>(),
                true, 0L, new ArrayList<>(),
                true, 0L, new ArrayList<>(),
                new HashMap<>(), false, "LOW", new ArrayList<>()
            );
            assertEquals(now.minusDays(1), dto.getPeriodStart());
            assertEquals("admin", dto.getValidatedBy());
            assertEquals(95.0, dto.getCompletenessPercentage());
        }

        @Test
        @DisplayName("test builder and getters")
        void testBuilderAndGetters() {
            AuditTrailCompletenessDTO dto = buildFullDTO();
            assertNotNull(dto.getPeriodStart());
            assertNotNull(dto.getPeriodEnd());
            assertEquals("admin", dto.getValidatedBy());
            assertTrue(dto.getIsComplete());
            assertEquals(95.0, dto.getCompletenessPercentage());
            assertEquals(1000L, dto.getTotalEventsExpected());
            assertEquals(990L, dto.getTotalEventsFound());
        }

        @Test
        @DisplayName("test setters")
        void testSetters() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            LocalDateTime now = LocalDateTime.now();
            dto.setPeriodStart(now.minusDays(1));
            dto.setPeriodEnd(now);
            dto.setValidatedAt(now);
            dto.setValidatedBy("admin");
            dto.setIsComplete(true);
            dto.setCompletenessPercentage(99.0);
            dto.setCompletenessStatus("COMPLETE");
            dto.setTotalEventsExpected(500L);
            dto.setTotalEventsFound(495L);
            dto.setMissingEventCount(5L);
            dto.setDuplicateEventCount(0L);
            dto.setCorruptedEventCount(0L);
            dto.setDetectedGaps(new ArrayList<>());
            dto.setMissingEventTypes(new ArrayList<>());
            dto.setAffectedDomains(Arrays.asList("PAYMENTS"));
            dto.setAffectedUsers(Arrays.asList("user-1"));
            dto.setIntegrityVerified(true);
            dto.setIntegrityCheckCount(500L);
            dto.setIntegrityFailureCount(0L);
            dto.setIntegrityIssues(new ArrayList<>());
            dto.setSequenceValidated(true);
            dto.setSequenceGapCount(0L);
            dto.setSequenceGaps(new ArrayList<>());
            dto.setTimestampValidated(true);
            dto.setTimestampAnomalyCount(0L);
            dto.setTimestampAnomalies(new ArrayList<>());
            dto.setComplianceImpact(new HashMap<>());
            dto.setRequiresRemediation(false);
            dto.setRemediationPriority("LOW");
            dto.setRemediationActions(new ArrayList<>());
            assertEquals(now.minusDays(1), dto.getPeriodStart());
            assertEquals(99.0, dto.getCompletenessPercentage());
            assertEquals(Arrays.asList("PAYMENTS"), dto.getAffectedDomains());
        }

        @Test
        @DisplayName("test getGapPercentage with null expected")
        void testGetGapPercentageNullExpected() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setTotalEventsExpected(null);
            assertEquals(0.0, dto.getGapPercentage());
        }

        @Test
        @DisplayName("test getGapPercentage with zero expected")
        void testGetGapPercentageZeroExpected() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setTotalEventsExpected(0L);
            assertEquals(0.0, dto.getGapPercentage());
        }

        @Test
        @DisplayName("test getGapPercentage with null missing")
        void testGetGapPercentageNullMissing() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setTotalEventsExpected(100L);
            dto.setMissingEventCount(null);
            assertEquals(0.0, dto.getGapPercentage());
        }

        @Test
        @DisplayName("test getGapPercentage normal")
        void testGetGapPercentageNormal() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setTotalEventsExpected(1000L);
            dto.setMissingEventCount(50L);
            assertEquals(5.0, dto.getGapPercentage());
        }

        @Test
        @DisplayName("test meetsComplianceStandards isComplete null")
        void testMeetsComplianceIsCompleteNull() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            assertFalse(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test meetsComplianceStandards false")
        void testMeetsComplianceIsCompleteFalse() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIsComplete(false);
            assertFalse(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test meetsComplianceStandards gap too high")
        void testMeetsComplianceGapTooHigh() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIsComplete(true);
            dto.setTotalEventsExpected(100L);
            dto.setMissingEventCount(5L);
            assertFalse(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test meetsComplianceStandards integrity not verified")
        void testMeetsComplianceIntegrityNotVerified() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIsComplete(true);
            dto.setTotalEventsExpected(100L);
            dto.setMissingEventCount(0L);
            dto.setIntegrityVerified(false);
            dto.setIntegrityFailureCount(0L);
            assertFalse(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test meetsComplianceStandards integrity failures")
        void testMeetsComplianceIntegrityFailures() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIsComplete(true);
            dto.setTotalEventsExpected(100L);
            dto.setMissingEventCount(0L);
            dto.setIntegrityVerified(true);
            dto.setIntegrityFailureCount(5L);
            assertFalse(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test meetsComplianceStandards true")
        void testMeetsComplianceTrue() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIsComplete(true);
            dto.setTotalEventsExpected(100L);
            dto.setMissingEventCount(0L);
            dto.setIntegrityVerified(true);
            dto.setIntegrityFailureCount(0L);
            assertTrue(dto.meetsComplianceStandards());
        }

        @Test
        @DisplayName("test getHighestGapSeverity null")
        void testGetHighestGapSeverityNull() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            assertEquals("NONE", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getHighestGapSeverity empty")
        void testGetHighestGapSeverityEmpty() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setDetectedGaps(new ArrayList<>());
            assertEquals("NONE", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getHighestGapSeverity critical")
        void testGetHighestGapSeverityCritical() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setDetectedGaps(Arrays.asList(
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("CRITICAL").build(),
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("HIGH").build()
            ));
            assertEquals("CRITICAL", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getHighestGapSeverity high")
        void testGetHighestGapSeverityHigh() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setDetectedGaps(Arrays.asList(
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("HIGH").build(),
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("MEDIUM").build()
            ));
            assertEquals("HIGH", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getHighestGapSeverity medium")
        void testGetHighestGapSeverityMedium() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setDetectedGaps(Arrays.asList(
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("MEDIUM").build(),
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("LOW").build()
            ));
            assertEquals("MEDIUM", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getHighestGapSeverity low only")
        void testGetHighestGapSeverityLow() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setDetectedGaps(Arrays.asList(
                AuditTrailCompletenessDTO.AuditGapDTO.builder().severity("LOW").build()
            ));
            assertEquals("LOW", dto.getHighestGapSeverity());
        }

        @Test
        @DisplayName("test getTrailHealthScore all healthy")
        void testGetTrailHealthScoreAllHealthy() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            assertEquals(100.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test getTrailHealthScore with completeness")
        void testGetTrailHealthScoreWithCompleteness() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setCompletenessPercentage(80.0);
            assertEquals(80.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test getTrailHealthScore integrity not verified")
        void testGetTrailHealthScoreIntegrityNotVerified() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setIntegrityVerified(false);
            assertEquals(80.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test getTrailHealthScore sequence not validated")
        void testGetTrailHealthScoreSequenceNotValidated() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setSequenceValidated(false);
            assertEquals(90.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test getTrailHealthScore timestamp not validated")
        void testGetTrailHealthScoreTimestampNotValidated() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setTimestampValidated(false);
            assertEquals(95.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test getTrailHealthScore all issues floors at zero")
        void testGetTrailHealthScoreAllIssues() {
            AuditTrailCompletenessDTO dto = new AuditTrailCompletenessDTO();
            dto.setCompletenessPercentage(5.0);
            dto.setIntegrityVerified(false);
            dto.setSequenceValidated(false);
            dto.setTimestampValidated(false);
            assertEquals(0.0, dto.getTrailHealthScore());
        }

        @Test
        @DisplayName("test AuditGapDTO")
        void testAuditGapDTO() {
            LocalDateTime now = LocalDateTime.now();
            AuditTrailCompletenessDTO.AuditGapDTO gap1 = new AuditTrailCompletenessDTO.AuditGapDTO();
            assertNotNull(gap1);
            AuditTrailCompletenessDTO.AuditGapDTO gap2 = new AuditTrailCompletenessDTO.AuditGapDTO(
                "gap-1", now.minusHours(2), now.minusHours(1), 60L, "PAYMENTS",
                "MISSING_EVENTS", "HIGH", "desc", "cause", true, "manual"
            );
            assertEquals("gap-1", gap2.getGapId());
            AuditTrailCompletenessDTO.AuditGapDTO gap3 = AuditTrailCompletenessDTO.AuditGapDTO.builder()
                .gapId("gap-2").gapStart(now).gapEnd(now.plusHours(1))
                .durationMinutes(60L).affectedDomain("ORDERS")
                .gapType("SEQUENCE_GAP").severity("CRITICAL")
                .description("test gap").possibleCause("unknown")
                .canBeRecovered(false).recoveryMethod("none")
                .build();
            assertEquals("gap-2", gap3.getGapId());
            assertEquals("CRITICAL", gap3.getSeverity());
            gap3.setGapId("gap-3");
            gap3.setSeverity("LOW");
            assertEquals("gap-3", gap3.getGapId());
            assertEquals("LOW", gap3.getSeverity());
            AuditTrailCompletenessDTO.AuditGapDTO gap4 = new AuditTrailCompletenessDTO.AuditGapDTO();
            AuditTrailCompletenessDTO.AuditGapDTO gap5 = new AuditTrailCompletenessDTO.AuditGapDTO();
            assertEquals(gap4, gap5);
            assertNotEquals(gap4, gap2);
            assertNotNull(gap2.toString());
        }

        @Test
        @DisplayName("test SequenceGapDTO")
        void testSequenceGapDTO() {
            LocalDateTime now = LocalDateTime.now();
            AuditTrailCompletenessDTO.SequenceGapDTO dto = new AuditTrailCompletenessDTO.SequenceGapDTO();
            assertNotNull(dto);
            AuditTrailCompletenessDTO.SequenceGapDTO dto2 = new AuditTrailCompletenessDTO.SequenceGapDTO(
                100L, 105L, 5L, now, "context", "impact"
            );
            assertEquals(100L, dto2.getExpectedSequence());
            AuditTrailCompletenessDTO.SequenceGapDTO dto3 = AuditTrailCompletenessDTO.SequenceGapDTO.builder()
                .expectedSequence(200L).actualSequence(210L).gapSize(10L)
                .detectedAt(now).context("ctx").impact("imp").build();
            assertEquals(200L, dto3.getExpectedSequence());
            dto3.setExpectedSequence(300L);
            assertEquals(300L, dto3.getExpectedSequence());
            AuditTrailCompletenessDTO.SequenceGapDTO a = new AuditTrailCompletenessDTO.SequenceGapDTO();
            AuditTrailCompletenessDTO.SequenceGapDTO b = new AuditTrailCompletenessDTO.SequenceGapDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test TimestampAnomalyDTO")
        void testTimestampAnomalyDTO() {
            LocalDateTime now = LocalDateTime.now();
            AuditTrailCompletenessDTO.TimestampAnomalyDTO dto = new AuditTrailCompletenessDTO.TimestampAnomalyDTO();
            assertNotNull(dto);
            AuditTrailCompletenessDTO.TimestampAnomalyDTO dto2 = new AuditTrailCompletenessDTO.TimestampAnomalyDTO(
                "evt-1", now, now.plusMinutes(5), 5L, "FUTURE_TIMESTAMP", "impact", "cause"
            );
            assertEquals("evt-1", dto2.getEventId());
            AuditTrailCompletenessDTO.TimestampAnomalyDTO dto3 = AuditTrailCompletenessDTO.TimestampAnomalyDTO.builder()
                .eventId("evt-2").recordedTimestamp(now).expectedTimestamp(now)
                .deviationMinutes(10L).anomalyType("PAST_TIMESTAMP")
                .impact("imp").possibleCause("cause").build();
            assertEquals("evt-2", dto3.getEventId());
            dto3.setEventId("evt-3");
            assertEquals("evt-3", dto3.getEventId());
            AuditTrailCompletenessDTO.TimestampAnomalyDTO a = new AuditTrailCompletenessDTO.TimestampAnomalyDTO();
            AuditTrailCompletenessDTO.TimestampAnomalyDTO b = new AuditTrailCompletenessDTO.TimestampAnomalyDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test equals and toString")
        void testEqualsAndToString() {
            AuditTrailCompletenessDTO dto1 = buildFullDTO();
            String str = dto1.toString();
            assertNotNull(str);
        }
    }

    @Nested
    @DisplayName("ComplianceReportDTO Tests")
    class ComplianceReportDTOTests {

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            assertNotNull(dto);
            assertNull(dto.getReportId());
            assertNull(dto.getGeneratedAt());
            assertNull(dto.getStatus());
            assertNull(dto.getComplianceStatus());
            assertNull(dto.getTotalEvents());
            assertNull(dto.getCompliantEvents());
            assertNull(dto.getViolations());
            assertNull(dto.getRecommendations());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            List<ComplianceReportDTO.ComplianceViolationDTO> violations = new ArrayList<>();
            List<ComplianceReportDTO.ComplianceRecommendationDTO> recs = new ArrayList<>();
            ComplianceReportDTO dto = new ComplianceReportDTO(
                "rpt-1", now, "COMPLETED", "COMPLIANT",
                1000L, 980L, violations, recs
            );
            assertEquals("rpt-1", dto.getReportId());
            assertEquals(now, dto.getGeneratedAt());
            assertEquals("COMPLETED", dto.getStatus());
            assertEquals("COMPLIANT", dto.getComplianceStatus());
            assertEquals(1000L, dto.getTotalEvents());
            assertEquals(980L, dto.getCompliantEvents());
            assertEquals(violations, dto.getViolations());
            assertEquals(recs, dto.getRecommendations());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            LocalDateTime now = LocalDateTime.now();
            ComplianceReportDTO dto = ComplianceReportDTO.builder()
                .reportId("rpt-2").generatedAt(now).status("IN_PROGRESS")
                .complianceStatus("NON_COMPLIANT").totalEvents(500L)
                .compliantEvents(400L).violations(new ArrayList<>())
                .recommendations(new ArrayList<>()).build();
            assertEquals("rpt-2", dto.getReportId());
            assertEquals("IN_PROGRESS", dto.getStatus());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            LocalDateTime now = LocalDateTime.now();
            dto.setReportId("rpt-3");
            dto.setGeneratedAt(now);
            dto.setStatus("COMPLETED");
            dto.setComplianceStatus("COMPLIANT");
            dto.setTotalEvents(200L);
            dto.setCompliantEvents(195L);
            dto.setViolations(new ArrayList<>());
            dto.setRecommendations(new ArrayList<>());
            assertEquals("rpt-3", dto.getReportId());
            assertEquals(now, dto.getGeneratedAt());
            assertEquals("COMPLETED", dto.getStatus());
            assertEquals("COMPLIANT", dto.getComplianceStatus());
            assertEquals(200L, dto.getTotalEvents());
            assertEquals(195L, dto.getCompliantEvents());
        }

        @Test
        @DisplayName("test getCompliancePercentage total null")
        void testGetCompliancePercentageTotalNull() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(null);
            assertEquals(100.0, dto.getCompliancePercentage());
        }

        @Test
        @DisplayName("test getCompliancePercentage total zero")
        void testGetCompliancePercentageTotalZero() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(0L);
            assertEquals(100.0, dto.getCompliancePercentage());
        }

        @Test
        @DisplayName("test getCompliancePercentage compliant null")
        void testGetCompliancePercentageCompliantNull() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(100L);
            dto.setCompliantEvents(null);
            assertEquals(0.0, dto.getCompliancePercentage());
        }

        @Test
        @DisplayName("test getCompliancePercentage normal")
        void testGetCompliancePercentageNormal() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(1000L);
            dto.setCompliantEvents(950L);
            assertEquals(95.0, dto.getCompliancePercentage());
        }

        @Test
        @DisplayName("test isCompliant true")
        void testIsCompliantTrue() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(1000L);
            dto.setCompliantEvents(960L);
            dto.setComplianceStatus("COMPLIANT");
            dto.setViolations(null);
            assertTrue(dto.isCompliant());
        }

        @Test
        @DisplayName("test isCompliant false low percentage")
        void testIsCompliantFalseLowPercentage() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(1000L);
            dto.setCompliantEvents(900L);
            dto.setComplianceStatus("COMPLIANT");
            assertFalse(dto.isCompliant());
        }

        @Test
        @DisplayName("test isCompliant false wrong status")
        void testIsCompliantFalseWrongStatus() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(1000L);
            dto.setCompliantEvents(980L);
            dto.setComplianceStatus("NON_COMPLIANT");
            assertFalse(dto.isCompliant());
        }

        @Test
        @DisplayName("test isCompliant false has violations")
        void testIsCompliantFalseHasViolations() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setTotalEvents(1000L);
            dto.setCompliantEvents(980L);
            dto.setComplianceStatus("COMPLIANT");
            dto.setViolations(Arrays.asList(
                ComplianceReportDTO.ComplianceViolationDTO.builder().violationId("v-1").build()
            ));
            assertFalse(dto.isCompliant());
        }

        @Test
        @DisplayName("test getCriticalViolationCount null")
        void testGetCriticalViolationCountNull() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            assertEquals(0, dto.getCriticalViolationCount());
        }

        @Test
        @DisplayName("test getCriticalViolationCount with critical")
        void testGetCriticalViolationCountWithCritical() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setViolations(Arrays.asList(
                ComplianceReportDTO.ComplianceViolationDTO.builder().severity("CRITICAL").build(),
                ComplianceReportDTO.ComplianceViolationDTO.builder().severity("HIGH").build(),
                ComplianceReportDTO.ComplianceViolationDTO.builder().severity("CRITICAL").build()
            ));
            assertEquals(2, dto.getCriticalViolationCount());
        }

        @Test
        @DisplayName("test getHighPriorityRecommendationCount null")
        void testGetHighPriorityRecommendationCountNull() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            assertEquals(0, dto.getHighPriorityRecommendationCount());
        }

        @Test
        @DisplayName("test getHighPriorityRecommendationCount with high and urgent")
        void testGetHighPriorityRecommendationCount() {
            ComplianceReportDTO dto = new ComplianceReportDTO();
            dto.setRecommendations(Arrays.asList(
                ComplianceReportDTO.ComplianceRecommendationDTO.builder().priority("HIGH").build(),
                ComplianceReportDTO.ComplianceRecommendationDTO.builder().priority("URGENT").build(),
                ComplianceReportDTO.ComplianceRecommendationDTO.builder().priority("LOW").build()
            ));
            assertEquals(2, dto.getHighPriorityRecommendationCount());
        }

        @Test
        @DisplayName("test ComplianceFindingDTO")
        void testComplianceFindingDTO() {
            ComplianceReportDTO.ComplianceFindingDTO dto = new ComplianceReportDTO.ComplianceFindingDTO();
            assertNotNull(dto);
            ComplianceReportDTO.ComplianceFindingDTO dto2 = new ComplianceReportDTO.ComplianceFindingDTO(
                "f-1", "SECURITY", "HIGH", "desc", 10L, "ev", "rec", "OPEN"
            );
            assertEquals("f-1", dto2.getFindingId());
            ComplianceReportDTO.ComplianceFindingDTO dto3 = ComplianceReportDTO.ComplianceFindingDTO.builder()
                .findingId("f-2").category("AUDIT").severity("CRITICAL")
                .description("test").eventCount(5L).evidence("ev")
                .recommendation("rec").status("RESOLVED").build();
            assertEquals("f-2", dto3.getFindingId());
            dto3.setFindingId("f-3");
            assertEquals("f-3", dto3.getFindingId());
            ComplianceReportDTO.ComplianceFindingDTO a = new ComplianceReportDTO.ComplianceFindingDTO();
            ComplianceReportDTO.ComplianceFindingDTO b = new ComplianceReportDTO.ComplianceFindingDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test ComplianceViolationDTO")
        void testComplianceViolationDTO() {
            LocalDateTime now = LocalDateTime.now();
            ComplianceReportDTO.ComplianceViolationDTO dto = new ComplianceReportDTO.ComplianceViolationDTO();
            assertNotNull(dto);
            ComplianceReportDTO.ComplianceViolationDTO dto2 = new ComplianceReportDTO.ComplianceViolationDTO(
                "v-1", "RULE_1", "desc", "CRITICAL", 5L, now, now, "fix", "OPEN"
            );
            assertEquals("v-1", dto2.getViolationId());
            ComplianceReportDTO.ComplianceViolationDTO dto3 = ComplianceReportDTO.ComplianceViolationDTO.builder()
                .violationId("v-2").rule("RULE_2").description("test")
                .severity("HIGH").occurrenceCount(3L).firstOccurrence(now)
                .lastOccurrence(now).remediation("fix").status("RESOLVED").build();
            assertEquals("v-2", dto3.getViolationId());
            dto3.setViolationId("v-3");
            assertEquals("v-3", dto3.getViolationId());
            ComplianceReportDTO.ComplianceViolationDTO a = new ComplianceReportDTO.ComplianceViolationDTO();
            ComplianceReportDTO.ComplianceViolationDTO b = new ComplianceReportDTO.ComplianceViolationDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test ComplianceRecommendationDTO")
        void testComplianceRecommendationDTO() {
            LocalDateTime now = LocalDateTime.now();
            ComplianceReportDTO.ComplianceRecommendationDTO dto = new ComplianceReportDTO.ComplianceRecommendationDTO();
            assertNotNull(dto);
            ComplianceReportDTO.ComplianceRecommendationDTO dto2 = new ComplianceReportDTO.ComplianceRecommendationDTO(
                "rec-1", "SECURITY", "HIGH", "desc", "reason", "impl", 5, now
            );
            assertEquals("rec-1", dto2.getRecommendationId());
            ComplianceReportDTO.ComplianceRecommendationDTO dto3 = ComplianceReportDTO.ComplianceRecommendationDTO.builder()
                .recommendationId("rec-2").category("AUDIT").priority("URGENT")
                .description("test").rationale("reason").implementation("impl")
                .estimatedEffort(10).targetDate(now).build();
            assertEquals("rec-2", dto3.getRecommendationId());
            dto3.setRecommendationId("rec-3");
            assertEquals("rec-3", dto3.getRecommendationId());
            ComplianceReportDTO.ComplianceRecommendationDTO a = new ComplianceReportDTO.ComplianceRecommendationDTO();
            ComplianceReportDTO.ComplianceRecommendationDTO b = new ComplianceReportDTO.ComplianceRecommendationDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test ComplianceRequirementStatusDTO")
        void testComplianceRequirementStatusDTO() {
            ComplianceReportDTO.ComplianceRequirementStatusDTO dto = new ComplianceReportDTO.ComplianceRequirementStatusDTO();
            assertNotNull(dto);
            ComplianceReportDTO.ComplianceRequirementStatusDTO dto2 = new ComplianceReportDTO.ComplianceRequirementStatusDTO(
                "req-1", "name", "MET", 95.0, "ev", "gap", "fix"
            );
            assertEquals("req-1", dto2.getRequirementId());
            ComplianceReportDTO.ComplianceRequirementStatusDTO dto3 = ComplianceReportDTO.ComplianceRequirementStatusDTO.builder()
                .requirementId("req-2").requirementName("name2")
                .status("NOT_MET").compliancePercentage(50.0)
                .evidence("ev").gap("gap").remediation("fix").build();
            assertEquals("req-2", dto3.getRequirementId());
            dto3.setRequirementId("req-3");
            assertEquals("req-3", dto3.getRequirementId());
            ComplianceReportDTO.ComplianceRequirementStatusDTO a = new ComplianceReportDTO.ComplianceRequirementStatusDTO();
            ComplianceReportDTO.ComplianceRequirementStatusDTO b = new ComplianceReportDTO.ComplianceRequirementStatusDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test equals and toString")
        void testEqualsAndToString() {
            ComplianceReportDTO dto1 = new ComplianceReportDTO();
            ComplianceReportDTO dto2 = new ComplianceReportDTO();
            assertEquals(dto1, dto2);
            dto1.setReportId("rpt-1");
            assertNotEquals(dto1, dto2);
            assertNotNull(dto1.toString());
        }
    }

    @Nested
    @DisplayName("HealthCheckDTO Tests")
    class HealthCheckDTOTests {

        private HealthCheckDTO buildHealthyDTO() {
            Map<String, HealthCheckDTO.ComponentHealthDTO> components = new HashMap<>();
            components.put("db", HealthCheckDTO.ComponentHealthDTO.builder()
                .status("UP").description("Database").responseTime(10L).build());
            return HealthCheckDTO.builder()
                .status("UP")
                .timestamp(LocalDateTime.now())
                .details("All good")
                .eventsLastHour(100L)
                .eventsLastDay(2400L)
                .version("1.0.0")
                .buildTime("2024-01-01")
                .commitHash("abc123")
                .components(components)
                .averageResponseTime(50.0)
                .totalRequests(10000L)
                .requestsPerSecond(10.0)
                .errorCount(5L)
                .errorRate(0.5)
                .memoryUsage(50.0)
                .cpuUsage(40.0)
                .diskUsage(60.0)
                .availableMemory(512L)
                .totalMemory(1024L)
                .databaseStatus("UP")
                .activeConnections(5)
                .maxConnections(20)
                .connectionPoolUsage(25.0)
                .cacheStatus("UP")
                .cacheHits(800L)
                .cacheMisses(200L)
                .cacheHitRatio(80.0)
                .messagingStatus("UP")
                .messagesSent(500L)
                .messagesReceived(499L)
                .messagingErrors(0L)
                .build();
        }

        @Test
        @DisplayName("test no-args constructor")
        void testNoArgsConstructor() {
            HealthCheckDTO dto = new HealthCheckDTO();
            assertNotNull(dto);
            assertNull(dto.getStatus());
            assertNull(dto.getTimestamp());
            assertNull(dto.getDetails());
            assertNull(dto.getEventsLastHour());
            assertNull(dto.getEventsLastDay());
            assertNull(dto.getVersion());
            assertNull(dto.getBuildTime());
            assertNull(dto.getCommitHash());
            assertNull(dto.getComponents());
            assertNull(dto.getAverageResponseTime());
            assertNull(dto.getTotalRequests());
            assertNull(dto.getRequestsPerSecond());
            assertNull(dto.getErrorCount());
            assertNull(dto.getErrorRate());
            assertNull(dto.getMemoryUsage());
            assertNull(dto.getCpuUsage());
            assertNull(dto.getDiskUsage());
            assertNull(dto.getAvailableMemory());
            assertNull(dto.getTotalMemory());
            assertNull(dto.getDatabaseStatus());
            assertNull(dto.getActiveConnections());
            assertNull(dto.getMaxConnections());
            assertNull(dto.getConnectionPoolUsage());
            assertNull(dto.getCacheStatus());
            assertNull(dto.getCacheHits());
            assertNull(dto.getCacheMisses());
            assertNull(dto.getCacheHitRatio());
            assertNull(dto.getMessagingStatus());
            assertNull(dto.getMessagesSent());
            assertNull(dto.getMessagesReceived());
            assertNull(dto.getMessagingErrors());
        }

        @Test
        @DisplayName("test all-args constructor")
        void testAllArgsConstructor() {
            LocalDateTime now = LocalDateTime.now();
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            HealthCheckDTO dto = new HealthCheckDTO(
                "UP", now, "ok", 100L, 2400L, "1.0", "today", "hash",
                comp, 50.0, 1000L, 10.0, 5L, 0.5,
                60.0, 40.0, 70.0, 512L, 1024L,
                "UP", 5, 20, 25.0,
                "UP", 800L, 200L, 80.0,
                "UP", 500L, 499L, 0L
            );
            assertEquals("UP", dto.getStatus());
            assertEquals(now, dto.getTimestamp());
        }

        @Test
        @DisplayName("test builder")
        void testBuilder() {
            HealthCheckDTO dto = buildHealthyDTO();
            assertEquals("UP", dto.getStatus());
            assertEquals("1.0.0", dto.getVersion());
            assertEquals(50.0, dto.getAverageResponseTime());
            assertEquals(0.5, dto.getErrorRate());
        }

        @Test
        @DisplayName("test setters and getters")
        void testSettersAndGetters() {
            HealthCheckDTO dto = new HealthCheckDTO();
            LocalDateTime now = LocalDateTime.now();
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            dto.setStatus("UP");
            dto.setTimestamp(now);
            dto.setDetails("healthy");
            dto.setEventsLastHour(50L);
            dto.setEventsLastDay(1200L);
            dto.setVersion("2.0");
            dto.setBuildTime("tomorrow");
            dto.setCommitHash("def456");
            dto.setComponents(comp);
            dto.setAverageResponseTime(100.0);
            dto.setTotalRequests(5000L);
            dto.setRequestsPerSecond(5.0);
            dto.setErrorCount(10L);
            dto.setErrorRate(2.0);
            dto.setMemoryUsage(70.0);
            dto.setCpuUsage(60.0);
            dto.setDiskUsage(80.0);
            dto.setAvailableMemory(256L);
            dto.setTotalMemory(512L);
            dto.setDatabaseStatus("UP");
            dto.setActiveConnections(3);
            dto.setMaxConnections(10);
            dto.setConnectionPoolUsage(30.0);
            dto.setCacheStatus("UP");
            dto.setCacheHits(400L);
            dto.setCacheMisses(100L);
            dto.setCacheHitRatio(80.0);
            dto.setMessagingStatus("UP");
            dto.setMessagesSent(250L);
            dto.setMessagesReceived(249L);
            dto.setMessagingErrors(1L);
            assertEquals("UP", dto.getStatus());
            assertEquals(now, dto.getTimestamp());
            assertEquals("healthy", dto.getDetails());
            assertEquals(50L, dto.getEventsLastHour());
            assertEquals(1200L, dto.getEventsLastDay());
            assertEquals("2.0", dto.getVersion());
            assertEquals("tomorrow", dto.getBuildTime());
            assertEquals("def456", dto.getCommitHash());
            assertEquals(comp, dto.getComponents());
            assertEquals(100.0, dto.getAverageResponseTime());
            assertEquals(5000L, dto.getTotalRequests());
            assertEquals(5.0, dto.getRequestsPerSecond());
            assertEquals(10L, dto.getErrorCount());
            assertEquals(2.0, dto.getErrorRate());
            assertEquals(70.0, dto.getMemoryUsage());
            assertEquals(60.0, dto.getCpuUsage());
            assertEquals(80.0, dto.getDiskUsage());
            assertEquals(256L, dto.getAvailableMemory());
            assertEquals(512L, dto.getTotalMemory());
            assertEquals("UP", dto.getDatabaseStatus());
            assertEquals(3, dto.getActiveConnections());
            assertEquals(10, dto.getMaxConnections());
            assertEquals(30.0, dto.getConnectionPoolUsage());
            assertEquals("UP", dto.getCacheStatus());
            assertEquals(400L, dto.getCacheHits());
            assertEquals(100L, dto.getCacheMisses());
            assertEquals(80.0, dto.getCacheHitRatio());
            assertEquals("UP", dto.getMessagingStatus());
            assertEquals(250L, dto.getMessagesSent());
            assertEquals(249L, dto.getMessagesReceived());
            assertEquals(1L, dto.getMessagingErrors());
        }

        @Test
        @DisplayName("test isHealthy true")
        void testIsHealthyTrue() {
            HealthCheckDTO dto = buildHealthyDTO();
            assertTrue(dto.isHealthy());
        }

        @Test
        @DisplayName("test isHealthy status not UP")
        void testIsHealthyStatusNotUp() {
            HealthCheckDTO dto = buildHealthyDTO();
            dto.setStatus("DOWN");
            assertFalse(dto.isHealthy());
        }

        @Test
        @DisplayName("test isHealthy components not healthy")
        void testIsHealthyComponentsNotHealthy() {
            HealthCheckDTO dto = buildHealthyDTO();
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            comp.put("db", HealthCheckDTO.ComponentHealthDTO.builder().status("DOWN").build());
            dto.setComponents(comp);
            assertFalse(dto.isHealthy());
        }

        @Test
        @DisplayName("test isHealthy performance not healthy")
        void testIsHealthyPerformanceNotHealthy() {
            HealthCheckDTO dto = buildHealthyDTO();
            dto.setErrorRate(10.0);
            assertFalse(dto.isHealthy());
        }

        @Test
        @DisplayName("test isHealthy resources not healthy")
        void testIsHealthyResourcesNotHealthy() {
            HealthCheckDTO dto = buildHealthyDTO();
            dto.setMemoryUsage(90.0);
            assertFalse(dto.isHealthy());
        }

        @Test
        @DisplayName("test isComponentsHealthy null")
        void testIsComponentsHealthyNull() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setComponents(null);
            assertTrue(dto.isComponentsHealthy());
        }

        @Test
        @DisplayName("test isComponentsHealthy empty")
        void testIsComponentsHealthyEmpty() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setComponents(new HashMap<>());
            assertTrue(dto.isComponentsHealthy());
        }

        @Test
        @DisplayName("test isComponentsHealthy all up")
        void testIsComponentsHealthyAllUp() {
            HealthCheckDTO dto = new HealthCheckDTO();
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            comp.put("db", HealthCheckDTO.ComponentHealthDTO.builder().status("UP").build());
            comp.put("cache", HealthCheckDTO.ComponentHealthDTO.builder().status("UP").build());
            dto.setComponents(comp);
            assertTrue(dto.isComponentsHealthy());
        }

        @Test
        @DisplayName("test isComponentsHealthy one down")
        void testIsComponentsHealthyOneDown() {
            HealthCheckDTO dto = new HealthCheckDTO();
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            comp.put("db", HealthCheckDTO.ComponentHealthDTO.builder().status("UP").build());
            comp.put("redis", HealthCheckDTO.ComponentHealthDTO.builder().status("DOWN").build());
            dto.setComponents(comp);
            assertFalse(dto.isComponentsHealthy());
        }

        @Test
        @DisplayName("test isPerformanceHealthy all null")
        void testIsPerformanceHealthyAllNull() {
            HealthCheckDTO dto = new HealthCheckDTO();
            assertTrue(dto.isPerformanceHealthy());
        }

        @Test
        @DisplayName("test isPerformanceHealthy error rate high")
        void testIsPerformanceHealthyErrorRateHigh() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setErrorRate(10.0);
            assertFalse(dto.isPerformanceHealthy());
        }

        @Test
        @DisplayName("test isPerformanceHealthy response time high")
        void testIsPerformanceHealthyResponseTimeHigh() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setAverageResponseTime(1500.0);
            assertFalse(dto.isPerformanceHealthy());
        }

        @Test
        @DisplayName("test isPerformanceHealthy within range")
        void testIsPerformanceHealthyWithinRange() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setErrorRate(3.0);
            dto.setAverageResponseTime(500.0);
            assertTrue(dto.isPerformanceHealthy());
        }

        @Test
        @DisplayName("test isResourcesHealthy all null")
        void testIsResourcesHealthyAllNull() {
            HealthCheckDTO dto = new HealthCheckDTO();
            assertTrue(dto.isResourcesHealthy());
        }

        @Test
        @DisplayName("test isResourcesHealthy memory high")
        void testIsResourcesHealthyMemoryHigh() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setMemoryUsage(90.0);
            assertFalse(dto.isResourcesHealthy());
        }

        @Test
        @DisplayName("test isResourcesHealthy cpu high")
        void testIsResourcesHealthyCpuHigh() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setCpuUsage(85.0);
            assertFalse(dto.isResourcesHealthy());
        }

        @Test
        @DisplayName("test isResourcesHealthy disk high")
        void testIsResourcesHealthyDiskHigh() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setDiskUsage(95.0);
            assertFalse(dto.isResourcesHealthy());
        }

        @Test
        @DisplayName("test isResourcesHealthy within range")
        void testIsResourcesHealthyWithinRange() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setMemoryUsage(70.0);
            dto.setCpuUsage(60.0);
            dto.setDiskUsage(80.0);
            assertTrue(dto.isResourcesHealthy());
        }

        @Test
        @DisplayName("test getStatusColor null")
        void testGetStatusColorNull() {
            HealthCheckDTO dto = new HealthCheckDTO();
            assertEquals("#808080", dto.getStatusColor());
        }

        @Test
        @DisplayName("test getStatusColor UP")
        void testGetStatusColorUp() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("UP");
            assertEquals("#28a745", dto.getStatusColor());
        }

        @Test
        @DisplayName("test getStatusColor DOWN")
        void testGetStatusColorDown() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("DOWN");
            assertEquals("#dc3545", dto.getStatusColor());
        }

        @Test
        @DisplayName("test getStatusColor OUT_OF_SERVICE")
        void testGetStatusColorOutOfService() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("OUT_OF_SERVICE");
            assertEquals("#ffc107", dto.getStatusColor());
        }

        @Test
        @DisplayName("test getStatusColor default")
        void testGetStatusColorDefault() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("UNKNOWN");
            assertEquals("#6c757d", dto.getStatusColor());
        }

        @Test
        @DisplayName("test getCriticalIssuesSummary service down")
        void testGetCriticalIssuesSummaryServiceDown() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("DOWN");
            assertTrue(dto.getCriticalIssuesSummary().contains("Service is DOWN"));
        }

        @Test
        @DisplayName("test getCriticalIssuesSummary components down")
        void testGetCriticalIssuesSummaryComponentsDown() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("UP");
            Map<String, HealthCheckDTO.ComponentHealthDTO> comp = new HashMap<>();
            comp.put("db", HealthCheckDTO.ComponentHealthDTO.builder().status("DOWN").build());
            dto.setComponents(comp);
            String summary = dto.getCriticalIssuesSummary();
            assertTrue(summary.contains("component(s) down"));
        }

        @Test
        @DisplayName("test getCriticalIssuesSummary high error rate")
        void testGetCriticalIssuesSummaryHighErrorRate() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("UP");
            dto.setErrorRate(10.0);
            String summary = dto.getCriticalIssuesSummary();
            assertTrue(summary.contains("High error rate"));
        }

        @Test
        @DisplayName("test getCriticalIssuesSummary high memory")
        void testGetCriticalIssuesSummaryHighMemory() {
            HealthCheckDTO dto = new HealthCheckDTO();
            dto.setStatus("UP");
            dto.setMemoryUsage(90.0);
            String summary = dto.getCriticalIssuesSummary();
            assertTrue(summary.contains("High memory usage"));
        }

        @Test
        @DisplayName("test getCriticalIssuesSummary no issues")
        void testGetCriticalIssuesSummaryNoIssues() {
            HealthCheckDTO dto = buildHealthyDTO();
            assertEquals("No critical issues detected.", dto.getCriticalIssuesSummary());
        }

        @Test
        @DisplayName("test ComponentHealthDTO")
        void testComponentHealthDTO() {
            LocalDateTime now = LocalDateTime.now();
            Map<String, Object> details = new HashMap<>();
            details.put("key", "value");
            HealthCheckDTO.ComponentHealthDTO dto1 = new HealthCheckDTO.ComponentHealthDTO();
            assertNotNull(dto1);
            HealthCheckDTO.ComponentHealthDTO dto2 = new HealthCheckDTO.ComponentHealthDTO(
                "UP", "Database", details, now, 15L, null
            );
            assertEquals("UP", dto2.getStatus());
            assertEquals("Database", dto2.getDescription());
            HealthCheckDTO.ComponentHealthDTO dto3 = HealthCheckDTO.ComponentHealthDTO.builder()
                .status("DOWN").description("Cache").details(details)
                .lastCheck(now).responseTime(100L).errorMessage("timeout").build();
            assertEquals("DOWN", dto3.getStatus());
            assertEquals("timeout", dto3.getErrorMessage());
            dto3.setStatus("UP");
            dto3.setDescription("Redis");
            dto3.setDetails(details);
            dto3.setLastCheck(now);
            dto3.setResponseTime(5L);
            dto3.setErrorMessage(null);
            assertEquals("UP", dto3.getStatus());
            assertEquals("Redis", dto3.getDescription());
            assertEquals(5L, dto3.getResponseTime());
            HealthCheckDTO.ComponentHealthDTO a = new HealthCheckDTO.ComponentHealthDTO();
            HealthCheckDTO.ComponentHealthDTO b = new HealthCheckDTO.ComponentHealthDTO();
            assertEquals(a, b);
            assertNotNull(a.toString());
        }

        @Test
        @DisplayName("test equals and toString")
        void testEqualsAndToString() {
            HealthCheckDTO dto1 = new HealthCheckDTO();
            HealthCheckDTO dto2 = new HealthCheckDTO();
            assertEquals(dto1, dto2);
            dto1.setStatus("UP");
            assertNotEquals(dto1, dto2);
            assertNotNull(dto1.toString());
        }
    }
}
