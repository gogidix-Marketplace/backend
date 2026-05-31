package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.domain.model.AccessAction;
import com.gogidix.hr.documentmanagement.domain.model.HRDocument;
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
class HRDocument_DocumentAccessLogEntryTest {

        @Test
    void testBuilder() {
        HRDocument.DocumentAccessLogEntry dto = HRDocument.DocumentAccessLogEntry.builder()
                        .logId("test-logId")
            .accessedBy("test-accessedBy")
            .accessedByName("test-accessedByName")
            .action(AccessAction.VIEW)
            .accessTimestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .reason("test-reason")
            .build();
        assertNotNull(dto);
        assertEquals("test-logId", dto.getLogId());
        assertEquals("test-accessedBy", dto.getAccessedBy());
        assertEquals("test-accessedByName", dto.getAccessedByName());
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
        assertEquals("test-reason", dto.getReason());
    }

    @Test
    void testSettersAndGetters() {
        HRDocument.DocumentAccessLogEntry dto = new HRDocument.DocumentAccessLogEntry();
        dto.setLogId("val-logId");
        dto.setAccessedBy("val-accessedBy");
        dto.setAccessedByName("val-accessedByName");
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        dto.setReason("val-reason");
        assertEquals("val-logId", dto.getLogId());
        assertEquals("val-accessedBy", dto.getAccessedBy());
        assertEquals("val-accessedByName", dto.getAccessedByName());
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        HRDocument.DocumentAccessLogEntry dto1 = HRDocument.DocumentAccessLogEntry.builder()
                        .logId("test-logId")
            .accessedBy("test-accessedBy")
            .accessedByName("test-accessedByName")
            .action(AccessAction.VIEW)
            .accessTimestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .reason("test-reason")
            .build();
        HRDocument.DocumentAccessLogEntry dto2 = HRDocument.DocumentAccessLogEntry.builder()
                        .logId("test-logId")
            .accessedBy("test-accessedBy")
            .accessedByName("test-accessedByName")
            .action(AccessAction.VIEW)
            .accessTimestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .reason("test-reason")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        HRDocument.DocumentAccessLogEntry dto = HRDocument.DocumentAccessLogEntry.builder()
                        .logId("test-logId")
            .accessedBy("test-accessedBy")
            .accessedByName("test-accessedByName")
            .action(AccessAction.VIEW)
            .accessTimestamp(Instant.parse("2025-01-15T10:00:00Z"))
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .reason("test-reason")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}