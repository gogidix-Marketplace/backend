package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxFilingController;
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
class TaxFilingController_AttachmentRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingController.AttachmentRequestDto dto = new TaxFilingController.AttachmentRequestDto();
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setStorageLocation("val-storageLocation");
        dto.setUrl("val-url");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-storageLocation", dto.getStorageLocation());
        assertEquals("val-url", dto.getUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingController.AttachmentRequestDto dto1 = new TaxFilingController.AttachmentRequestDto();
        TaxFilingController.AttachmentRequestDto dto2 = new TaxFilingController.AttachmentRequestDto();
        dto1.setFileName("test");
        dto1.setFileType("test");
        dto1.setFileSize(42L);
        dto1.setStorageLocation("test");
        dto1.setUrl("test");
        dto2.setFileName("test");
        dto2.setFileType("test");
        dto2.setFileSize(42L);
        dto2.setStorageLocation("test");
        dto2.setUrl("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setFileName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingController.AttachmentRequestDto dto = new TaxFilingController.AttachmentRequestDto();
        dto.setFileName("test");
        dto.setFileType("test");
        dto.setFileSize(42L);
        dto.setStorageLocation("test");
        dto.setUrl("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingController.AttachmentRequestDto dto = new TaxFilingController.AttachmentRequestDto();
        dto.setFileName("test");
        dto.setFileType("test");
        dto.setFileSize(42L);
        dto.setStorageLocation("test");
        dto.setUrl("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}