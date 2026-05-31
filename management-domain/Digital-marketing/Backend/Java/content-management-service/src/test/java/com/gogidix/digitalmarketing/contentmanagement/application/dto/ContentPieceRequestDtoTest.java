package com.gogidix.digitalmarketing.contentmanagement.application.dto;

import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceRequestDto;
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
class ContentPieceRequestDtoTest {

        @Test
    void testBuilder() {
        ContentPieceRequestDto dto = ContentPieceRequestDto.builder()
                        .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-contentType", dto.getContentType());
        assertEquals("test-author", dto.getAuthor());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-approvalStatus", dto.getApprovalStatus());
        assertEquals("test-isFeatured", dto.getIsFeatured());
    }

    @Test
    void testSettersAndGetters() {
        ContentPieceRequestDto dto = new ContentPieceRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setTitle("val-title");
        dto.setContentType("val-contentType");
        dto.setAuthor("val-author");
        dto.setStatus("val-status");
        dto.setApprovalStatus("val-approvalStatus");
        dto.setIsFeatured("val-isFeatured");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-contentType", dto.getContentType());
        assertEquals("val-author", dto.getAuthor());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-approvalStatus", dto.getApprovalStatus());
        assertEquals("val-isFeatured", dto.getIsFeatured());
    }

    @Test
    void testEqualsAndHashCode() {
        ContentPieceRequestDto dto1 = ContentPieceRequestDto.builder()
                        .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .build();
        ContentPieceRequestDto dto2 = ContentPieceRequestDto.builder()
                        .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ContentPieceRequestDto dto = ContentPieceRequestDto.builder()
                        .tenantId("test-tenantId")
            .title("test-title")
            .contentType("test-contentType")
            .author("test-author")
            .status("test-status")
            .approvalStatus("test-approvalStatus")
            .isFeatured("test-isFeatured")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}