package com.gogidix.customersupport.ticketmanagement.application.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AttachmentDtoTest {

    @Test
    void builder_createsAttachmentDto() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder()
                .fileName("test.txt")
                .fileUrl("http://example.com/file")
                .fileSize("1024")
                .contentType("text/plain")
                .uploadedAt(Instant.now())
                .uploadedBy("user1")
                .build();
        assertNotNull(dto);
        assertEquals("test.txt", dto.getFileName());
        assertEquals("http://example.com/file", dto.getFileUrl());
        assertEquals("1024", dto.getFileSize());
        assertEquals("text/plain", dto.getContentType());
        assertEquals("user1", dto.getUploadedBy());
    }

    @Test
    void equals_sameValues() {
        Instant now = Instant.now();
        TicketResponseDto.AttachmentDto dto1 = TicketResponseDto.AttachmentDto.builder()
                .fileName("f.txt").fileUrl("u").fileSize("1").contentType("t")
                .uploadedAt(now).uploadedBy("u1").build();
        TicketResponseDto.AttachmentDto dto2 = TicketResponseDto.AttachmentDto.builder()
                .fileName("f.txt").fileUrl("u").fileSize("1").contentType("t")
                .uploadedAt(now).uploadedBy("u1").build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void equals_differentValues() {
        TicketResponseDto.AttachmentDto dto1 = TicketResponseDto.AttachmentDto.builder()
                .fileName("a.txt").build();
        TicketResponseDto.AttachmentDto dto2 = TicketResponseDto.AttachmentDto.builder()
                .fileName("b.txt").build();
        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_null() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder().build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_self() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder().build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_otherType() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder().build();
        assertNotEquals("string", dto);
    }

    @Test
    void toString_containsFields() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder()
                .fileName("test.txt").build();
        String s = dto.toString();
        assertNotNull(s);
        assertTrue(s.contains("test.txt"));
    }

    @Test
    void canEqual_sameType() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder().build();
        assertTrue(dto.canEqual(TicketResponseDto.AttachmentDto.builder().build()));
    }

    @Test
    void canEqual_differentType() {
        TicketResponseDto.AttachmentDto dto = TicketResponseDto.AttachmentDto.builder().build();
        assertFalse(dto.canEqual("string"));
    }

    @Test
    void setters_work() {
        TicketResponseDto.AttachmentDto dto = new TicketResponseDto.AttachmentDto();
        dto.setFileName("a");
        dto.setFileUrl("b");
        dto.setFileSize("1");
        dto.setContentType("c");
        dto.setUploadedAt(Instant.now());
        dto.setUploadedBy("d");
        assertEquals("a", dto.getFileName());
        assertEquals("b", dto.getFileUrl());
        assertEquals("1", dto.getFileSize());
        assertEquals("c", dto.getContentType());
        assertEquals("d", dto.getUploadedBy());
    }
}
