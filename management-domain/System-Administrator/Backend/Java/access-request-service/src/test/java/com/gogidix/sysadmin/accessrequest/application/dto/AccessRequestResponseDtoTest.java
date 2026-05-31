package com.gogidix.sysadmin.accessrequest.application.dto;

import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestResponseDto;
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
class AccessRequestResponseDtoTest {

        @Test
    void testBuilder() {
        AccessRequestResponseDto dto = AccessRequestResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .requestNumber("test-requestNumber")
            .requestedBy("test-requestedBy")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .status("test-status")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestNumber", dto.getRequestNumber());
        assertEquals("test-requestedBy", dto.getRequestedBy());
        assertEquals("test-requestedFor", dto.getRequestedFor());
        assertEquals("test-requestType", dto.getRequestType());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-resourceType", dto.getResourceType());
        assertEquals("test-accessLevel", dto.getAccessLevel());
        assertEquals("test-justification", dto.getJustification());
    }

    @Test
    void testSettersAndGetters() {
        AccessRequestResponseDto dto = new AccessRequestResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setRequestNumber("val-requestNumber");
        dto.setRequestedBy("val-requestedBy");
        dto.setRequestedFor("val-requestedFor");
        dto.setRequestType("val-requestType");
        dto.setStatus("val-status");
        dto.setResourceType("val-resourceType");
        dto.setAccessLevel("val-accessLevel");
        dto.setJustification("val-justification");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestNumber", dto.getRequestNumber());
        assertEquals("val-requestedBy", dto.getRequestedBy());
        assertEquals("val-requestedFor", dto.getRequestedFor());
        assertEquals("val-requestType", dto.getRequestType());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-resourceType", dto.getResourceType());
        assertEquals("val-accessLevel", dto.getAccessLevel());
        assertEquals("val-justification", dto.getJustification());
    }

    @Test
    void testEqualsAndHashCode() {
        AccessRequestResponseDto dto1 = AccessRequestResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .requestNumber("test-requestNumber")
            .requestedBy("test-requestedBy")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .status("test-status")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        AccessRequestResponseDto dto2 = AccessRequestResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .requestNumber("test-requestNumber")
            .requestedBy("test-requestedBy")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .status("test-status")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        AccessRequestResponseDto dto = AccessRequestResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .requestNumber("test-requestNumber")
            .requestedBy("test-requestedBy")
            .requestedFor("test-requestedFor")
            .requestType("test-requestType")
            .status("test-status")
            .resourceType("test-resourceType")
            .resourceIds(Collections.emptyList())
            .accessLevel("test-accessLevel")
            .justification("test-justification")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}