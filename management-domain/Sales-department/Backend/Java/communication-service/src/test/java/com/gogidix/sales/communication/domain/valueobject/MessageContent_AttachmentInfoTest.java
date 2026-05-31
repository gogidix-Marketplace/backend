package com.gogidix.sales.communication.domain.valueobject;

import com.gogidix.sales.communication.domain.valueobject.MessageContent;
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
class MessageContent_AttachmentInfoTest {

        @Test
    void testBuilder() {
        MessageContent.AttachmentInfo dto = MessageContent.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-fileUrl", dto.getFileUrl());
    }

    @Test
    void testSettersAndGetters() {
        MessageContent.AttachmentInfo dto = new MessageContent.AttachmentInfo();
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setFileUrl("val-fileUrl");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-fileUrl", dto.getFileUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageContent.AttachmentInfo dto1 = MessageContent.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        MessageContent.AttachmentInfo dto2 = MessageContent.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageContent.AttachmentInfo dto = MessageContent.AttachmentInfo.builder()
                        .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}