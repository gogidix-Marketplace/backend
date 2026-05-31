package com.gogidix.customersupport.customerportal.application.dto;

import com.gogidix.customersupport.customerportal.application.dto.TicketHistoryResponseDto;
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
class TicketHistoryResponseDto_AttachmentInfoDtoTest {

        @Test
    void testBuilder() {
        TicketHistoryResponseDto.AttachmentInfoDto dto = TicketHistoryResponseDto.AttachmentInfoDto.builder()
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
        TicketHistoryResponseDto.AttachmentInfoDto dto = new TicketHistoryResponseDto.AttachmentInfoDto();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        TicketHistoryResponseDto.AttachmentInfoDto dto1 = TicketHistoryResponseDto.AttachmentInfoDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        TicketHistoryResponseDto.AttachmentInfoDto dto2 = TicketHistoryResponseDto.AttachmentInfoDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TicketHistoryResponseDto.AttachmentInfoDto dto = TicketHistoryResponseDto.AttachmentInfoDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}