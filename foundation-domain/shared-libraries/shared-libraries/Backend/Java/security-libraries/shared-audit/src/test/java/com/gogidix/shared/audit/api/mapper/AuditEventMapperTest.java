package com.gogidix.shared.audit.api.mapper;

import com.gogidix.shared.audit.api.dto.*;
import com.gogidix.shared.audit.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuditEventMapper Tests")
class AuditEventMapperTest {

    private final AuditEventMapper mapper = new AuditEventMapper() {
        @Override
        public AuditEventDTO toDTO(AuditEvent auditEvent) { return null; }
        @Override
        public List<AuditEventDTO> toDTOList(List<AuditEvent> auditEvents) { return null; }
        @Override
        public AuditEventCreationRequest toCreationRequest(CreateAuditEventDTO createDTO) { return null; }
        @Override
        public AuditSearchCriteria toSearchCriteria(AuditSearchDTO searchDTO) { return null; }
        @Override
        public AuditStatisticsDTO toStatisticsDTO(AuditStatistics statistics) { return null; }
        @Override
        public ComplianceReportDTO toComplianceReportDTO(ComplianceReport report) { return null; }
    };

    @Test
    @DisplayName("test map LocalDateTime")
    void testMapLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, mapper.map(now));
    }

    @Test
    @DisplayName("test map LocalDateTime null")
    void testMapLocalDateTimeNull() {
        assertNull(mapper.map((LocalDateTime) null));
    }

    @Test
    @DisplayName("test mapEnum")
    void testMapEnum() {
        assertEquals("LOGIN_ATTEMPT", mapper.mapEnum(AuditEventType.LOGIN_ATTEMPT));
        assertEquals("SUCCESS", mapper.mapEnum(AuditResult.SUCCESS));
    }

    @Test
    @DisplayName("test mapEnum null")
    void testMapEnumNull() {
        assertNull(mapper.mapEnum(null));
    }

    @Test
    @DisplayName("test mapRiskScore")
    void testMapRiskScore() {
        assertEquals("42", mapper.mapRiskScore(42));
    }

    @Test
    @DisplayName("test mapRiskScore null")
    void testMapRiskScoreNull() {
        assertEquals("0", mapper.mapRiskScore(null));
    }

    @Test
    @DisplayName("test mapMetadata")
    void testMapMetadata() {
        Object meta = "some metadata string";
        assertEquals("some metadata string", mapper.mapMetadata(meta));
    }

    @Test
    @DisplayName("test mapMetadata null")
    void testMapMetadataNull() {
        assertNull(mapper.mapMetadata(null));
    }

    @Test
    @DisplayName("test mapMetadata object toString")
    void testMapMetadataObjectToString() {
        Object meta = new HashMap<String, String>() {{ put("key", "val"); }};
        String result = mapper.mapMetadata(meta);
        assertNotNull(result);
    }

    @Test
    @DisplayName("test mapMetadataFromString null")
    void testMapMetadataFromStringNull() {
        assertNull(mapper.mapMetadataFromString(null));
    }

    @Test
    @DisplayName("test mapMetadataFromString empty")
    void testMapMetadataFromStringEmpty() {
        assertNull(mapper.mapMetadataFromString(""));
        assertNull(mapper.mapMetadataFromString("   "));
    }

    @Test
    @DisplayName("test mapMetadataFromString valid")
    void testMapMetadataFromStringValid() {
        Object result = mapper.mapMetadataFromString("{\"key\":\"val\"}");
        assertNotNull(result);
        assertEquals("{\"key\":\"val\"}", result);
    }

    @Test
    @DisplayName("test mapIntToInteger")
    void testMapIntToInteger() {
        assertEquals(Integer.valueOf(42), mapper.mapIntToInteger(42));
        assertEquals(Integer.valueOf(0), mapper.mapIntToInteger(0));
        assertEquals(Integer.valueOf(-1), mapper.mapIntToInteger(-1));
    }

    @Test
    @DisplayName("test mapIntegerToInt")
    void testMapIntegerToInt() {
        assertEquals(42, mapper.mapIntegerToInt(42));
        assertEquals(0, mapper.mapIntegerToInt(0));
    }

    @Test
    @DisplayName("test mapIntegerToInt null")
    void testMapIntegerToIntNull() {
        assertEquals(0, mapper.mapIntegerToInt(null));
    }

    @Test
    @DisplayName("test mapBooleanToBoolean")
    void testMapBooleanToBoolean() {
        assertTrue(mapper.mapBooleanToBoolean(true));
        assertFalse(mapper.mapBooleanToBoolean(false));
    }

    @Test
    @DisplayName("test mapBooleanToboolean")
    void testMapBooleanToboolean() {
        assertTrue(mapper.mapBooleanToboolean(Boolean.TRUE));
        assertFalse(mapper.mapBooleanToboolean(Boolean.FALSE));
    }

    @Test
    @DisplayName("test mapBooleanToboolean null")
    void testMapBooleanTobooleanNull() {
        assertFalse(mapper.mapBooleanToboolean(null));
    }

    @Test
    @DisplayName("test afterMappingAuditEventDTO")
    void testAfterMappingAuditEventDTO() {
        AuditEventDTO dto = new AuditEventDTO();
        AuditEvent entity = AuditEvent.builder()
            .eventId("evt-1")
            .userId("user-1")
            .sessionId("sess-1")
            .timestamp(LocalDateTime.now())
            .eventType(AuditEventType.SECURITY_EVENT)
            .domain(BusinessDomain.SECURITY)
            .action("INTRUSION")
            .resource("/api/admin")
            .result(AuditResult.FAILURE)
            .description("Security breach")
            .complianceType(ComplianceType.GDPR)
            .riskScore("HIGH")
            .build();

        mapper.afterMappingAuditEventDTO(dto, entity);

        assertEquals("CRITICAL", dto.getSeverity());
        assertTrue(dto.getRequiresSecurityEscalation());
        assertTrue(dto.getCompliantEvent());
        assertFalse(dto.getFinancialEvent());
    }

    @Test
    @DisplayName("test afterMappingAuditEventDTO null dto")
    void testAfterMappingAuditEventDTONullDto() {
        AuditEvent entity = AuditEvent.builder()
            .eventId("evt-1")
            .userId("user-1")
            .sessionId("sess-1")
            .timestamp(LocalDateTime.now())
            .eventType(AuditEventType.DATA_ACCESS)
            .domain(BusinessDomain.IDENTITY)
            .action("READ")
            .resource("/api/users")
            .result(AuditResult.SUCCESS)
            .description("Read users")
            .build();

        mapper.afterMappingAuditEventDTO(null, entity);

        assertEquals("evt-1", entity.getEventId());
    }

    @Test
    @DisplayName("test afterMappingAuditEventDTO null entity")
    void testAfterMappingAuditEventDTONullEntity() {
        AuditEventDTO dto = new AuditEventDTO();
        mapper.afterMappingAuditEventDTO(dto, null);
        assertNull(dto.getSeverity());
    }

    @Test
    @DisplayName("test afterMappingAuditEventDTO both null")
    void testAfterMappingAuditEventDTOBothNull() {
        mapper.afterMappingAuditEventDTO(null, null);
    }

    @Test
    @DisplayName("test beforeMappingCreationRequest valid")
    void testBeforeMappingCreationRequestValid() {
        CreateAuditEventDTO source = CreateAuditEventDTO.builder()
            .userId("user-1")
            .sessionId("sess-1")
            .action("LOGIN")
            .build();
        AuditEventCreationRequest.AuditEventCreationRequestBuilder target =
            AuditEventCreationRequest.builder();

        assertDoesNotThrow(() -> mapper.beforeMappingCreationRequest(target, source));
    }

    @Test
    @DisplayName("test beforeMappingCreationRequest invalid throws")
    void testBeforeMappingCreationRequestInvalid() {
        CreateAuditEventDTO source = CreateAuditEventDTO.builder().build();
        AuditEventCreationRequest.AuditEventCreationRequestBuilder target =
            AuditEventCreationRequest.builder();

        assertThrows(IllegalArgumentException.class,
            () -> mapper.beforeMappingCreationRequest(target, source));
    }

    @Test
    @DisplayName("test beforeMappingCreationRequest null source")
    void testBeforeMappingCreationRequestNullSource() {
        AuditEventCreationRequest.AuditEventCreationRequestBuilder target =
            AuditEventCreationRequest.builder();

        assertDoesNotThrow(() -> mapper.beforeMappingCreationRequest(target, null));
    }

    @Test
    @DisplayName("test mapMetadata with object throwing toString")
    void testMapMetadataWithThrowingObject() {
        Object throwingObj = new Object() {
            @Override
            public String toString() {
                throw new RuntimeException("toString failed");
            }
        };
        assertEquals("{}", mapper.mapMetadata(throwingObj));
    }
}
