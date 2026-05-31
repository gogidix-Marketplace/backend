package com.gogidix.sysadmin.accessrequest.application.dto;

import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestDto;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccessRequestDtoTest {

        @Test
    void testBuilder() {
        AccessRequestDto dto = AccessRequestDto.builder()
                        .tenantId("test-tenantId")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .startDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .endDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestedFor", dto.getRequestedFor());
        assertEquals("test-requestType", dto.getRequestType());
        assertEquals("test-resourceType", dto.getResourceType());
        assertEquals("test-accessLevel", dto.getAccessLevel());
        assertEquals("test-justification", dto.getJustification());
    }

    @Test
    void testSettersAndGetters() {
        AccessRequestDto dto = new AccessRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setRequestedFor("val-requestedFor");
        dto.setRequestType("val-requestType");
        dto.setResourceType("val-resourceType");
        dto.setAccessLevel("val-accessLevel");
        dto.setJustification("val-justification");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestedFor", dto.getRequestedFor());
        assertEquals("val-requestType", dto.getRequestType());
        assertEquals("val-resourceType", dto.getResourceType());
        assertEquals("val-accessLevel", dto.getAccessLevel());
        assertEquals("val-justification", dto.getJustification());
    }

    @Test
    void testEqualsAndHashCode() {
        AccessRequestDto dto1 = AccessRequestDto.builder()
                        .tenantId("test-tenantId")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .startDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .endDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AccessRequestDto dto2 = AccessRequestDto.builder()
                        .tenantId("test-tenantId")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .startDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .endDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AccessRequestDto dto = AccessRequestDto.builder()
                        .tenantId("test-tenantId")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .startDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .endDateTime(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}