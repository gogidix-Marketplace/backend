package com.gogidix.digitalmarketing.contentmanagement.application.dto;

import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceResponseDto;
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
class ContentPieceResponseDtoTest {

        @Test
    void testBuilder() {
        ContentPieceResponseDto dto = ContentPieceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-contentType", dto.getContentType());
        assertEquals("test-author", dto.getAuthor());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-approvalStatus", dto.getApprovalStatus());
        assertEquals("test-isFeatured", dto.getIsFeatured());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        ContentPieceResponseDto dto = new ContentPieceResponseDto();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setTitle("val-title");
        dto.setContentType("val-contentType");
        dto.setAuthor("val-author");
        dto.setStatus("val-status");
        dto.setApprovalStatus("val-approvalStatus");
        dto.setIsFeatured("val-isFeatured");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-contentType", dto.getContentType());
        assertEquals("val-author", dto.getAuthor());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-approvalStatus", dto.getApprovalStatus());
        assertEquals("val-isFeatured", dto.getIsFeatured());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ContentPieceResponseDto dto1 = ContentPieceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ContentPieceResponseDto dto2 = ContentPieceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ContentPieceResponseDto dto = ContentPieceResponseDto.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .createdBy("test-createdBy")
            .createdAt(Instant.parse("2025-01-15T10:00:00Z"))
            .updatedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}