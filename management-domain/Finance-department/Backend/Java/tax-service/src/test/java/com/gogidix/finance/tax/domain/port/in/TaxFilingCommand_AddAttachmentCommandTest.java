package com.gogidix.finance.tax.domain.port.in;

import com.gogidix.finance.tax.domain.port.in.TaxFilingCommand;
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
class TaxFilingCommand_AddAttachmentCommandTest {

        @Test
    void testSettersAndGetters() {
        TaxFilingCommand.AddAttachmentCommand dto = new TaxFilingCommand.AddAttachmentCommand();
        dto.setTenantId("val-tenantId");
        dto.setFilingId("val-filingId");
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setStorageLocation("val-storageLocation");
        dto.setUrl("val-url");
        dto.setUploadedBy("val-uploadedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-filingId", dto.getFilingId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-storageLocation", dto.getStorageLocation());
        assertEquals("val-url", dto.getUrl());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFilingCommand.AddAttachmentCommand dto1 = new TaxFilingCommand.AddAttachmentCommand();
        TaxFilingCommand.AddAttachmentCommand dto2 = new TaxFilingCommand.AddAttachmentCommand();
        dto1.setTenantId("test");
        dto1.setFilingId("test");
        dto1.setFileName("test");
        dto1.setFileType("test");
        dto1.setFileSize(42L);
        dto1.setStorageLocation("test");
        dto1.setUrl("test");
        dto1.setUploadedBy("test");
        dto2.setTenantId("test");
        dto2.setFilingId("test");
        dto2.setFileName("test");
        dto2.setFileType("test");
        dto2.setFileSize(42L);
        dto2.setStorageLocation("test");
        dto2.setUrl("test");
        dto2.setUploadedBy("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTenantId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxFilingCommand.AddAttachmentCommand dto = new TaxFilingCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setFileName("test");
        dto.setFileType("test");
        dto.setFileSize(42L);
        dto.setStorageLocation("test");
        dto.setUrl("test");
        dto.setUploadedBy("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxFilingCommand.AddAttachmentCommand dto = new TaxFilingCommand.AddAttachmentCommand();
        dto.setTenantId("test");
        dto.setFilingId("test");
        dto.setFileName("test");
        dto.setFileType("test");
        dto.setFileSize(42L);
        dto.setStorageLocation("test");
        dto.setUrl("test");
        dto.setUploadedBy("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}