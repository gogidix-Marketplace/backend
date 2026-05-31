package com.gogidix.customersupport.customerportal.domain.model;

import com.gogidix.customersupport.customerportal.domain.model.TicketHistory;
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
class TicketHistory_AttachmentInfoTest {

        @Test
    void testBuilder() {
        TicketHistory.AttachmentInfo dto = TicketHistory.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
    }

    @Test
    void testSettersAndGetters() {
        TicketHistory.AttachmentInfo dto = new TicketHistory.AttachmentInfo();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketHistory.AttachmentInfo dto1 = TicketHistory.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TicketHistory.AttachmentInfo dto2 = TicketHistory.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketHistory.AttachmentInfo dto = TicketHistory.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}